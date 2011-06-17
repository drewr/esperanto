(ns esperanto.core
  (:require [cheshire :as json])
  (:import
   [org.elasticsearch.common.transport InetSocketTransportAddress]
   [org.elasticsearch.action.admin.cluster.health ClusterHealthRequest]
   [org.elasticsearch.action.admin.indices.delete DeleteIndexRequest]
   [org.elasticsearch.client.action.index IndexRequestBuilder]
   [org.elasticsearch.client.action.bulk BulkRequestBuilder]
   [org.elasticsearch.client.transport TransportClient]
   [org.elasticsearch.common.settings ImmutableSettings]
   [org.elasticsearch.node NodeBuilder]
   [org.elasticsearch.action.count CountRequest]
   [org.elasticsearch.index.query.xcontent QueryStringQueryBuilder]))

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

(defn make-index-request
  ([client idx source]
     (make-index-request client idx (or (source "_type")
                                        (source "type"))
                         source))
  ([client idx type source]
     (doto (IndexRequestBuilder. client idx)
       (.setType type)
       (.setSource source))))

(defn make-bulk-request [client reqs]
  (loop [br (BulkRequestBuilder. client)
         r reqs]
    (if-not (seq reqs)
      br
      (do
        (println "adding" (first reqs))
        (.add br (first reqs))
        (recur br (rest reqs))))))

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

(defn count-doc
  ([client index-name]
     (count-doc client index-name "*:*"))
  ([client index-name q]
     (-> client
         (.count
          (.query
           (CountRequest. (into-array String [index-name]))
           (QueryStringQueryBuilder. q)))
         .actionGet
         .count)))

