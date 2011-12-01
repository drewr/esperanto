(ns esperanto.lucene
  (:use [clojure.java.io :only [file]])
  (:import (java.io StringReader)
           (org.apache.lucene.analysis SimpleAnalyzer)
           (org.apache.lucene.analysis.standard StandardAnalyzer)
           (org.apache.lucene.analysis.tokenattributes TermAttribute)
           (org.apache.lucene.document Document Field
                                       Field$Store Field$Index)
           (org.apache.lucene.index IndexWriter IndexWriter$MaxFieldLength
                                    Term)
           (org.apache.lucene.queryParser QueryParser)
           (org.apache.lucene.search IndexSearcher)
           (org.apache.lucene.store FSDirectory)
           (org.apache.lucene.util Version)))

(defn index [writer fields]
  (let [doc (Document.)]
    (doseq [field fields]
      (.add doc field))
    (.addDocument writer doc)))

(defn analyze [analyzer s]
  (let [stream (.tokenStream analyzer "field" (StringReader. s))
        term (.addAttribute stream TermAttribute)]
    (loop [st stream v (transient [])]
      (if (.incrementToken st)
        (recur st (conj! v (.term term)))
        (persistent! v)))))

(defn token-seq
  ([rdr]
     (token-seq (StandardAnalyzer. Version/LUCENE_CURRENT) rdr))
  ([analyzer rdr]
     (let [stream (.tokenStream analyzer "field" rdr)
           term (.addAttribute stream TermAttribute)
           step (fn step [st v]
                  (lazy-seq
                   (when (.incrementToken st)
                     (cons (.term term) (step st v)))))]
       (step stream []))))

(defn explain [dir query analyzer version]
  (let [parser (QueryParser. version "text" analyzer)
        query (.parse parser query)
        _ (println query)
        searcher (IndexSearcher. dir)
        docs (.search searcher query 10)]
    (doseq [match (.scoreDocs docs)]
      (let [doc (.doc searcher (.doc match))]
        (println (map #(let [b (.getBinaryValue %)]
                         (if (pos? (count b)) (String. b)))
                      (.getFields doc)))
        (-> (.explain searcher query (.doc match)) str println)))
    (.close searcher)))

(defn explain-fs [dir query]
  (let [dir (-> (file dir) FSDirectory/open)]
    (explain dir query (SimpleAnalyzer.) Version/LUCENE_CURRENT)
    (.close dir)))
