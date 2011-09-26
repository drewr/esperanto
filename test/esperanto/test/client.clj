(ns esperanto.test.client
  (:use [esperanto.client] :reload)
  (:use [clojure.test]
        [esperanto.node :only [make-test-tcp-node node-fixture
                               rand-cluster-name]]
        [esperanto.admin.indices :only [refresh index-fixture create]]
        [esperanto.admin.cluster :only [wait-for-green status]]
        [esperanto.index :only [index-doc]]))

(def cluster (rand-cluster-name))
(def index "foo")
(def port (str (+ 40000 (rand-int 1000))))

(deftest t-transport
  (let [doc {:foo 1 :type "test"}
        master (doto (make-test-tcp-node {"transport.tcp.port" port
                                          "cluster.name" cluster})
                 (.start))
        masterwait (wait-for-green (.client master) [] 5000)
        client (make-transport-client {:cluster cluster
                                       :host "localhost"
                                       :port port})]
    (try
      (is (not (.isTimedOut masterwait)))
      (is (not (.isTimedOut (wait-for-green client [] 5000))))
      (is
       (.getAcknowledged
        (create client index
                (cheshire.core/generate-string
                 {:index.number_of_replicas "0"}))))
      (is (not (.isTimedOut (wait-for-green client [index] 5000))))
      (index-doc client index doc)
      (refresh client index)
      (is (not (.isTimedOut (wait-for-green client [index] 5000))))
      (finally
       (.stop master)))))
