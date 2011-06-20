(ns esperanto.test.lucene
  (:use [esperanto.lucene] :reload)
  (:use [clojure.test])
  (:import (java.io File)))

(declare dir-fixture)

(def *dir* nil)

(use-fixtures :once dir-fixture)

(defn dir-fixture [f]
  (binding [*dir* (-> (File. "tmp")
                            (File.
                             (subs (str (java.util.UUID/randomUUID)) 0 8)))]
    (.mkdir *dir*)
    (f)
    (.delete *dir*)))

(deftest t-test
  (println "****" (str *dir*)))

