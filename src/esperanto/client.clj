(ns esperanto.client
  (:use [esperanto.node :only [make-node]])
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.common.transport InetSocketTransportAddress)
           (org.elasticsearch.client.transport TransportClient)))

(defn make-client-node [& {:as settings}]
  (apply make-node (-> (merge settings {"node.client" "true"})
                       seq flatten)))

(defn make-transport-client [host port cluster & {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put "cluster.name" cluster)
            (.put (or settings {})))]
    (doto (TransportClient. s)
      (.addTransportAddress (InetSocketTransportAddress. host port)))))

