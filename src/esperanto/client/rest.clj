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
  (let [o (u/args opts)]
    (query (http/uri-append url "_bulk")
           (merge {:method :post} o
                  {:body (str (:body o) "\n")}))))

(defn data:load [url & {:as opts}]
  (let [o (merge {:method :put
                  :bulksize 100} opts)
        metas (repeat (json/encode {:index
                                    {:_index (:index o)
                                     :_type (:type o)}}))
        batches (map #(apply str (interpose "\n" %))
                     (for [batch (partition-all (:bulksize o) (:doc-seq o))]
                       (interleave (take (count batch) metas) batch)))]
    (doseq [b batches]
      (index:bulk url (assoc o :body b)))))
