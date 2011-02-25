(ns esperanto.core
  (:import
   [org.elasticsearch.common.transport InetSocketTransportAddress]
   [org.elasticsearch.action.admin.cluster.health ClusterHealthRequest]
   [org.elasticsearch.action.admin.indices.delete DeleteIndexRequest]
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

(defn make-index-request
  ([client idx source]
     (make-index-request client idx (or (source "_type")
                                        (source "type"))
                         source))
  ([client idx type source]
     (doto (IndexRequestBuilder. client idx)
       (.setSource source)
       (.setType type))))

(defn health [client cluster]
  (-> client .admin
      .cluster
      (.health (ClusterHealthRequest. (make-array String 0)))
      .actionGet))

(defn delete-index [client idx]
  (-> client (.admin) (.indices)
      (.delete (DeleteIndexRequest. idx)) (.actionGet)))

(defn execute [request & listener]
  (if listener
    (reify org.elasticsearch.action.ActionListener
           (onResponse [_ resp] (listener resp))
           (onFailure [_ e] (listener e)))
    (future (-> request .execute .actionGet))))
