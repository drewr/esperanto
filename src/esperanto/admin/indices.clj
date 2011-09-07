(ns esperanto.admin.indices
  (:use [esperanto.action :only [execute]]
        [esperanto.admin.cluster :only [wait-for]])
  (:import (org.elasticsearch.client Requests)
           (org.elasticsearch.indices IndexMissingException)))

(defn make-index-creation
  ([client idx]
     (make-index-creation client idx {}))
  ([client idx settings]
     (-> client .admin .indices
         (.prepareCreate idx)
         (.setSettings settings))))

(defn make-refresh-request [client & indices]
  (-> client .admin .indices
      (.prepareRefresh (into-array String indices))))

(defn make-status-request [client & indices]
  (-> client .admin .indices
      (.prepareStatus (into-array String indices))))

(defn make-delete-request [client & indices]
  (-> client .admin .indices
      (.prepareDelete (first indices))))

(defn create
  ([client idx]
     (create client idx {}))
  ([client idx settings]
     @(execute (make-index-creation client idx settings))))

(defn refresh [client & indices]
  @(execute (apply make-refresh-request client indices)))

(defn status [client & indices]
  @(execute (apply make-status-request client indices)))

(defn delete [client & indices]
  @(execute (apply make-delete-request client indices)))

(defn index-fixture [node idx]
  (fn [f]
    (when-let [istat (status (.client node) idx)]
      (delete (.client node) idx))
    (create (.client node) idx {"number_of_shards" "1"
                                "number_of_replicas" "0"})
    (f)))

