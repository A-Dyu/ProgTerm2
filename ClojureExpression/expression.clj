;***************;
;     HW 10     ;
;***************;

(def exp #(Math/exp %))
(defn _sumexp [& args] (apply + (mapv exp args)))
(defn div [a b] (/ (double a) (double b)))
(defn _divide ([x] (/ (double x)))
              ([x & rst] (reduce div x rst)))

(defn constant [value] (constantly value))

(defn variable [name] #(get % name))

(defn operation [op] (fn [& args] #(apply op (mapv (fn [x] (x %)) args))))


(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation _divide))

(def sumexp (operation _sumexp))
(def softmax (operation (fn [& args]
                          (div (exp (first args)) (apply _sumexp args)))))

(def negate subtract)

(def TOKEN_TO_OP {
                  '+ add
                  '- subtract
                  '* multiply
                  '/ divide
                  'negate negate
                  'sumexp sumexp
                  'softmax softmax})

(defn parseProt [MAP cnst vr]
      (fn [expression]
        (letfn [(parse [token]
                       (cond
                             (list? token) (parseOp token)
                             (number? token) (cnst token)
                             :else (vr (str token))))
                (parseOp [op]
                         (apply (get MAP (first op))
                                (mapv parse (rest op))))]
               (parse (read-string expression)))))

(def parseFunction (parseProt TOKEN_TO_OP constant variable))


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

(def toStringInfix (method :toStringInfix))

(def diff (method :diff))

(defn expressionProt [eval toStr diff & [toInf]]
  {:evaluate eval
   :toString toStr
   :toStringInfix (or toInf toStr)
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

(def OperationProt (let [_op (field :op)
                         _operator (field :operator)
                         _diffRule (field :diffRule)
                         _args (field :args)]
                     (expressionProt
                     (fn [this vars]
                       (apply
                        (_op this)
                        (mapv
                         #(evaluate % vars)
                         (_args this))))
                     (fn [this]
                       (str
                        "("
                        (_operator this)
                        " "
                        (clojure.string/join " " (mapv toString (_args this)))
                        ")"))
                     (fn [this name]
                       ((_diffRule this) (_args this) (mapv #(diff % name) (_args this))))
                      (fn [this]
                         (if (== (count (_args this)) 1)
                           (str
                            (_operator this)
                            "("
                            (toStringInfix (first (_args this)))
                            ")")
                           (str "(" (clojure.string/join
                                     (str " " (_operator this) " ")
                                     (mapv toStringInfix (_args this))) ")"))))))

(defn Operation [op operator diffRule]
  (constructor
    (fn [this & args]
      (assoc this
        :args (vec args)))
    {:prototype OperationProt
     :op op
     :operator operator
     :diffRule diffRule}))

(def Negate (Operation
             -
             'negate
             (fn [args d_args] (Negate (first d_args)))))

(def Add (Operation
           +
          '+
          (fn [args d_args] (apply Add d_args))))

(def Subtract (Operation
                -
               '-
               (fn [args d_args] (apply Subtract d_args))))

(declare Multiply)
(defn mul_diff [args d_args]
  (second (reduce
           (fn [[a da] [b db]] [(Multiply a b)
                                (Add (Multiply da b) (Multiply a db))])
           (mapv vector args d_args))))
(def Multiply (Operation * '* mul_diff))

(declare Divide)
(defn div_diff [[x & rest_x] [dx & rest_dx]]
  (let [rest (apply Multiply rest_x)
        diff_rest (mul_diff rest_x rest_dx)]
    (if (empty? rest_x)
      (Negate (Divide dx (Multiply x x)))
      (Divide
       (Subtract
        (Multiply
         dx
         rest)
        (Multiply
         x
         diff_rest))
       (Multiply rest rest)))))
(def Divide (Operation _divide '/ div_diff))

(declare Sumexp)
(defn diff_sumexp [args d_args]
                    (apply Add
                           (mapv (fn [x y] (Multiply (Sumexp x) y))
                                 args d_args)))
(def Sumexp (Operation _sumexp 'sumexp diff_sumexp))

(def Softmax (Operation
              (fn [& args]
                 (div (exp (first args)) (apply _sumexp args)))
              'softmax
              (fn [args d_args]
                  (div_diff [(Sumexp (first args))
                             (apply Sumexp args)]
                            [(diff_sumexp (vector (first args)) (vector (first d_args)))
                             (diff_sumexp args d_args)]))))

; objects for HW 12 ;
; ***************** ;
(defn bit-func [f]
  (fn [& args]
    (Double/longBitsToDouble (apply f (mapv #(Double/doubleToLongBits %) args)))))
(def And (Operation (bit-func bit-and) '& nil))
(def Xor (Operation (bit-func bit-xor) (symbol "^") nil))
(def Or (Operation (bit-func bit-or) '| nil))
(def Impl (Operation
           (bit-func (fn [& args]
                       (reduce (fn [a b] (bit-or (bit-not a) b)) args)))
           '=>
           nil))
(def Iff (Operation
          (bit-func (fn [& args]
                      (bit-not (apply bit-xor args))))
          '<=>
          nil))
; ***************** ;


(def TOKEN_TO_OBJ {
                   '+ Add
                   '- Subtract
                   '* Multiply
                   '/ Divide
                   'negate Negate
                   'sumexp Sumexp
                   'softmax Softmax
                   '& And
                   (symbol "^") Xor
                   '| Or
                   '=> Impl
                   '<=> Iff})

(def parseObject (parseProt TOKEN_TO_OBJ Constant Variable))


;***************;
;     HW 12     ;
;***************;

;;; start of library block ;;;
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
    "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))
(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(defn +str [p] (+map (partial apply str) p))
(def *digit (+char "0123456789"))
(def *all-chars (mapv char (range 32 128)))
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
;;; end of library block ;;;

(def *number (+seqf
              (fn [sign left dot right] (read-string (str sign (apply str left) dot (apply str right))))
              (+opt (+char "+-"))
              (+plus *digit)
              (+opt (+char "."))
              (+star *digit)))

(def *variable (+map Variable (+str (+plus *letter))))
(def *constant (+map (comp Constant double) *number))

(defn +op [p] (+map (comp (partial get TOKEN_TO_OBJ) symbol str) p))

(def TOKEN_TO_INFIX_UNARY {'- Negate})
(def *negate (+op (+seqf str (+char "n") (+char "e") (+char "g") (+char "a") (+char "t") (+char "e"))))
(def UNARY_OPS [*negate])
(def *unary (apply +or UNARY_OPS))
(defn apply_unary [factor]
  (let [val (second factor)]
    (letfn [(rec [ops]
               (if (== (count ops) 0)
                 val
                 ((first ops) (rec (rest ops)))))]
           (rec (first factor)))))

(declare *bracket)
(def *single_value
  (+map apply_unary (+seq
                    *ws
                    (+star
                     (+map first
                           (+seq *unary *ws)))
                    *ws
                    (+or *constant *variable (delay *bracket)))))

(defn apply_left_binary [bin]
  (reduce
   (fn [a b] ((first b) a (second b)))
   (first bin)
   (partition 2 (rest bin))))

(defn apply_right_binary [rbin]
  (let [bin (reverse rbin)]
    (reduce
     (fn [a b] ((first b) (second b) a))
     (first bin)
     (partition 2 (rest bin)))))

(defn +seq_op [_apply *value *operator]
  (+map _apply (+map flatten (+seq *ws *value *ws (+star (+seq *operator *ws *value *ws))))))

(defn +seq_left_op [*value *operator]
  (+seq_op apply_left_binary *value *operator))

(defn +seq_right_op [*value *operator]
  (+seq_op apply_right_binary *value *operator))

(def *multiply (+op (+char "*")))
(def *divide (+op (+char "/")))
(def *multiplicator (+or *multiply *divide))

(def *term (+seq_left_op *single_value *multiplicator))

(def *add (+op (+char "+")))
(def *subtract (+op (+char "-")))
(def *additor (+or *add *subtract))

(def *term_seq (+seq_left_op *term *additor))

(def *and (+op (+char "&")))
(def *and_seq (+seq_left_op *term_seq *and))

(def *or (+op (+char "|")))
(def *or_seq (+seq_left_op *and_seq *or))

(def *xor (+op (+char "^")))
(def *xor_seq (+seq_left_op *or_seq *xor))

(def *impl (+op (+seqf str (+char "=") (+char ">"))))
(def *impl_seq (+seq_right_op *xor_seq *impl))

(def *iff (+op (+seqf str (+char "<") (+char "=") (+char ">"))))
(def *expression (+seq_left_op *impl_seq *iff))

(def *bracket (+seqn 1 (+char "(") *expression (+char ")")))

(def parseObjectInfix (+parser (+or *expression *bracket)))