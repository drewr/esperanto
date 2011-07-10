(ns esperanto.index
  (:use [esperanto.action :only [execute]])
  (:require [cheshire.core :as json]))

(defn make-index-request
  ([client idx source]
     (make-index-request client idx (or (source "_type")
                                        (source "type"))
                         source))
  ([client idx type source]
     (-> client
         (.prepareIndex idx type)
         (.setSource (json/generate-string source)))))

(defn make-bulk-request [client reqs]
  (loop [br (.prepareBulk client)
         r reqs]
    (if (seq r)
      (do
        (.add br (first r))
        (recur br (rest r)))
      br)))

(defn index-doc [client idx doc]
  @(execute (make-index-request client idx doc)))

(defn index-bulk
  ([client reqs]
     @(execute (make-bulk-request client reqs)))
  ([client idx docs]
     (let [resp @(execute
                  (make-bulk-request
                   client
                   (for [doc docs]
                     (make-index-request client idx doc))))]
       resp)))

(defn bulk-failures [response]
  (->> (.items response)
       (filter #(.isFailed %))
       (map #(.getFailureMessage %))))

