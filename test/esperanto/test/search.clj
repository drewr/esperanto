(ns esperanto.test.search
  (:refer-clojure :exclude [count doc])
  (:use [esperanto.index]
        [esperanto.search] :reload)
  (:use [clojure.test]
        [esperanto.node :only [make-test-node node-fixture]]
        [esperanto.admin.indices :only [refresh index-fixture]]
        [esperanto.action :only [execute]]))

(def node (make-test-node {}))

(def client (.client node))

(def index "twitter")

(def doc {:type "tweet"
          :text "The quick brown fox jumps over the lazy dog"})

(defn doc-seq [n]
  (for [i (range n)]
    (doto (java.util.HashMap.)
      (.put "id" (str i))
      (.put "type" "tweet")
      (.put "text" "The quick brown fox jumps over the lazy dog"))))

(defn doc-seq [n]
  (for [i (range n)]
    (merge doc {:id (str i)})))

(use-fixtures :once (node-fixture node))
(use-fixtures :each (index-fixture node index))

(deftest t-index-single
  (index-doc client index doc)
  (refresh client index)
  (is (= 1 (-> (search client index "quick") meta :total))))

(deftest t-index-double
  (index-doc client index doc)
  (index-doc client index doc)
  (refresh client index)
  (is (= 2 (-> (search client index "quick") meta :total))))

(deftest t-index-bulk
  (let [resp (index-bulk client index (repeat 100 doc))
        _ (refresh client index)
        sresp (search client index "quick")
        timeout 500]
    (is (< (.getTookInMillis resp) timeout)
        (format "*** bulk index took longer than %dms" timeout))
    (is (not (.hasFailures resp)))
    (is (= 100 (-> sresp meta :total)))))

(deftest t-index-seq
  (let [ct 100
        coll (doc-seq ct)
        ids (into #{} (map :id coll))
        bulk (index-bulk client index coll)
        _ (refresh client index)
        sresp (search client index)
        xs (index-seq client index)]
    (is (not (.hasFailures bulk)))
    ;; How many docs does ES think the index has?
    (is (= ct (count client index)))
    ;; How many docs does a search for all docs return?
    (is (= ct (-> sresp meta :total)))
    ;; Are the ids exactly what we indexed?
    (is (= ct (clojure.core/count xs)))
    (is (= ids (into #{} (map :id xs))))))

(deftest t-index-copy
  (let [idx1 "foo"
        idx2 "bar"
        ct 100
        coll (doc-seq ct)
        ids (into #{} (map :id coll))
        _ (index-bulk client idx1 coll)
        _ (refresh client idx1)
        ct2 (copy client idx1 client idx2)
        _ (refresh client idx2)]
    (is (= (clojure.core/count coll)
           (clojure.core/count (index-seq client idx1))
           ct2
           (clojure.core/count (index-seq client idx2))))
    (is (= ids (into #{} (map :id (index-seq client idx2)))))))

(deftest t-count
  (index-doc client index doc)
  (refresh client index)
  (is (= 1 (count client index)))
  (is (= 1 (count client index "quick")))
  (is (= 0 (count client index "foo"))))

