(ns esperanto.stress
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [esperanto.search :as search])
  (:use [esperanto.index :only [index-bulk]]
        [esperanto.admin.indices :only [refresh]]
        [esperanto.node :only [make-client-node]]))

(defn take-rand [n xs]
  (repeatedly n (fn [] (rand-nth xs))))

(def words
  (take 10000
        (-> "/usr/share/dict/words" io/reader line-seq)))

(defn take-docs [corpus x y]
  (repeatedly x (fn []
                  {"type" "random"
                   "text" (s/join " " (take-rand y corpus))})))

(defn index-rand [client idx corpus x y]
  (index-bulk client idx (take-docs corpus x y)))

(comment
  (def foo (-> (make-client-node
                {"node.name" "Foo!!!"
                 "discovery.zen.ping.multicast.enabled" "false"
                 "discovery.zen.ping.unicast.hosts" "localhost:9302"
                 "cluster.name" "foo"})
               .start))

  (time
   (let [c (.client foo)]
     (dotimes [_ 1000]
       (index-rand c "test" words 100 100)
       (refresh c "test"))))



)
