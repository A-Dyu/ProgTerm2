(defn funToElements [f]
  (fn [a]
    (loop [ans [] v a]
      (if (== (count v) 0)
        ans
        (recur (conj ans (f (first v))) (rest v))))))

(defn funByElements [f]
  (fn [& args]
    (if (== (count args) 1)
       ((funToElements f) (first args))
       (reduce
          (fn [a b]
            (loop [ans [] x a y b]
              (if (== (count x) 0)
                ans
                (recur (conj ans (f (first x) (first y))) (rest x) (rest y))))) args))))


(defn getRow [m i] ((funToElements (fn [v] (nth v i))) m))

(defn vCheck [a b] (== (count a) (count b)))

(defn v+ [& args] (apply (funByElements +) args))

(defn v- [& args] (apply (funByElements -) args))

(defn v* [& args] (apply (funByElements *) args))

(defn v*s [v & args] (reduce (fn [v x] ((funToElements (partial * x)) v)) v args))

(defn scalar [a b] (reduce + (v* a b)))

(defn vect [& args]
  (reduce (fn ([a b] (vector
   (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))))) args))

(defn m+ [& args] (apply (funByElements v+) args))

(defn m- [& args] (apply (funByElements v-) args))

(defn m* [& args] (apply (funByElements v*) args))

(defn m*s [m & args] (reduce (fn [m s] ((funToElements (fn [v] (v*s v s))) m)) m args))

(defn m*v [m v] ((funToElements (fn [a] (reduce + (v* a v)))) m))

(defn transpose [m]
  (loop [ans [] i 0]
    (if (== i (count (first m)))
      ans
      (recur (conj ans (getRow m i)) (+ i 1)))))

(defn m*m [& args]
  (reduce (fn [a b]
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






