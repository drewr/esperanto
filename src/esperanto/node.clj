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

(defn rand-cluster-name []
  (str "cluster_"
       (-> (java.util.UUID/randomUUID) str
           .toUpperCase (subs 0 8))))

(defn make-test-node [& {:as settings}]
  (apply make-node (flatten
                    (merge
                     {"index.store.type" "ram"
                      "node.local" "true"
                      "cluster.name" (rand-cluster-name)}
                     settings))))

(defn node-fixture [node]
  (fn [f]
    (try
      (.start node)
      (f)
      (finally
       (.stop node)))))

