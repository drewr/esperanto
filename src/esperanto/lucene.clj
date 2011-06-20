(ns esperanto.lucene
  (:import (java.io StringReader)
           (org.apache.lucene.analysis.tokenattributes TermAttribute)
           (org.apache.lucene.document Document Field
                                       Field$Store Field$Index)
           (org.apache.lucene.index IndexWriter IndexWriter$MaxFieldLength
                                    Term)
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

