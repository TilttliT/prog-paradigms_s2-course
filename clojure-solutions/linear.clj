(defn vector-of-type? [type v] (and (vector? v) (every? type v)))
(defn same-size? [a b] (== (count a) (count b)))
(defn collection-of-equal-size-vectors? [type l] (and
                                                   (every? (partial vector-of-type? type) l)
                                                   (every? (partial same-size? (first l)) l)))

(defn cordByCordV [f] (fn [& l]
                          {
                           :pre  [(collection-of-equal-size-vectors? number? l)]
                           }
                          (apply mapv f l)))

(def v+ (cordByCordV +))
(def v- (cordByCordV -))
(def v* (cordByCordV *))
(def vd (cordByCordV /))

(defn scalar [& l]
      (apply + (reduce v* l)))

(defn v*s [v & l]
      {
       :pre  [(and (vector-of-type? number? v) (every? number? l))]
       }
      (let [prod (reduce * l)]
           (mapv (partial * prod) v)))

(defn matrix-of-type? [type m] (and (vector? m) (collection-of-equal-size-vectors? type m)))
(defn same-sub-size? [a b] (same-size? (first a) (first b)))
(defn same-size-matrix? [a b] (and (same-size? a b) (same-sub-size? a b)))
(defn collection-of-equal-size-matrix? [type l] (and
                                                  (every? (partial matrix-of-type? type) l)
                                                  (collection-of-equal-size-vectors? vector? l)
                                                  (every? (partial same-sub-size? (first l)) l)))

(defn cordByCordM [f] (fn [& l] {
                                 :pre  [(collection-of-equal-size-matrix? number? l)]
                                 }
                          (apply mapv (cordByCordV f) l)))
(def m+ (cordByCordM +))
(def m- (cordByCordM -))
(def m* (cordByCordM *))
(def md (cordByCordM /))

(defn transpose [m] {
                     :pre  [(matrix-of-type? number? m)]
                     }
      (apply mapv vector m))

(defn m*s [m & l]
      (let [prod (reduce * l)]
           (mapv (fn [v] (v*s v prod)) m)))

(defn m*v [m v]
      (mapv (partial scalar v) m))

(defn m*m [& l] (reduce (fn [a b] (mapv (partial m*v (transpose b)) a)) l))

(defn vect [& l] {
                  :pre  [(collection-of-equal-size-vectors? number? l)]
                  }
      (reduce (fn [a b] (let [[a1 a2 a3] a
                              m [[0 (- a3) a2] [a3 0 (- a1)] [(- a2) a1 0]]]
                             (m*v m b))) l))

(defn tensor-of-number? [t] (or (number? t)
                                (vector-of-type? number? t)
                                (and (vector? t)
                                     (every? (partial same-size? (first t)) t)
                                     (every? tensor-of-number? t))))

(defn tensor-same-size? [a b]
      (if (or (number? a) (number? b))
        (and (number? a) (number? b))
        (and (same-size? a b) (tensor-same-size? (first a) (first b)))))

; a is suffix b
(defn is-suffix? [a b]
      (or (tensor-same-size? a b)
          (and (vector? b) (is-suffix? a (first b)))))

(defn broadcast [a b] {
                       :pre  [(is-suffix? a b)]
                       }
      (if (tensor-same-size? a b)
        a
        (mapv #(broadcast a %) b)))

(defn cordByCordH [f] (fn [& t]
                          (cond
                            (every? number? t) (apply f t)
                            :else (apply mapv (cordByCordH f) t)
                            )))

(defn cordByCordHB [f neutral] (fn [& t] (letfn [(fun [& t] (reduce
                                                              (fn [a b]
                                                                  (if (is-suffix? a b)
                                                                    ((cordByCordH f) (broadcast a b) b)
                                                                    ((cordByCordH f) a (broadcast b a))))
                                                              t))]
                                                (if (== (count t) 1)
                                                  (reduce fun (cons neutral t))
                                                  (reduce fun t)))))

(def hb+ (cordByCordHB + 0))
(def hb- (cordByCordHB - 0))
(def hb* (cordByCordHB * 1))
(def hbd (cordByCordHB / 1))