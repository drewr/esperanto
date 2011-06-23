(ns esperanto.index
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
    (if-not (seq reqs)
      br
      (do
        (println "adding" (first reqs))
        (.add br (first reqs))
        (recur br (rest reqs))))))

