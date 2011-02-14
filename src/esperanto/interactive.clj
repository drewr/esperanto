(ns esperanto.interactive
  (:gen-class)
  (:require [clojure.main]))

(def welcome "ElasticSearch Shell")

(defn prompt [cluster]
  (printf "%s> " cluster)
  (flush))

(defn help []
  (println "Type as much of a command as necessary.")
  (let [f "%-20s %-60s"
        lines [["cluster NAME" "Change to cluster NAME."]]]
    (doseq [line lines]
      (println (apply format f line)))))

(defn tokenize [input]
  (.split input "\\s"))

(defn evaluate [world input]
  (let [[cmd & opts] (tokenize input)]
    (merge world
           (condp #(.startsWith %1 %2) cmd
             "help" {:out (help)}
             "cluster" {:out (first opts)}
             {:out (help)}))))

(defn shell []
  (let [world {:cluster "elasticsearch"}]
    (prompt (:cluster world))
    (loop [world world
           input (.trim (.readLine *in*))]
      (if-not (or
               (nil? input)
               (= input "exit"))
        (let [world (evaluate world input)]
          (println (:out world))
          (prompt (:cluster world))
          (recur world (.readLine *in*)))
        (println "\nGoodbye!")))))

(defn -main [& args]
  (println welcome)
  (shell))
