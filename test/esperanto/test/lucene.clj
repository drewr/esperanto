(ns esperanto.test.lucene
  (:use [esperanto.lucene] :reload)
  (:use [clojure.test])
  (:import (java.io File StringReader)))

(def ^:dynamic *dir* nil)

(def text "The quick brown fox jumps over the lazy dog")

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

(deftest t-analyze
  (let [tokens ["quick" "brown" "fox"
                "jumps" "over" "lazy" "dog"]]
    (is (= tokens (token-seq (StringReader. text))))))
