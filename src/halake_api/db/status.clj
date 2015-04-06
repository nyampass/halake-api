(ns halake-api.db.status
  (:require [monger
             [collection :as mc]
             [query :as mq]]
            [halake-api.db.core :refer [db]])
  (:import java.util.Date))

(def ^:const STATUS "status")

(defn update-status [{:keys [temperature humidity congestion] :as status}]
  (let [status (-> (select-keys status [:temperature :humidity :congestion])
                   (assoc :updated-at (Date.)))]
    (mc/insert-and-return db STATUS status)))

(defn retrieve-latest-status []
  (-> (mq/with-collection db STATUS
        (mq/find {})
        (mq/sort {:updated-at -1})
        (mq/limit 1))
      first))
