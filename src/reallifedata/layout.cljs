(ns reallifedata.layout
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reallifedata.appdb :refer [appdb]]))

(defn layout [body]
  [:div {:class "container"}
   [:nav {:class "navbar navbar-inverse navbar-fixed-right"}
    [:div {:class "container"}
     [:div {:class "navbar-header"}
      [:a {:class "navbar-brand", :href "#"} "Peter Weissbrod"]]
     [:div {:id "navbar", :class "navbar-collapse collapse"}
      [:ul {:class "nav navbar-nav"}
       [:li>a {:href "#/oss"} "oss"]
       [:li>a {:href "#/art"} "art"]
       ]]]]
    body
   ])
