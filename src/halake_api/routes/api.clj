(ns halake-api.routes.api
  (:require [compojure.core :refer :all]
            [environ.core :refer [env]]
            [ring.util.response :as response]
            [taoensso.timbre :as timbre]
            [ring.middleware.format-response :refer [wrap-json-response]]
            [clj-time
             [core :as t]
             [coerce :as coerce]]
            [halake-api.db.status :as status]))

(def halake-api-key (env :halake-api-key))

(defn to-date-str [date]
  (-> (coerce/from-date date)
      (t/to-time-zone (t/time-zone-for-id "Asia/Tokyo"))
      str))

;;
;; Unauthorized routes
;;
(defn retrieve-latest [key]
  (if-let [status (-> (status/retrieve-latest-status key)
                      (select-keys [key :updated-at])
                      (update-in [:updated-at] to-date-str)
                      (assoc :status :ok))]
    (response/response status)))

(defroutes unauthorized-routes
  (GET "/temperature" []
       (retrieve-latest :temperature))
  (GET "/humidity" []
       (retrieve-latest :humidity))
  (GET "/congestion" []
       (retrieve-latest :congestion)))

;;
;; Authorized routes
;;
(defn safe-parse [s]
  (try
    (Float/parseFloat s)
    (catch NumberFormatException _
      nil)))

(defn assoc-some [m k v]
  (cond-> m v (assoc k v)))

(defn update-status [temperature humidity congestion]
  (try
    (-> {}
        (assoc-some :temperature (some-> temperature safe-parse))
        (assoc-some :humidity (some-> humidity safe-parse))
        (assoc-some :congestion (some-> congestion safe-parse))
        status/update-status)
    (response/response {:status :ok})
    (catch Exception e
      (timbre/error e)
      (response/response {:status :error, :message "Status update failed"}))))

(defroutes authorized-routes
  (POST "/status/update" {{:keys [temperature humidity congestion]} :params}
        (update-status temperature humidity congestion)))

(defn wrap-authorize [app]
  (fn [{{:strs [x-halake-api-key]} :headers :as req}]
    (if (and (not (nil? x-halake-api-key))
             (= x-halake-api-key halake-api-key))
      (app req)
      (response/response {:status :error, :messsage "Authorization required"}))))

;;
;; API routes
;;
(defroutes api-routes
  (-> (context "/1.0" _
        (context "/auth" _
          (wrap-authorize authorized-routes))
        unauthorized-routes)
      wrap-json-response))
