(defn checkVectorsEqual [args]
  (every? (partial == (count (first args))) (mapv count args)))

(defn isVector [v]
  (and (vector? v) (every? number? v)))

(defn isMatrix [m]
  (and (vector? m) (every? isVector m) (checkVectorsEqual m)))

(defn funByElements [f args]
  {:pre [(checkVectorsEqual args)]}
    (if (== (count args) 1)
       (mapv f (first args))
       (reduce
          (fn [a b]
            (loop [ans [] x a y b]
              (if (== (count x) 0)
                ans
                (recur (conj ans (f (first x) (first y))) (rest x) (rest y))))) args)))

(defn getRow [m i] (mapv (fn [v] (nth v i)) m))

(defn v+ [& args]
  {:pre [(every? isVector args)]}
  (funByElements + args))

(defn v- [& args]
  {:pre [(every? isVector args)]}
  (funByElements - args))

(defn v* [& args]
  {:pre [(every? isVector args)]}
  (funByElements * args))

(defn v*s [v & args]
  {:pre [(isVector v) (every? number? args)]}
  (reduce (fn [v x] (mapv (partial * x) v)) v args))

(defn scalar [a b]
  {:pre [(isVector a) (isVector b) (== (count a) (count b))]}
  (reduce + (v* a b)))

(defn vect [& args]
  {:pre [(every? isVector args) (checkVectorsEqual args) (== (count (first args)) 3)]}
  (reduce (fn ([a b] (vector
   (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))))) args))

(defn m+ [& args]
  {:pre [(every? isMatrix args)]}
  (funByElements v+ args))

(defn m- [& args]
  {:pre [(every? isMatrix args)]}
  (funByElements v- args))

(defn m* [& args]
  {:pre [(every? isMatrix args)]}
  (funByElements v* args))

(defn m*s [m & args]
  {:pre [(isMatrix m) (every? number? args)]}
  (reduce (fn [m s] (mapv (fn [v] (v*s v s)) m)) m args))

(defn m*v [m v]
  {:pre [(isMatrix m) (isVector v) (== (count (first m)) (count v))]}
  (mapv (fn [a] (reduce + (v* a v))) m))

(defn transpose [m]
  {:pre [(isMatrix m)]}
  (loop [ans [] i 0]
    (if (== i (count (first m)))
      ans
      (recur (conj ans (getRow m i)) (+ i 1)))))

(defn m*m [& args]
  (reduce (fn [a b]
    {:pre [(isMatrix a) (isMatrix b)]}
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






