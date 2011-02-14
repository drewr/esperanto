(ns esproto.core
  (:import
   [org.elasticsearch.common.transport InetSocketTransportAddress]
   [org.elasticsearch.action.admin.cluster.health ClusterHealthRequest]
   [org.elasticsearch.client.action.index IndexRequestBuilder]
   [org.elasticsearch.client.transport TransportClient]
   [org.elasticsearch.common.settings ImmutableSettings]
   [org.elasticsearch.node NodeBuilder]
   [org.scribe.builder ServiceBuilder]
   [org.scribe.builder.api TwitterApi]
   ))

(defn make-node [& {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put (or settings {})))]
    (-> (NodeBuilder/nodeBuilder) (.settings s) .build)))

(defn make-client-node [& {:as settings}]
  (apply make-node (-> (merge settings {"node.client" "true"})
                       seq flatten)))

(defn make-transport-client [host port cluster & {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put "cluster.name" cluster)
            (.put (or settings {})))]
    (doto (TransportClient. s)
      (.addTransportAddress (InetSocketTransportAddress. host port)))))

(defn make-index-request [client idx payload]
  (doto (IndexRequestBuilder. client idx)
    (.setSource payload)
    (.setType (payload "type"))))

(defn health [client cluster]
  (-> client .admin
      .cluster
      (.health (ClusterHealthRequest. (make-array String 0)))
      .actionGet))

