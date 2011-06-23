(ns esperanto.client
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.common.transport InetSocketTransportAddress)
           (org.elasticsearch.client.transport TransportClient)
           (org.elasticsearch.node NodeBuilder)))

(defn make-node [& {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put (merge settings {"transport.tcp.compress" true})))]
    (-> (NodeBuilder/nodeBuilder)
        (.loadConfigSettings false)
        (.settings s)
        .build)))

(defn make-client-node [& {:as settings}]
  (apply make-node (-> (merge settings {"node.client" "true"})
                       seq flatten)))

(defn make-transport-client [host port cluster & {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put "cluster.name" cluster)
            (.put (or settings {})))]
    (doto (TransportClient. s)
      (.addTransportAddress (InetSocketTransportAddress. host port)))))

