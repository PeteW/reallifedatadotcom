(ns reallifedata.oss
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljsjs.react-xmasonry :as xmasonry]
            [cljs.repl :as repl]
            [cljs.pprint :as pprint]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]))

(def XMasonry (reagent/adapt-react-class js/XMasonry))
(def XBlock (reagent/adapt-react-class js/XBlock))

(def projectfilter (atom "All"))
(def projects (atom []))
(def src (atom ""))

(go (let [response (<! (http/get "projects.json"))]
  (reset! projects (js->clj (:items (:body response))))))

(defn view []
  [:div 
    [:p 
      [:strong "Disclosure: "]
      "This project list excludes material directly associated or exclusive property of clients and employers or older than 10 years."]
    [:div.row
     [:div.col-md-2
      [:div.panel.panel-default
       [:div.panel-heading "Date"]
       [:div.panel-body
         (for [t (cons "All" (sort (distinct (map :date @projects))))]
           [:a {:href "#/oss"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
      [:div.panel.panel-default
       [:div.panel-heading "Tech"]
       [:div.panel-body
        (for [t (cons "All" (sort (distinct (flatten (map :tech @projects)))))]
           [:a {:href "#/oss"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
     ]
    [:div.col-md-10
      [XMasonry {:id "grid" :center true :responsive true }
            (for [project (shuffle
                            (filter
                              #(or
                               (= @projectfilter "All")
                               (= @projectfilter (:date %))
                               (contains? (set (:tech %)) @projectfilter)
                               ) @projects))]
                                [XBlock {:key (:key project)}
                                  [:div.tile.panel.panel-default 
                                     [:a {:href (:project-url project) :target "_blank"}
                                       (if (contains? project :picture)
                                          [:div.tileheader.panel-heading [:img {:src (:picture project) :style {:max-width "100%" :width "auto" :height "auto" }}]])
                                      [:div {:style {:background-color "#0c0" :color "#222" :padding "0px 5px" }} [:h4 (:key project)]]]
                                  [:div.tilebody.panel-body (:description project)]
                                  (if (contains? project :description2)
                                    [:div.tilebody2.panel-body (:description2 project)])
                                  (if (contains? project :tech)
                                    [:div.tech (for [t (:tech project)]
                                      [:a {:href "#/oss"} [:span.badge {:on-click #(reset! projectfilter t)}(str t)]] )])
                                  (if (contains? project :date)
                                    [:div.tiledate "Date: "
                                      [:a {:href "#/oss"} [:span.badge {:on-click #(reset! projectfilter (:date project))}(:date project)]]])
                                   ]])]]]
    [:div.row
     [:div.col-md-12
      [:pre @src]]]
    ])
