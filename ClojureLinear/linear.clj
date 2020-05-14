(defn checkVectorsEqual [args]
    (and
     (every? vector? args)
     (every? (partial == (count (first args))) (mapv count args))))

(defn isVector? [v]
  (and
   (vector? v)
   (every? number? v)))

(defn isMatrix? [m]
  (and
   (vector? m)
   (every? isVector? m)
   (checkVectorsEqual m)))

(defn getShape [t]
  (cond
   (number? t)
    []
   (vector? t)
    (let[shape (getShape (first t))]
      (if
        (apply = shape (mapv getShape (rest t)))
          (cons (count t) shape)))))


(defn isTensor? [t]
  (not (nil? (getShape t))))

(defn funByElements [f checkArgs]
  (fn [& args]
    {:pre [(or (every? number? args) (checkVectorsEqual args)) (every? checkArgs args)]
     :post [(checkArgs %)]}
    (apply mapv f args)))

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
    (reduce (fn [a b]
              (letfn [
                       (vectCord [x y]
                                 (- (* (nth a x) (nth b y)) (* (nth a y) (nth b x))))]
      (vector (vectCord 1 2) (vectCord 2 0) (vectCord 0 1)))) args))

(def m+ (funByElements v+ isMatrix?))

(def m- (funByElements v- isMatrix?))

(def m* (funByElements v* isMatrix?))

(defn m*s [m & args]
  {:pre [(isMatrix? m) (every? number? args)]
   :post [(isMatrix? %)]}
    (mapv (fn [v] (apply (partial v*s v) args)) m))

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
  {:pre [(every? isTensor? args)]
   :post [(apply = (mapv getShape %))]}
    (letfn [(getLevel [t]
                      (if (number? t)
                        0
                        (+ (getLevel (first t)) 1)))
             (getMaxShape [args]
                          (apply max-key count (mapv getShape args)))
             (castTensor [maxShape a]
                         (let [level (getLevel a)]
                           (letfn [(recurCast [shape]
                                    (if (== level (count shape))
                                     a
                                     (apply vector (repeat (first shape) (recurCast (rest shape))))))]
                                  (recurCast maxShape))))]
           (mapv (partial castTensor (getMaxShape args)) args)))

(defn tensOp [op]
  (letfn [(recurOp [& args] (if (every? number? args)
                              (apply op args)
                              (apply mapv recurOp args)))]
         (fn [& args] (apply recurOp (broadcast args)))))

(def b+ (tensOp +))

(def b- (tensOp -))

(def b* (tensOp *))

(def bd (tensOp /))