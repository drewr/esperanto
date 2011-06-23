(ns esperanto.admin.cluster
  (:import
   (org.elasticsearch.action.admin.cluster.health ClusterHealthRequest)))

(defn health [client cluster]
  (-> client .admin
      .cluster
      (.health (ClusterHealthRequest. (make-array String 0)))
      .actionGet))

