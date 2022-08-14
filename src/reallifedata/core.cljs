(ns reallifedata.core
    (:require
      [secretary.core :as secretary :include-macros true]
      [reagent.session :as session]
      [secretary.core :as secretary :include-macros true]
      [goog.events :as events]
      [goog.history.EventType :as EventType]
      [reallifedata.index :as index]
      [reallifedata.oss :as oss]
      [reallifedata.art :as art]
      [reallifedata.genart :as genart]
      [reallifedata.genarttest :as genarttest]
      [reallifedata.layout :refer [layout]]
      [reagent.core :as r])
    (:import goog.History))

;(session/put! :current-page #'index/view)

;; -------------------------
;; Main view function
(defn page []
  (layout [:div [(session/get :current-page)]])
  ;(layout [:div [#'index/view]])
  )

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'index/view))

(secretary/defroute "/oss" []
  (session/put! :current-page #'oss/view))

(secretary/defroute "/art" []
  (session/put! :current-page #'art/view))

(secretary/defroute "/genart" []
  (session/put! :current-page #'genart/view))

(secretary/defroute "/genarttest" []
  (session/put! :current-page #'genarttest/view))
;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (r/render [page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
