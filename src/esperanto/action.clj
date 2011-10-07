(ns esperanto.action
  (:require [esperanto.transform.cluster]
            [esperanto.transform.indices]
            [esperanto.transform.index])
  (:use [esperanto.transform :only [transform]])
  (:import (org.elasticsearch.action ActionListener)
           (org.elasticsearch.indices IndexMissingException)))

(defn post [response]
  (transform response))

(defn execute
  ([request]
     (future
       (try
         (-> request .execute .actionGet post)
         (catch IndexMissingException _
           nil))))
  ([request listener]
     (.execute request
               (reify ActionListener
                 (onResponse [_ resp] (listener resp))
                 (onFailure [_ e] (listener e))))))

