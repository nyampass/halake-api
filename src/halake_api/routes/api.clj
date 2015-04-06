(ns halake-api.routes.api
  (:require [compojure.core :refer :all]
            [environ.core :refer [env]]
            [ring.util.response :as response]
            [taoensso.timbre :as timbre]
            [ring.middleware.format-response :refer [wrap-json-response]]
            [halake-api.db.status :as status]))

(def halake-api-key (env :halake-api-key))

;;
;; Unauthorized routes
;;
(defn retrieve-latest [key]
  (if-let [status (status/retrieve-latest-status)]
    (response/response {:status :ok, key (get status key)})))

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
(defn update-status [temperature humidity congestion]
  (try
    (status/update-status {:temperature temperature
                           :humidity humidity
                           :congestion congestion})
    (response/response {:status :ok})
    (catch Exception e
      (timbre/error e)
      (response/response {:status :error, :message "Status update failed"}))))

(defroutes authorized-routes
  (POST "/status/update" {{:keys [temperature humidity congestion]} :params}
        (update-status temperature humidity congestion)))

(defn wrap-authorize [app]
  (fn [{{:keys [halake-api-key]} :headers :as req}]
    (if (= halake-api-key @#'halake-api-key)
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
