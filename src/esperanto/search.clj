(ns esperanto.search
  (:refer-clojure :exclude [count])
  (:use [esperanto.action :only [execute]])
  (:import (clojure.lang PersistentVector)
           (org.elasticsearch.action.search SearchType)
           (org.elasticsearch.client.node NodeClient)
           (org.elasticsearch.common.unit TimeValue)
           (org.elasticsearch.index.query QueryBuilders)))

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

(defn make-scroll-request [client id timeout]
  (-> client
      (.prepareSearchScroll id)
      (.setScroll (TimeValue/timeValueMillis timeout))))

(defn make-count-request [client idxs query]
  (-> client
      (.prepareCount (into-array idxs))
      (.setQuery (QueryBuilders/queryString query))))

(defn search
  ([client idx]
     (search client idx "*:*"))
  ([client idx query]
     @(execute (make-search-request client idx query))))

(defn count
  ([client idx]
     (count client idx "*:*"))
  ([client idx query]
     (-> (make-count-request client [idx] query)
         execute
         deref
         .getCount)))

(defn scroll [client id timeout]
  (lazy-seq
   (when-let [hits (seq
                    (-> @(execute (make-scroll-request client id timeout))
                        .hits))]
     (cons hits (scroll client id timeout)))))

(defn scan
  ([client idx query timeout]
     (scroll client
             (.scrollId @(execute
                          (-> (make-search-request client idx query)
                              (.setSearchType SearchType/SCAN)
                              (.setSize 50)
                              (.setScroll
                               (TimeValue/timeValueMillis timeout)))))
             timeout)))

(defn index-seq
  ([client idx]
     (index-seq client idx "*:*"))
  ([client idx query]
     (index-seq client idx query 120000))
  ([client idx query timeout]
     (apply concat (scan client idx query timeout))))

