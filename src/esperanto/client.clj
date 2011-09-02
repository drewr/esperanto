(ns esperanto.client
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.common.transport InetSocketTransportAddress)
           (org.elasticsearch.client.transport TransportClient)))

(defn make-transport-client [{:keys [host port cluster] :as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put "cluster.name" cluster)
            (.put (or settings {})))]
    (doto (TransportClient. s)
      (.addTransportAddress (InetSocketTransportAddress. host port)))))

