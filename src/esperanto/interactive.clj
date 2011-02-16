(ns esperanto.interactive
  (:gen-class))

(def welcome "ElasticSearch Shell")

(defn prompt [cluster]
  (printf "%s> " cluster)
  (flush))

(defn help []
  (with-out-str
    (println "Type as much of a command as necessary.")
    (let [f "%-20s %-60s"
          lines [["cluster NAME" "Change to cluster NAME."]
                 ["exit" "Leave essh (or ^D)."]]]
      (doseq [line lines]
        (println (apply format f line))))))

(defn tokenize [input]
  (.split input "\\s+"))

(defn evaluate [world input]
  (let [[cmd & opts] (tokenize input)]
    (merge world
           (if (seq cmd)
             (condp #(.startsWith %1 %2) cmd
               "help" {:out (help)}
               "cluster" {:out ""
                          :cluster (first opts)}
               {:out (help)})
             {:out ""}))))

(defn readl [rdr]
  (let [line (.readLine rdr)]
    (if (string? line)
      (.trim line)
      line)))

(defn exit [input text]
  (when (nil? input)
    (print "\n"))
  (println text))

(defn shell []
  (let [world {:cluster "elasticsearch"}]
    (prompt (:cluster world))
    (loop [world world
           input (readl *in*)]
      (if-not (or
               (nil? input)
               (= input "exit"))
        (let [world (evaluate world input)]
          (when (seq (:out world))
            (println (.trim (:out world))))
          (prompt (:cluster world))
          (recur world (readl *in*)))
        (exit input "Goodbye!")))))

(defn -main [& args]
  (println welcome)
  (shell))
