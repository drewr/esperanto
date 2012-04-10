(ns esperanto.test.client.rest
  (:use [clojure.test]
        [esperanto.admin.indices :only [refresh index-fixture]]
        [esperanto.node :only [make-test-node node-fixture]])
  (:require [clojure.java.io :as io]
            [esperanto.client.rest :as es]))

(def node (make-test-node {}))

(def index "twitter")

(def mapping {:tweet
              {:_source {:enabled true}
               :properties
               {:text
                {:store "yes"
                 :type "string"
                 :index "analyzed"}}}})

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
     (format "%s/%s" (url (host n) index) type))
  ([n index type id]
     (format "%s/%s" (url (host n) index type) id)))

(defn doc-seq [n]
  (for [i (range n)]
    (merge doc {:id (str i)})))

(use-fixtures :once
              (node-fixture node)
              (fn [t]
                (es/data:load {:url (url node)
                               :index index
                               :type "tweet"
                               :reader (-> "data/foo.txt"
                                           io/resource
                                           io/reader)})
                (es/index:refresh {:url (url node index)})
                (t)))

(deftest t-count
  (is (= 7 (-> {:url (url node index)} es/index:count :body :count))))

(deftest t-facet-stat
  (is (< 0.999999999999
         (-> {:url (url node index)
              :body {:query {:match_all {}}
                     :facets {:foo
                              {:statistical
                               {:field :foo}}}}}
             es/index:search :body :facets :foo :min)
         1.000000000001)))
