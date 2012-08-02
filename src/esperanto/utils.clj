(ns esperanto.utils)

(defn args [opts]
  (condp instance? (first opts)
    clojure.lang.Keyword (apply hash-map opts)
    clojure.lang.PersistentArrayMap (first opts)
    clojure.lang.PersistentHashMap (first opts)
    (throw (Exception. (str "can't make sense of args " (str opts))))))

(defn take-bytes [bytes coll]
  (lazy-seq
    (when (and (seq coll) (pos? bytes))
      (cons (first coll)
            (take-bytes (- bytes (count (first coll))) (rest coll))))))

(defn partition-bytes [bytes coll]
  (lazy-seq
    (when-let [s (seq coll)]
      (let [seg (doall (take-bytes bytes s))]
        (cons seg (partition-bytes bytes (nthrest s (count seg))))))))

