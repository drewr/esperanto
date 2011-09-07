(ns esperanto.test.lucene
  (:use [esperanto.lucene] :reload)
  (:use [clojure.test])
  (:import (java.io File)))

(def ^:dynamic *dir* nil)

(defn dir-fixture [f]
  (binding [*dir* (-> (File. "tmp")
                      (File.
                       (str "lucene-"
                            (subs
                             (str
                              (java.util.UUID/randomUUID)) 0 8))))]
    (.mkdir *dir*)
    (f)
    (.delete *dir*)))

(use-fixtures :each dir-fixture)

(deftest t-index-document-1
  (println "****" (str *dir*)))

(deftest t-index-document-2
  (println "****" (str *dir*)))

(deftest t-index-document-3
  (println "****" (str *dir*)))

