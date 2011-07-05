(ns esperanto.action
  (:import (org.elasticsearch.action ActionListener)
           (org.elasticsearch.indices IndexMissingException)))

(defn execute
  ([request]
     (future
       (try
         (-> request .execute .actionGet)
         (catch IndexMissingException _
           nil))))
  ([request listener]
     (.execute request
               (reify ActionListener
                 (onResponse [_ resp] (listener resp))
                 (onFailure [_ e] (listener e))))))

