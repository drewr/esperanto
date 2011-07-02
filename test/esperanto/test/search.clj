(ns esperanto.test.search
  (:use [esperanto.index] :reload)
  (:use [clojure.test]
        [esperanto.node]
        [esperanto.action :only [execute]]))

(def node (make-node "index.store.type" "ram"
                     "node.local" "true"
                     "cluster.name" (rand-cluster-name)))

(use-fixtures :once (node-fixture node))

(deftest t-index-single
  (let [doc {"type" "tweet"
             "id" "1"
             "text" "The quick brown fox jumps over the lazy dog"}
        req (make-index-request (.client node) "twitter" doc)
        res @(execute req)]
    (is (.getId res))))

(deftest t-index-bulk
  (let [doc {"type" "tweet"
             "text" "The quick brown fox jumps over the lazy dog"}
        reqs (for [i (range 10)]
               (make-index-request (.client node)
                                   "twitter"
                                   (merge doc {"id" i})))
        req (make-bulk-request (.client node) reqs)
        res @(execute req)]
    (is (not (.hasFailures res)))))

