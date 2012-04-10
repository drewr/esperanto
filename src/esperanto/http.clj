(ns esperanto.http
  (:require [cheshire.core :as json]
            [clj-http.client :as http]))

(defn request [opts]
  (let [opts (if (map? (:body opts))
               (assoc opts :body (json/encode (:body opts)))
               opts)
        resp (http/request (merge {:throw-entire-message? true}
                                  opts))
        body (if (string? (:body resp))
               (try
                 (json/decode (:body resp) :yesplease)
                 (catch Exception _ (:body resp)))
               (:body resp))
        resp (assoc resp :body body)]
    (condp = (:status resp)
      200 resp
      400 resp
      resp)))

(defn uri-append [base suffix]
  (if (and base (.endsWith base suffix))
    base
    (format "%s/%s" base suffix)))
