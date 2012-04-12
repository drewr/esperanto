(ns esperanto.utils)

(defn args [opts]
  (condp instance? (first opts)
    clojure.lang.Keyword (apply hash-map opts)
    clojure.lang.PersistentArrayMap (first opts)
    (throw (Exception. (str "can't make sense of args " (str opts))))))
