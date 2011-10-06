(ns esperanto.search
  (:refer-clojure :exclude [count])
  (:require [cheshire.core :as json])
  (:use [esperanto.action :only [execute]])
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

(defn make-scroll-request [client id timeout]
  (-> client
      (.prepareSearchScroll id)
      (.setScroll (TimeValue/timeValueMillis timeout))))

(defn make-count-request [client idxs query]
  (-> client
      (.prepareCount (into-array idxs))
      (.setQuery (QueryBuilders/queryString query))))

(defn hit->clj [hit]
  (let [src (.sourceAsString hit)]
    (with-meta (merge {:id (.getId hit)}
                      (if src
                        (json/parse-string src :kw)
                        {:ERROR "_source is not enabled"}))
      {:index (.getIndex hit)
       :node (-> hit .getShard .getNodeId)
       :shard (-> hit .getShard .getShardId)
       :sort-vals (seq (.getSortValues hit))})))

(defn search->clj [r]
  (with-meta (map hit->clj (.getHits r))
    {:facets (.getFacets r)
     :shards (.getTotalShards r)
     :shards-bad (.getFailedShards r)
     :shards-good (.getSuccessfulShards r)
     :status (bean (.status r))
     :timed-out? (.isTimedOut r)
     :took (.getTookInMillis r)
     :total (-> r .getHits .getTotalHits)}))

(defn search
  ([client idx]
     (search client idx {:match_all {}}))
  ([client idx query]
     (search->clj
      @(execute (make-search-request client idx query)))))

(defn searchq
  ([client idx query]
     (search client idx {:query_string
                         {:query query}})))

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
     (cons (map hit->clj hits) (scroll client id timeout)))))

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
     (index-seq client idx {:match_all {}}))
  ([client idx query]
     (index-seq client idx query 120000))
  ([client idx query timeout]
     (apply concat (scan client idx query timeout))))

