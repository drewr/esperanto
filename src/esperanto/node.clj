(ns esperanto.node
  (:import (org.elasticsearch.common.settings ImmutableSettings)
           (org.elasticsearch.node NodeBuilder)))

(defn make-node [settings]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put (merge settings {"transport.tcp.compress" "true"})))]
    (-> (NodeBuilder/nodeBuilder)
        (.loadConfigSettings false)
        (.settings s)
        .build)))

(defn make-client-node [settings]
  (make-node (merge settings {"node.client" "true"})))

(defn rand-cluster-name []
  (str "cluster_"
       (-> (java.util.UUID/randomUUID) str
           .toUpperCase (subs 0 8))))

(defn make-test-node [settings]
  (make-node (merge
              {"index.store.type" "ram"
               "node.local" "true"
               "transport.host" "localhost"
               "http.bind_host" "localhost"
               "http.publish_host" "localhost"
               "cluster.name" (rand-cluster-name)}
              settings)))

(defn make-test-tcp-node [settings]
  (make-node (merge
              {"index.store.type" "ram"
               "transport.host" "localhost"
               "cluster.name" (rand-cluster-name)}
              settings)))

(defn node-fixture [node]
  (fn [f]
    (try
      (.start node)
      (f)
      (finally
       (.stop node)))))

