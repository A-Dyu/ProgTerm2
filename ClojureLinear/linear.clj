(defn checkArgs [f args]
  (reduce (fn [a b] (and a b)) true (mapv f args)))

(defn checkVectorsEqual [args]
  (checkArgs (fn [x] (== (count x) (count (first args)))) args))

(defn isMatrix [m]
  (and (vector? m) (checkArgs vector? m) (checkVectorsEqual m)))

(defn funByElements [f args]
  {:pre [(checkArgs vector? args) (checkVectorsEqual args) (checkArgs (fn [x] (= (type x) (type (first args)))) args)]}
    (if (== (count args) 1)
       (mapv  f (first args))
       (reduce
          (fn [a b]
            (loop [ans [] x a y b]
              (if (== (count x) 0)
                ans
                (recur (conj ans (f (first x) (first y))) (rest x) (rest y))))) args)))

(defn getRow [m i] (mapv (fn [v] (nth v i)) m))

(defn v+ [& args] (funByElements + args))

(defn v- [& args] (funByElements - args))

(defn v* [& args] (funByElements * args))

(defn v*s [v & args]
  {:pre [(vector? v) (checkArgs number? v) (checkArgs number? args)]}
  (reduce (fn [v x] (mapv (partial * x) v)) v args))

(defn scalar [a b]
  {:pre [(vector? a) (vector? b) (== (count a) (count b))]}
  (reduce + (v* a b)))

(defn vect [& args]
  {:pre [(checkArgs vector? args) (checkVectorsEqual args) (== (count (first args)) 3) (checkArgs (fn [x] (number? (first x))) args)]}
  (reduce (fn ([a b] (vector
   (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))))) args))

(defn m+ [& args] (funByElements v+ args))

(defn m- [& args] (funByElements v- args))

(defn m* [& args] (funByElements v* args))

(defn m*s [m & args]
  {:pre [(isMatrix m) (checkArgs number? args)]}
  (reduce (fn [m s] (mapv (fn [v] (v*s v s)) m)) m args))

(defn m*v [m v]
  {:pre [(isMatrix m) (vector? v) (number? (first v))]}
  (mapv (fn [a] (reduce + (v* a v))) m))

(defn transpose [m]
  {:pre [(isMatrix m)]}
  (loop [ans [] i 0]
    (if (== i (count (first m)))
      ans
      (recur (conj ans (getRow m i)) (+ i 1)))))

(defn m*m [& args]
  (reduce (fn [a b] {:pre [(isMatrix a) (isMatrix b) (== (count (first a)) (count b))]}
    (letfn [
             (mGetCol [a b j]
               (loop [i 0 ans []]
                 (if (== i (count (first b)))
                   ans
                   (recur (+ i 1) (conj ans (reduce + (v* (getRow b i) (nth a j))))))))
             ]
     (loop [j 0 ans []]
      (if (== j (count a))
        ans
        (recur (+ j 1) (conj ans (mGetCol a b j))))))) args))






