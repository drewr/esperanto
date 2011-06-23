(ns esperanto.search
  (:import (org.elasticsearch.action.count CountRequest)
           (org.elasticsearch.index.query.xcontent QueryStringQueryBuilder)))

(defn count-doc
  ([client index-name]
     (count-doc client index-name "*:*"))
  ([client index-name q]
     (-> client
         (.count
          (.query
           (CountRequest. (into-array String [index-name]))
           (QueryStringQueryBuilder. q)))
         .actionGet
         .count)))

