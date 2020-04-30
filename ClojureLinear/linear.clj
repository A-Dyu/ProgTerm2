(defn checkVectorsEqual [args] (or (every? number? args) (and (every? vector? args) (every? (partial == (count (first args))) (mapv count args)))))

(defn isVector? [v] (and (vector? v) (every? number? v)))

(defn isMatrix? [m] (and (vector? m) (every? isVector? m) (checkVectorsEqual m)))

(defn isTensor? [t] (or (every? number? t) (and (checkVectorsEqual t) (every? isTensor? t))))

(defn funByElements [f args checkArgs]
  {:pre [(checkVectorsEqual args) (every? checkArgs args)]}
    (if (every? number? args) (apply f args) (apply mapv f args)))

(defn v+ [& args] (funByElements + args isVector?))

(defn v- [& args] (funByElements - args isVector?))

(defn v* [& args] (funByElements * args isVector?))

(defn v*s [v & args]
  {:pre [(isVector? v) (every? number? args)]}
    (mapv (partial * (apply * args)) v))

(defn scalar [a b]
  {:pre [(isVector? a) (isVector? b) (checkVectorsEqual [a b])]}
    (reduce + (v* a b)))

(defn vect [& args]
  {:pre [(every? isVector? args) (every? (fn [x] (== (count x) 3)) args)]}
    (reduce (fn [a b] (letfn [(vectCord [x y] (- (* (nth a x) (nth b y)) (* (nth a y) (nth b x))))]
      (vector (vectCord 1 2) (vectCord 2 0) (vectCord 0 1)))) args))

(defn m+ [& args] (funByElements v+ args isMatrix?))

(defn m- [& args] (funByElements v- args isMatrix?))

(defn m* [& args] (funByElements v* args isMatrix?))

(defn m*s [m & args]
  {:pre [(isMatrix? m) (every? number? args)]}
    (mapv (fn [v] (apply v* v args)) m))

(defn m*v [m v]
  {:pre [(isMatrix? m)]}
    (mapv (fn [a] (reduce + (v* a v))) m))

(defn transpose [m]
  {:pre [(isMatrix? m)]}
    (apply mapv vector m))

(defn m*m [& args] (reduce (fn [a b] (mapv (partial m*v (transpose b)) a)) args))

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

(defn tensOp [op tOp args] (if (every? number? args) (funByElements op args isTensor?) (funByElements tOp args isTensor?)))

(defn b+ [& args] (tensOp + b+ (broadcast args)))

(defn b- [& args] (tensOp - b- (broadcast args)))

(defn b* [& args] (tensOp * b* (broadcast args)))

(defn bd [& args] (tensOp / bd (broadcast args)))
