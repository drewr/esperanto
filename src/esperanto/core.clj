(ns esperanto.core
  (:use [esperanto.index :only [index-bulk]]))

(defn index [client idx x]
  (index-bulk client idx (if (sequential? x) x [x])))
