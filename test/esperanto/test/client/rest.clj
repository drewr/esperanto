(ns esperanto.test.client.rest
  (:use [clojure.test]
        [esperanto.admin.indices :only [refresh index-fixture]]
        [esperanto.node :only [make-test-node node-fixture]])
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]
            [esperanto.client.rest :as es]))

(def node (make-test-node {}))

(def index "twitter")

(def doc {:type "tweet"
          :text "The quick brown fox jumps over the lazy dog"})

(defn port [n]
  (-> n .injector
      (.getInstance org.elasticsearch.http.HttpServerTransport)
      .boundAddress
      .boundAddress
      .address
      .getPort))

(defn host [n]
  (format "http://localhost:%d" (port n)))

(defn url
  ([n]
     (host n))
  ([n index]
     (format "%s/%s" (host n) index))
  ([n index type]
     (format "%s/%s/%s" (host n) index type))
  ([n index type id]
     (format "%s/%s/%s/%s" (host n) index type id)))

(defn doc-seq [n]
  (for [i (range n)]
    (merge doc {:id (str i) :num i})))

(use-fixtures :once
  (node-fixture node))

(deftest t-all
  (let [_idx "idx"
        _type "typ"
        ct 11]
    (es/data:load {:url (url node)
                   :index _idx
                   :type _type
                   :doc-seq (map json/encode (doc-seq ct))})
    (es/index:refresh {:url (url node _idx)})
    (is (= ct (-> {:url (url node _idx)} es/index:count :body :count)))
    (is (< 4.99999999
           (-> {:url (url node _idx)
                :body {:query {:match_all {}}
                       :facets {:foo
                                {:statistical
                                 {:field :num}}}}}
               es/index:search :body :facets :foo :mean)
           5.00000001))))
