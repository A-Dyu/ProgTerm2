;***************;
;     HW 10     ;
;***************;

(defn exp [x] (Math/exp x))
(defn _sumexp [& args] (apply + (mapv exp args)))
(defn div [a b] (/ (double a) (double b)))

(defn constant [value] (constantly value))

(defn variable [name] (fn [vars] (get vars name)))

(defn operation [op] (fn [& args] (fn [vars] (apply op (mapv (fn [x] (x vars)) args)))))

(defn unary [op] (fn [x] (fn [vars] (op (x vars)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn
                         ([x] (/ x))
                         ([x & rst] (reduce (fn [a b] (div a b)) x rst)))))

(def sumexp (operation _sumexp))
(def softmax (operation (fn [& args]
                          (div (exp (first args)) (apply _sumexp args)))))

(def negate (unary -))

(def TOKEN_TO_OP {
                  '+ add
                  '- subtract
                  '* multiply
                  '/ divide
                  'negate negate
                  'sumexp sumexp
                  'softmax softmax})

(defn parseFunction [expression]
  (letfn [(parseToken [token]
            (cond
              (list? token) (parseOp token)
              (number? token) (constant token)
              :else (variable (str token))))
          (parseOp [op]
            (apply (get TOKEN_TO_OP (first op))
                   (mapv parseToken (rest op))))]
    (parseToken (read-string expression))))

;***************;
;     HW 11     ;
;***************;

(defn proto-get [obj key]
  (cond
   (contains? obj key) (obj key)
   (contains? obj :prototype) (proto-get (obj :prototype) key)
   :else nil))

(defn proto-call [this key & args]
  (apply (proto-get this key) this args))

(defn field [key]
  (fn [this] (proto-get this key)))

(defn method [key]
  (fn [this & args] (apply proto-call this key args)))

(defn constructor [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))

(def evaluate (method :evaluate))

(def toString (method :toString))

(def diff (method :diff))

(defn expressionProt [eval toStr diff]
  {:evaluate eval
   :toString toStr
   :diff diff})

(declare ZERO)
(def Constant (let [_val (field :val)]
             (constructor
              (fn [this val]
                (assoc this
                       :val val))
              (expressionProt
               (fn [this vars] (_val this))
               (fn [this] (format "%.1f" (_val this)))
               (fn [this name] ZERO)))))
(def ZERO (Constant 0))
(def ONE (Constant 1))

(def Variable (let [_name (field :name)]
                (constructor
                 (fn [this name]
                   (assoc this
                          :name name))
                 (expressionProt
                  (fn [this vars] (get vars (_name this)))
                  _name
                  (fn [this name] (if
                                    (= (_name this) name)
                                      ONE
                                      ZERO))))))

(defn Unary [op operator diffRule]
  (let [_x (field :x)]
    (constructor
     (fn [this x]
       (assoc this
              :x x))
     (expressionProt
      (fn [this vars] (op (evaluate (_x this) vars)))
      (fn [this] (str "(" operator " " (toString (_x this)) ")"))
      (fn [this name] (diffRule (_x this) name))))))

(def Negate (Unary
             -
             'negate
             (fn [x name] (Negate (diff x name)))))

(def OperationProt (let [_op (field :op)
                         _operator (field :operator)
                         _diffRule (field :diffRule)
                         _args (field :args)]
                     (expressionProt
                     (fn [this vars] (apply (_op this) (mapv (fn [x] (evaluate x vars)) (_args this))))
                     (fn [this] (str "(" (_operator this) " " (clojure.string/join " " (mapv toString (_args this))) ")"))
                     (fn [this name] ((_diffRule this) (_args this) name)))))

(defn Operation [op operator diffRule]
   (constructor
    (fn [this & args]
      (assoc this
             :args (vec args)))
    {:prototype OperationProt
     :op op
     :operator operator
     :diffRule diffRule}))

(def Add (Operation
           +
          '+
          (fn [args name] (apply Add (mapv (fn [x] (diff x name)) args)))))

(def Subtract (Operation
                -
               '-
               (fn [args name] (apply Subtract (mapv (fn [x] (diff x name)) args)))))

(def Multiply (Operation
                *
               '*
               (fn [args name]
                   (apply Add
                          (mapv (fn [i] (apply Multiply (assoc args i (diff (nth args i) name))))
                                (range (count args)))))))

(def Divide (Operation
             (fn
               ([x] (/ x))
               ([x & rst] (reduce (fn [a b] (div a b)) x rst)))
             '/
             (fn [args name]
               (let [num (first args)
                     denom (apply Multiply (rest args))]
                 (Divide
                  (Subtract
                   (Multiply
                    (diff num name)
                    denom)
                   (Multiply
                     num
                    (diff denom name)))
                  (Multiply
                    denom
                    denom))))))

(def TOKEN_TO_OBJ {
                   '+ Add
                   '- Subtract
                   '* Multiply
                   '/ Divide
                   'negate Negate})

(defn parseObject [expression]
  (letfn [(parseToken [token]
                      (cond
                       (list? token) (parseOp token)
                       (number? token) (Constant token)
                       :else (Variable (str token))))
          (parseOp [op]
                   (apply (get TOKEN_TO_OBJ (first op))
                          (mapv parseToken (rest op))))]
         (parseToken (read-string expression))))





