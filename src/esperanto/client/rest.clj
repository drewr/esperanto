(ns esperanto.client.rest
  (:require [cheshire.core :as json]
            [esperanto.http :as http]
            [esperanto.utils :as u])
  (:import [java.util.concurrent Executors TimeUnit]))

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

(defn responser [a resp]
  (let [f (fn [acc item]
            (let [op (-> item first key)]
              (if (-> item :create :error)
                (update-in acc [op :error] (fnil inc 0))
                (update-in acc [op :ok] (fnil inc 0)))))]
    (reduce f a (-> resp :body :items))))

(defn submit-bulk [pool result url req]
  (.execute pool (fn []
                   (swap! result responser (index:bulk url req)))))

(defn metaize [o doc]
  (let [doc (if (string? doc) (json/decode doc) doc)
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

(defn data:load [url & {:as o}]
  (let [pool (Executors/newFixedThreadPool (:threads o 1))
        result (atom {})]
    (doall
     (apply concat
            (for [b (map #(apply str (interpose "\n" %))
                         (for [batch (if (:bulkbytes o)
                                       (u/partition-bytes (:bulkbytes o)
                                                          (:doc-seq o))
                                       (partition-all (:bulknum o)
                                                      (:doc-seq o)))]
                           (interleave (map (partial metaize o) batch)
                                       batch)))]
              (submit-bulk pool result url (assoc o :body b)))))
    (.shutdown pool)
    (.awaitTermination pool 30 TimeUnit/SECONDS)
    @result))
