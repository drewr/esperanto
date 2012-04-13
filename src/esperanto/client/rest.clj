(ns esperanto.client.rest
  (:require [cheshire.core :as json]
            [esperanto.http :as http]
            [esperanto.utils :as u]))

(defn query [url opts]
  (http/request (merge {:url url} opts)))

(defn index:create [url & {:as opts}]
  (query url (merge {:method :put} opts)))

(defn index:delete [url & {:as opts}]
  (query url (merge {:method :delete} opts)))

(defn index:count [url & {:as opts}]
  (query (format "%s/_count?q=%s" url (:query opts "*:*"))
         (merge {:method :get})))

(defn index:refresh [url & {:as opts}]
  (query (http/uri-append url "_refresh")
         (merge {:method :get} opts {})))

(defn index:search [url & {:as opts}]
  (query (http/uri-append url "_search")
         (merge {:method :post} opts)))

(defn index:bulk [url & opts]
  (let [o (u/args opts)
        body (str (:body o) "\n")]
    (query (http/uri-append url "_bulk")
           (merge {:method :post} o {:body body}))))

(defn data:load [url & {:as opts}]
  (let [o (merge {:method :put
                  :bulksize 100} opts)
        demetaize (fn [doc]
                    (let [doc (json/decode doc)]
                      (json/encode (dissoc doc :_index :_type))))
        metaize (fn [doc]
                  (let [doc (json/decode doc)
                        ix (:index o (doc "_index"))
                        ty (:type o (doc "_type"))
                        id (:id o (doc "_id"))
                        m {:index {:_index ix :_type ty}}
                        m (if id (update-in m [:index] assoc :_id id) m)]
                    (if (not (and ix ty))
                      (throw (Exception.
                              (str "missing _index or _type for doc "
                                   (with-out-str (pr doc))))))
                    (json/encode m)))
        batches (map #(apply str (interpose "\n" %))
                     (for [batch (partition-all (:bulksize o) (:doc-seq o))]
                       (interleave (map metaize batch) (map demetaize batch))))]
    (doseq [b batches]
      (index:bulk url (assoc o :body b)))))
