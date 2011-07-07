(defproject esperanto "1.0.0-SNAPSHOT"
  :description "You know, for clojure"
  :repositories {"sonatype"
                 "http://oss.sonatype.org/content/repositories/releases/"}
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.elasticsearch/elasticsearch "0.17.0-SNAPSHOT"]
                 [org.scribe/scribe "1.0.9"]
                 [cheshire "1.1.0"]]
  :dev-dependencies [[swank-clojure "1.3.1"]]
  :aot [esperanto.interactive])
