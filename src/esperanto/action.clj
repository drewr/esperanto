(ns esperanto.action
  (:import (org.elasticsearch.action ActionListener)))

(defn execute [request & listener]
  (if listener
    (reify ActionListener
           (onResponse [_ resp] (listener resp))
           (onFailure [_ e] (listener e)))
    (future (-> request .execute .actionGet))))

