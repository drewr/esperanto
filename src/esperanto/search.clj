(ns esperanto.search
  (:import (clojure.lang PersistentVector)
           (org.elasticsearch.client.node NodeClient)
           (org.elasticsearch.index.query.xcontent QueryBuilders)
           (org.elasticsearch.action.count CountRequest)))

(defn count-doc
  ([client index-name]
     (count-doc client index-name "*:*"))
  ([client index-name q]
     (-> client
         (.count
          (.query
           (CountRequest. (into-array String [index-name]))
           (QueryBuilders/queryString q)))
         .actionGet
         .count)))

(def make-search-request* #(vec (map type %&)))

(defmulti make-search-request #(apply #'make-search-request* %&))

(defmethod make-search-request [NodeClient String String]
  [client idx query]
  (make-search-request client [idx] query))

(defmethod make-search-request [NodeClient PersistentVector String]
  [client idxs query]
  (-> client
      (.prepareSearch (into-array idxs))
      (.setQuery (QueryBuilders/queryString query))))

