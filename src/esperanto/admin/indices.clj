(ns esperanto.admin.indices
  (:use [esperanto.action :only [execute]])
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
      (.prepareDelete (into-array String indices))))

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
    (try
      (create (.client node) idx {"number_of_shards" "1"})
      (f)
      (finally
       (when (status (.client node) idx)
         (delete (.client node) idx))))))

