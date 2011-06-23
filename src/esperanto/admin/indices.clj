(ns esperanto.admin.indices
  (:import (org.elasticsearch.client Requests)))

(defn status [client & indices]
  (-> client .admin .indices
      (.status (Requests/indicesStatusRequest
                (into-array String indices)))))

(defn delete [client idx]
  (-> client .admin .indices
      (.delete (Requests/deleteIndexRequest idx))))

