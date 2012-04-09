(defproject com.draines/esperanto "1.0.0-SNAPSHOT"
  :description "You know, for clojure"
  :repositories {"sonatype"
                 "http://oss.sonatype.org/content/repositories/releases/"
                 "java.net"
                 "http://download.java.net/maven/2/"}
  :dependencies [[org.clojure/clojure "1.4.0-beta5"]
                 [org.clojure.contrib/repl-utils "1.3.0-alpha4"]
                 [org.elasticsearch/elasticsearch "0.17.6"]
                 [cheshire "2.0.2"]
                 [slingshot "0.2.3"]]
  :aot [esperanto.interactive])
