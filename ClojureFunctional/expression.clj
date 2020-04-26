(defn constant [value] (constantly value))

(defn variable [name] (fn [vars] (get vars name)))

(defn operation [op] (fn [& args] (fn [vars] (apply op (mapv (fn [x] (x vars)) args)))))

(defn unary [op] (fn [x] (fn [vars] (op (x vars)))))

(def add (operation +))

(def subtract (operation -))

(def multiply (operation *))

(def divide (operation (fn ([x] (/ (double x))) ([x & rst] (reduce (fn [a b] (/ (double a) (double b))) x rst)))))

(def negate (unary -))

(def symbolToOps {"+" add "-" subtract "*" multiply "/" divide "negate" negate})

(defn parseFunction [expression]
  (letfn [(parseToken [token]
          (if (list? token)
                    (parseOp token)
                    (if (number? token)
                      (constant token)
                      (variable (str token)))))
          (parseArgs [& argList] (apply vector (parseToken (first argList)) (if (> (count argList) 1) (apply parseArgs (rest argList)) [])))
          (parseOp [op] (apply (get symbolToOps (str (first op))) (apply parseArgs (rest op))))]
         (parseToken (read-string expression))))
