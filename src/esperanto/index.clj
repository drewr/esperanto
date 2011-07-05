(ns esperanto.index
  (:use [esperanto.action :only [execute]])
  (:import (org.elasticsearch.client.action.bulk BulkRequestBuilder)
           (org.elasticsearch.client.action.index IndexRequestBuilder)))

(defn make-index-request
  ([client idx source]
     (make-index-request client idx (or (source "_type")
                                        (source "type"))
                         source))
  ([client idx type source]
     (doto (IndexRequestBuilder. client idx)
       (.setType type)
       (.setSource source))))

(defn make-bulk-request [client reqs]
  (loop [br (BulkRequestBuilder. client)
         r reqs]
    (if (seq r)
      (do
        (.add br (first r))
        (recur br (rest r)))
      br)))

(defn index-doc [client idx doc]
  @(execute (make-index-request client idx doc)))

