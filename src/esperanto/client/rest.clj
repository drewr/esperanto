(ns esperanto.client.rest
  (:require [cheshire.core :as json]
            [esperanto.http :as http]))

(defn query [opts]
  (http/request opts))

(defn index:create [opts]
  (query (merge {:method :put} opts)))

(defn index:delete [opts]
  (query (merge {:method :delete} opts)))

(defn index:count [opts]
  (query (merge {:method :get
                 :url (format "%s/_count?q=%s"
                              (:url opts)
                              (:query opts "*:*"))})))

(defn index:refresh [opts]
  (query (merge {:method :get}
                opts
                {:url (http/uri-append (:url opts) "_refresh")})))

(defn index:search [opts]
  (query (merge {:method :post}
                opts
                {:url (http/uri-append (:url opts) "_search")})))

(defn index:bulk [opts]
  (query (merge {:method :post}
                opts
                {:url (http/uri-append (:url opts) "_bulk")
                 :body (str (:body opts) "\n")})))

(defn data:load [opts]
  (let [o (merge {:method :put
                  :bulksize 100} opts)
        metas (repeat (json/encode {:index
                                    {:_index (:index o)
                                     :_type (:type o)}}))
        batches (map #(apply str (interpose "\n" %))
                     (for [batch (partition-all (:bulksize o)
                                                (-> o :reader line-seq))]
                       (interleave (take (count batch) metas) batch)))]
    (doseq [b batches]
      (index:bulk (assoc o :body b)))))
