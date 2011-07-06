(ns esperanto.test.search
  (:refer-clojure :exclude [doc])
  (:use [esperanto.index]
        [esperanto.search] :reload)
  (:use [clojure.test]
        [esperanto.node :only [make-test-node node-fixture]]
        [esperanto.admin.indices :only [refresh index-fixture]]
        [esperanto.action :only [execute]]))

(def node (make-test-node))

(def index "twitter")

(def doc {"type" "tweet"
          "text" "The quick brown fox jumps over the lazy dog"})

(use-fixtures :once (node-fixture node))
(use-fixtures :each (index-fixture node index))

(deftest t-index-single
  (let [_ (index-doc (.client node) index doc)
        _ (refresh (.client node) index)
        sresp @(execute (make-search-request (.client node) index "quick"))]
    (is (= 1 (-> sresp .hits .totalHits)))))

(deftest t-index-double
  (let [_ (index-doc (.client node) index doc)
        _ (index-doc (.client node) index doc)
        _ (refresh (.client node) index)
        sresp @(execute (make-search-request (.client node) index "quick"))]
    (is (= 2 (-> sresp .hits .totalHits)))))

(deftest t-index-bulk
  (let [resp (index-bulk (.client node) index (repeat 10 doc))
        _ (refresh (.client node) index)
        sresp @(execute (make-search-request (.client node) index "quick"))
        timeout 100]
    (is (< (.getTookInMillis resp) timeout)
        (format "*** bulk index took longer than %dms" timeout))
    (is (not (.hasFailures resp)))
    (is (= 10 (-> sresp .hits .totalHits)))))

