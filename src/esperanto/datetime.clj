(ns esperanto.datetime
  (:import (org.elasticsearch.common.joda Joda)))

(defn parse [dt & {:keys [pattern]}]
  (let [formatter (Joda/forPattern (or pattern "dateOptionalTime"))]
    (try
      (-> formatter .parser (.parseDateTime dt))
      (catch Exception _
        :invalid))))
