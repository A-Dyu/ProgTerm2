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

(def symbolToOps {
                  "+" add
                  "-" subtract
                  "*" multiply
                  "/" divide
                  "negate" negate
                  "sumexp" sumexp
                  "softmax" softmax})

(defn parseFunction [expression]
  (letfn [(parseToken [token]
            (cond
             (list? token)
              (parseOp token)
             (number? token)
              (constant token)
             :else
              (variable (str token))))
          (parseArgs [& argList]
            (apply vector
                   (parseToken (first argList))
                   (if (> (count argList) 1)
                     (apply parseArgs (rest argList))
                     [])))
          (parseOp [op]
                   (apply (get symbolToOps (str (first op)))
                          (apply parseArgs (rest op))))]
    (parseToken (read-string expression))))
