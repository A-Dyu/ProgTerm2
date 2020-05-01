(defn checkVectorsEqual [args] (or (every? number? args) (and (every? vector? args) (every? (partial == (count (first args))) (mapv count args)))))

(defn isVector? [v] (and (vector? v) (every? number? v)))

(defn isMatrix? [m] (and (vector? m) (every? isVector? m) (checkVectorsEqual m)))

(defn isTensor? [t] (or (every? number? t) (and (checkVectorsEqual t) (every? isTensor? t))))

(defn funByElements [f checkArgs]
  (fn [& args]
    {:pre [(checkVectorsEqual args) (every? checkArgs args)]
     :post [(checkArgs %)]}
      (if (every? number? args) (apply f args) (apply mapv f args))))

(def v+ (funByElements + isVector?))

(def v- (funByElements - isVector?))

(def v* (funByElements * isVector?))

(defn v*s [v & args]
  {:pre [(isVector? v) (every? number? args)]
   :post [(isVector? %)]}
    (mapv (partial * (apply * args)) v))

(defn scalar [a b]
  {:pre [(isVector? a) (isVector? b) (checkVectorsEqual [a b])]
   :post [(number? %)]}
    (apply + (v* a b)))

(defn vect [& args]
  {:pre [(every? isVector? args) (every? (fn [x] (== (count x) 3)) args)]
   :post [(isVector? %)]}
    (reduce (fn [a b] (letfn [(vectCord [x y] (- (* (nth a x) (nth b y)) (* (nth a y) (nth b x))))]
      (vector (vectCord 1 2) (vectCord 2 0) (vectCord 0 1)))) args))

(def m+ (funByElements v+ isMatrix?))

(def m- (funByElements v- isMatrix?))

(def m* (funByElements v* isMatrix?))

(defn m*s [m & args]
  {:pre [(isMatrix? m) (every? number? args)]
   :post [(isMatrix? %)]}
    (mapv (fn [v] (apply v* v args)) m))

(defn m*v [m v]
  {:pre [(isMatrix? m)]
   :post [(isVector? %)]}
    (mapv (partial apply +) (mapv (partial v* v) m)))

(defn transpose [m]
  {:pre [(isMatrix? m)]
   :post [(isMatrix? %)]}
    (apply mapv vector m))

(defn m*m [& args]
  {:pre [(every? isMatrix? args)]
   :post [(isMatrix? %)]}
    (reduce (fn [a b] (mapv (partial m*v (transpose b)) a)) args))

(defn broadcast [args]
  { :pre [(every? isTensor? args)]
    :post [(fn [args] (if (every? number? args) true (and (checkVectorsEqual args) (recur (mapv first args)))))]}
    (letfn [(getLevel [t] {:pre [(or (number? t) (vector? t))]} (if (number? t) 0 (+ (getLevel (first t)) 1)))
             (getMaxByType [& args] (if (== (count args) 1) (first args)
              (let [mx (apply getMaxByType (rest args)) mxt (getLevel mx) curt (getLevel (first args))]
                (if (> mxt curt)
                  mx
                  (first args)))))
             (castTensor [a b] (if (== (getLevel a) (getLevel b)) a (apply vector (repeat (count b) (castTensor a (first b))))))]
      (mapv (fn [a] (castTensor a (apply getMaxByType args))) args)))

(defn tensOp [op tOp] (funByElements (fn [args] (if (every? number? args) (apply op args) (apply tOp args))) isTensor?))

(def b+ (tensOp + b+))

(def b- (tensOp - b-))

(def b* (tensOp * b*))

(def bd (tensOp / bd))
