(ns esperanto.node
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.node NodeBuilder)))

(defn make-node [& {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put (merge settings {"transport.tcp.compress" "true"})))]
    (-> (NodeBuilder/nodeBuilder)
        (.loadConfigSettings false)
        (.settings s)
        .build)))

(defn node-fixture [node]
  (fn [f]
    (try
      (.start node)
      (f)
      (finally
       (.stop node)))))

(defn rand-cluster-name []
  (str "cluster_"
       (-> (java.util.UUID/randomUUID) str
           .toUpperCase (subs 0 8))))

