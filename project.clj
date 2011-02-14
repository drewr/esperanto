(defproject esperanto "1.0.0-SNAPSHOT"
  :description "You know, for clojure"
  :repositories {"sonatype"
                 "http://oss.sonatype.org/content/repositories/releases/"}
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.elasticsearch/elasticsearch "0.14.3"]
                 [org.scribe/scribe "1.0.9"]]
  :dev-dependencies [[swank-clojure "1.2.1"]])
