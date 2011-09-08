(ns esperanto.stress
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [cheshire.core :as json]
            [esperanto.search :as search])
  (:use [esperanto.index :only [index-bulk]]
        [esperanto.admin.indices :only [refresh]]
        [esperanto.node :only [make-client-node]]))

(defn take-rand [n xs]
  (repeatedly n (fn [] (rand-nth xs))))

(def words
  (take 50000
        (-> "/usr/share/dict/words" io/reader line-seq)))

(defn take-docs [n m xs]
  (repeatedly n (fn []
                  {"type" "random"
                   "text" (s/join " " (take-rand m xs))})))

(defn index-rand [client idx corpus x y]
  (index-bulk client idx (take-docs x y corpus)))

(defn save-doc [f doc]
  (locking save-doc
    (with-open [f (io/writer f :append true)]
      (.append f (json/generate-string doc))
      (.newLine f))))



(comment
  (def docs "docs.txt")

  ;; Store docs
  (time (count (pmap #(save-doc docs %) (take-docs 1000 500 words))))

  (def foo (-> (make-client-node
                {"node.name" "Foo!!!"
                 "discovery.zen.ping.multicast.enabled" "false"
                 "discovery.zen.ping.unicast.hosts" "localhost:9300"
                 "cluster.name" "elasticsearch"})
               .start))

  (let [c (.client foo)
        now (System/currentTimeMillis)
        iter 10
        bsize 500
        wsize 500
        t (atom 0)]
    (println now "start" iter bsize wsize)
    (dotimes [_ iter]
      (index-rand c "test" words bsize wsize)
      (refresh c "test")
      (println now (swap! t + batch) (- (System/currentTimeMillis) now)))
    (println now "end" (* iter bsize) @t (- (System/currentTimeMillis) now)))

  (let [c (.client foo)
        idx "test"
        now (System/currentTimeMillis)
        bsize 100]
    (println now "start")
    (doseq [docs (partition-all bsize
                                (map json/parse-string
                                     (line-seq (io/reader docs))))]
      (index-bulk c idx docs)
      (refresh c idx)
      (println now (- (System/currentTimeMillis) now) (search/count c idx)))
    (println now "end" (- (System/currentTimeMillis) now) (search/count c idx)))



)
