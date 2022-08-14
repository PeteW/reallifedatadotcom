(ns reallifedata.genart
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [go <!]]
            [cljs.pprint :as pprint]
            [cljsjs.react-xmasonry :as xmasonry]
            [clojure.string :as str]
            [goog.object :as g]
            [reagent.core :as reagent :refer [atom]]
            [reagent-modals.modals :as reagent-modals]
            [reallifedata.genartprojects.p1 :as p1]))

(def XMasonry (reagent/adapt-react-class js/XMasonry))
(def XBlock (reagent/adapt-react-class js/XBlock))

; project atom loaded from json file
(def projects (atom []))

; load projects from json file, parse to clojure list and update atom
(go (let [response (<! (http/get "genartprojects.json"))]
    (reset! projects (js->clj (:items (:body response))))))

; given a project (string in the form of reallifedata.genartprojects.p1/create
; load a modal window containing a canvas and run the project within the canvas asynchronously
(defn run-project [project]
  (js/alert project)
  (let [fun (js/eval (-> project
        (str/replace #"/" ".")
        (str/replace #"-" "_")))]
  (reagent-modals/modal! [
  (fn []
    (js/alert fun)
    (js/setTimeout #(fun "sketch") 250)
    [:div {:id "sketch"}])])))

(defn view []
  ((g/get js/window "loadSrc") "genart")
  [:div [:h2 "generative art"]
   [:div.row]
   [:div.col-md-10
    [reagent-modals/modal-window]
    [XMasonry {:id "grid" :center true :responsive true}
     (for [project (shuffle @projects)]
        [XBlock {:key (:key project)}
         [:div.tile.panel.panel-default
          [:a {:on-click #(run-project (:key project))}
            [:div.tileheader.panel-heading [:img {:src (:picture project) :style {:max-width "100%" :width "auto" :height "auto" }}]]
            [:div {:style {:background-color "#0c0" :color "#222" :padding "0px 5px" }} [:h4 (:name project)]]
            [:div.tilebody.panel-body (:description project)]
           ]]])]]])

