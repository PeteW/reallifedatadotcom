(ns reallifedata.utils
    (:require
      [secretary.core :as secretary :include-macros true]
      [reagent.session :as session]
      [secretary.core :as secretary :include-macros true]
      [goog.events :as events]
      [goog.history.EventType :as EventType]
      [reagent.core :as r])
    (:import goog.History))

(def XBlock (r/adapt-react-class js/XBlock))

;; -------------------------
;; Tile creation function
(defn tile [header1 header2 image footer]
  [XBlock [:div.tile
           [:div.tileheader1 header1]
           [:div.tileheader2 header2]
           [:div.tilebody
            [:img {:src image
                     :style {:display "block"
                             :max-width "400px"
                             :max-height "400px"
                             :width "auto"
                             :height "auto"
                             }}]]
           [:div.footer footer]
           ]])

