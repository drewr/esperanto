(ns esperanto.test.lucene
  (:use [esperanto.lucene] :reload)
  (:use [clojure.test])
  (:import (java.io File StringReader)
           (org.apache.lucene.analysis.standard StandardAnalyzer)
           (org.apache.lucene.util Version)))

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
    (is (= tokens (analyze
                   (StandardAnalyzer. Version/LUCENE_31) text)))
    (is (= tokens (token-seq
                   (StandardAnalyzer. Version/LUCENE_31)
                   (StringReader. text))))))
