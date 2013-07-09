(ns esperanto.profile
  (:require [clj-http.client :as http]))

(defn run-url-times
  ([ep n]
     (run-url-times ep 9200 n))
  ([ep port n]
     (let [start (System/currentTimeMillis)]
       (dotimes [_ n]
         (http/get (format "http://localhost:%d/%s" port ep)))
       (- (System/currentTimeMillis) start))))

(defn run-url-set [url]
  (for [n [1 10 100 1000 10000]]
    {:url url
     :requests n
     :took (run-url-times url n)}))

(defn print-set [results]
  (doseq [r results]
    (apply printf "%s %8d %8d\n"
           ((juxt :url :requests :took) r))))
