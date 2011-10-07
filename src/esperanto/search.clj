(ns esperanto.search
  (:refer-clojure :exclude [count])
  (:require [cheshire.core :as json])
  (:use [esperanto.action :only [execute]]
        [esperanto.transform :only [transform]])
  (:import (org.elasticsearch.action.search SearchType)
           (org.elasticsearch.client.node NodeClient)
           (org.elasticsearch.common.unit TimeValue)
           (org.elasticsearch.index.query QueryBuilders)))

(defn make-search-request [client idxs query]
  (let [idxs (if (sequential? idxs) idxs (vector idxs))
        idxs (into-array String idxs)]
    (-> client
        (.prepareSearch idxs)
        (.setQuery (json/generate-string query)))))

(defn make-scan-request [client idx query batchsize timeout]
  (-> (make-search-request client idx query)
      (.setSearchType SearchType/SCAN)
      (.setSize batchsize)
      (.setScroll (TimeValue/timeValueMillis timeout))))

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
     (search client idx {:match_all {}}))
  ([client idx query]
     @(execute (make-search-request client idx query))))

(defn searchq
  ([client idx query]
     (search client idx {:query_string
                         {:query query}})))

(defn count
  ([client idx]
     (count client idx "*:*"))
  ([client idx query]
     (:count @(execute (make-count-request client [idx] query)))))

(defn scroll [client id timeout]
  (lazy-seq
   (when-let [hits (seq (-> @(execute
                              (make-scroll-request client id timeout))))]
     (cons hits (scroll client id timeout)))))

(defn scan
  ([client idx query timeout]
     (let [id (-> @(execute (make-scan-request client idx query 100 timeout))
                  meta :scroll-id)]
       (scroll client id timeout))))

(defn index-seq
  ([client idx]
     (index-seq client idx {:match_all {}}))
  ([client idx query]
     (index-seq client idx query 120000))
  ([client idx query timeout]
     (apply concat (scan client idx query timeout))))

