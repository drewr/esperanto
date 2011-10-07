(ns esperanto.admin.cluster
  (:use [esperanto.action :only [execute]])
  (:import (org.elasticsearch.action.admin.cluster.health
            ClusterHealthStatus ClusterHealthResponse)
           (org.elasticsearch.common.unit TimeValue)))

(def statuses {:green ClusterHealthStatus/GREEN
               :yellow ClusterHealthStatus/YELLOW
               :red ClusterHealthStatus/RED
               ClusterHealthStatus/GREEN :green
               ClusterHealthStatus/YELLOW :yellow
               ClusterHealthStatus/RED :red})

(defn make-health-request [client indices]
  (-> client .admin .cluster
      (.prepareHealth (into-array String indices))))

(defn health [client indices]
  @(execute (make-health-request client indices)))

(defn wait-for
  ([status client indices]
     (wait-for status client indices 10000))
  ([status client indices timeout]
     (-> (make-health-request client indices)
         (.setWaitForStatus (statuses status))
         (.setTimeout (TimeValue/timeValueMillis timeout))
         execute
         deref)))

(defn wait-for-yellow [client indices timeout]
  (wait-for :yellow client indices timeout))

(defn wait-for-green [client indices timeout]
  (wait-for :green client indices timeout))

(defn to-status [status]
  (cond
   (map? status) (get statuses (:status status))
   (instance? ClusterHealthResponse status) (-> status .status to-status)
   :else (statuses status)))

(defn status [client & indices]
  (-> (health client indices) :status to-status))
