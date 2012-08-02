(defproject com.draines/esperanto "1.0.0-SNAPSHOT"
  :description "You know, for clojure"
  :repositories {"sonatype"
                 "http://oss.sonatype.org/content/repositories/releases/"
                 "java.net"
                 "http://download.java.net/maven/2/"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.elasticsearch/elasticsearch "0.19.2"]
                 [clj-http "0.5.1"]
                 [slingshot "0.10.2"]
                 [cheshire "4.0.1"]]
  :jvm-opts ["-Xmx512m"
             "-XX:MaxPermSize=256m"
             "-XX:+UseParNewGC"
             "-XX:+UseConcMarkSweepGC"
             "-Dfile.encoding=UTF-8"
             "-Dsun.jnu.encoding=UTF-8"
             "-Dsun.io.useCanonCaches=false"]
  :aot [esperanto.interactive])
