(ns esperanto.test.admin.cluster
  (:use [esperanto.admin.cluster] :reload)
  (:use [clojure.test]
        [esperanto.node :only [node-fixture make-test-node]]
        [esperanto.admin.indices :only [index-fixture]]))

(def node1 (make-test-node {"cluster.name" "e.t.a.cluster"}))
(def node2 (make-test-node {"cluster.name" "e.t.a.cluster"}))
(def client (.client node1))

(def index "foo")

(use-fixtures :once
              (node-fixture node1)
              (node-fixture node2))
(use-fixtures :each (index-fixture node1 index))

(deftest t-health
  (is (= :green (to-status (wait-for :green client [index] 1000))))
  (is (= :green (status client index))))

