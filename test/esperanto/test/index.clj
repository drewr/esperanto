(ns esperanto.test.index
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
        res @(execute
              (make-index-request (.client node) "twitter" doc))]
    (is (.getId res))))

