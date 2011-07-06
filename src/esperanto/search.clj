(ns esperanto.search
  (:refer-clojure :exclude [count])
  (:use [esperanto.action :only [execute]])
  (:import (clojure.lang PersistentVector)
           (org.elasticsearch.client.node NodeClient)
           (org.elasticsearch.index.query.xcontent QueryBuilders)))

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

(defn make-count-request [client idxs query]
  (-> client
      (.prepareCount (into-array idxs))
      (.setQuery (QueryBuilders/queryString query))))

(defn count
  ([client idx]
     (count client idx "*:*"))
  ([client idx query]
     @(execute (make-count-request client [idx] query))))

