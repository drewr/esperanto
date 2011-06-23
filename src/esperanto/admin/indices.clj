(ns esperanto.admin.indices
  (:import (org.elasticsearch.client Requests)))

(defn status [client & indices]
  (future
    (-> client .admin .indices
        (.status (Requests/indicesStatusRequest
                  (make-array String 0)))
        .actionGet)))

(defn delete-index [client idx]
  (-> client .admin .indices
      (.delete (Requests/deleteIndexRequest idx))
      .actionGet))

