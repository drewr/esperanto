(defproject esperanto "1.0.0-SNAPSHOT"
  :description "You know, for clojure"
  :repositories {"sonatype"
                 "http://oss.sonatype.org/content/repositories/releases/"
                 "java.net"
                 "http://download.java.net/maven/2/"}
  :dependencies [[org.clojure/clojure "1.3.0-beta3"]
                 [org.clojure.contrib/repl-utils "1.3.0-alpha1"]
                 [org.elasticsearch/elasticsearch "0.17.6"]
                 [cheshire "2.0.2"]]
  :dev-dependencies [[swank-clojure "1.3.2"]]
  :aot [esperanto.interactive])
