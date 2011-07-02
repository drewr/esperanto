(ns esperanto.node
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.node NodeBuilder)))

(defn make-node [& {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put (merge settings {"transport.tcp.compress" true})))]
    (-> (NodeBuilder/nodeBuilder)
        (.loadConfigSettings false)
        (.settings s)
        .build)))

