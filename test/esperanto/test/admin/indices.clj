(ns esperanto.test.admin.indices
  (:use [esperanto.admin.indices] :reload)
  (:use [clojure.test]
        [esperanto.node :only [make-test-node node-fixture]]))

(def node (make-test-node {"node.name" (str *ns*)}))
(def client (.client node))
(def index "test-idx")

(def doc {:type "tweet"
          :text "The quick brown fox jumps over the lazy dog"})

(use-fixtures :once
  (node-fixture node))

(deftest t-create-delete
  (create client index)
  (refresh client index)
  (is (status client index))
  (delete client index))

(deftest t-shards
  (let [shard-count 1]
    (create client index {"number_of_shards" shard-count
                          "number_of_replicas" "0"})
    (refresh client index)
    (is (= shard-count (count (shards client index))))
    (delete client index)))

