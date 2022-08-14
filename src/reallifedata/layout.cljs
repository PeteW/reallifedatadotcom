(ns reallifedata.layout
  (:require [reagent.core :as reagent :refer [atom]]
            ))

(defn layout [body]
  [:div
   [:nav {:class "navbar navbar-inverse navbar-fixed-right"}
    [:div {:class "container"}
     [:div {:class "navbar-header"}
      [:button {:type "button"
                :class "navbar-toggle collapsed"
                :data-toggle "collapse"
                :data-target "#navbar"
                :aria-expanded "false"
                :aria-controls "navbar"}
       [:span.sr-only "Toggle Navigation"]
       [:span.icon-bar]
       [:span.icon-bar]
       [:span.icon-bar]
       ]
      [:a {:class "navbar-brand", :href "#"} "Peter Weissbrod"]]
     [:div {:id "navbar" :class "navbar-collapse"}
      [:ul {:class "nav navbar-nav"}
       [:li>a {:href "#/oss"} "oss"]
       [:li>a {:href "#/art"} "phys-art"]
       [:li>a {:href "#/genart"} "gen-art"]
       ]]]]
   [:div {:class "container"}
    body
    [:div.col-md-12
      [:p "Source"]
      [:pre#github-iframe {:style {:width "100%"}}]
    ]
   ]
   ])
