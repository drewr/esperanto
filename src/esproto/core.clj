(ns esproto.core
  (:import [org.elasticsearch.node NodeBuilder]
           [org.elasticsearch.common.settings ImmutableSettings]
           [org.elasticsearch.client.action.index IndexRequestBuilder]
           [org.scribe.builder ServiceBuilder]
           [org.scribe.builder.api TwitterApi]))

(defn make-node [& {:as settings}]
  (let [s (doto (ImmutableSettings/settingsBuilder)
            (.put (or settings {})))]
    (-> (NodeBuilder/nodeBuilder) (.settings s) .build)))

(defn make-index-request [client idx payload]
  (doto (IndexRequestBuilder. client idx)
    (.setSource payload)
    (.setType (payload "type"))))

