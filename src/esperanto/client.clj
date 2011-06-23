(ns esperanto.client
  (:require [cheshire.core :as json])
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.common.transport InetSocketTransportAddress)
           (org.elasticsearch.client.transport TransportClient)
           (org.elasticsearch.node NodeBuilder)))

(defn node-client
  "Contruct a node-client from a clojure map of settings"
  [settings]
  (-> (NodeBuilder/nodeBuilder)
      (.loadConfigSettings false)
      (.settings (-> (ImmutableSettings/settingsBuilder)
                     (.loadFromSource (json/encode settings))
                     (.put "transport.tcp.compress" true)))
      (.node)
      (.client)))

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

