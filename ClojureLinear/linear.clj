(defn checkVectorsEqual [args] (every? (partial == (count (first args))) (mapv count args)))

(defn isVector [v] (and (vector? v) (every? number? v)))

(defn isMatrix [m] (and (vector? m) (every? isVector m) (checkVectorsEqual m)))

(defn funByElements [f args checkArgs]
  {:pre [(checkVectorsEqual args) (every? checkArgs args)]}
    (apply mapv f args))

(defn v+ [& args] (funByElements + args isVector))

(defn v- [& args] (funByElements - args isVector))

(defn v* [& args] (funByElements * args isVector))

(defn v*s [v & args]
  {:pre [(isVector v) (every? number? args)]}
    (reduce (fn [v x] (mapv (partial * x) v)) v args))

(defn scalar [a b]
  {:pre [(isVector a) (isVector b) (checkVectorsEqual [a b])]}
    (reduce + (v* a b)))

(defn vect [& args]
  {:pre [(every? isVector args) (every? (fn [x] (== (count x) 3)) args)]}
    (reduce (fn [a b] (letfn [(vectCord [x y] (- (* (nth a x) (nth b y)) (* (nth a y) (nth b x))))]
      (vector (vectCord 1 2) (vectCord 2 0) (vectCord 0 1)))) args))

(defn m+ [& args] (funByElements v+ args isMatrix))

(defn m- [& args] (funByElements v- args isMatrix))

(defn m* [& args] (funByElements v* args isMatrix))

(defn m*s [m & args]
  {:pre [(isMatrix m) (every? number? args)]}
    (reduce (fn [m s] (mapv (fn [v] (v*s v s)) m)) m args))

(defn m*v [m v]
  {:pre [(isMatrix m)]}
    (mapv (fn [a] (reduce + (v* a v))) m))

(defn transpose [m]
  {:pre [(isMatrix m)]}
    (apply mapv vector m))

(defn m*m [& args] (reduce (fn [a b] (mapv (partial m*v (transpose b)) a)) args))