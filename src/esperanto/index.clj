(ns esperanto.index
  (:use [esperanto.action :only [execute]]
        [esperanto.search :only [index-seq]]
        [slingshot.slingshot])
  (:require [cheshire.core :as json]))

(defn make-index-request
  ([client idx source]
     (let [type (or (get source :_type)
                    (get source "_type")
                    (get source :type)
                    (get source "type"))]
       (if type
         (make-index-request client idx type source)
         (throw+ ::ENEEDTYPE
                 (with-out-str
                   (binding [*print-length* 3]
                     (print "document needs to have a type:" source)))))))
  ([client idx type source]
     (let [req (-> client
                   (.prepareIndex idx type)
                   (.setSource (json/generate-string source))
                   #_(.setSource source))]
       (if-let [id (or (get source :id)
                       (get source "id"))]
         (-> req (.setId (str id)))
         req))))

(defn make-bulk-request [client reqs]
  (loop [br (.prepareBulk client)
         r reqs]
    (if (seq r)
      (do
        (.add br (first r))
        (recur br (rest r)))
      br)))

(defn index-doc [client idx doc]
  (merge doc
         @(execute (make-index-request client idx doc))))

(defn index-bulk
  ([client reqs]
     @(execute (make-bulk-request client reqs)))
  ([client idx docs]
     (let [resp @(execute
                  (make-bulk-request
                   client
                   (for [doc docs]
                     (doto (make-index-request client idx doc)
                       (-> .request .beforeLocalFork)))))]
       resp)))

(defn bulk-failures [response]
  (->> (.items response)
       (filter #(.isFailed %))
       (map #(.getFailureMessage %))))

(defn copy [client1 idx1 client2 idx2 & {:keys [batchsize]}]
  (count
   (apply concat
          (remove :failed?
                  (map :items (for [docs (partition-all
                                          (or batchsize 50)
                                          (index-seq client1 idx1))]
                                (index-bulk client2 idx2 docs)))))))

