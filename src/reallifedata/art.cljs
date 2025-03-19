(ns reallifedata.art
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [cljsjs.react-xmasonry :as xmasonry]
            [cljs-aws.base.config :as config]
            [cljs-aws.s3 :as s3]
            [cljs.pprint :as pprint]
            [goog.object :as g]
            [cljs.core.async :refer [go <!]]
            [cljs-http.client :as http]
            ))

(def projectfilter (atom "All"))
(def projects (atom []))
(def selected-project (atom nil))
(def show-modal? (atom false))
(def XMasonry (reagent/adapt-react-class js/XMasonry))
(def XBlock (reagent/adapt-react-class js/XBlock))

; load projects from json file, parse to clojure list and update atom
(go (let [response (<! (http/get "art.json"))]
  (reset! projects (js->clj (:items (:body response))))))

; tap an image show the modal
(defn show-project [project]
  (reset! selected-project project)
  (reset! show-modal? true)
)

(defn view []
  ((g/get js/window "loadSrc") "art")
  [:div  [:h2 "ART"]
  ; modal dialog
  (when @show-modal?
    [:div.modal-wrapper
     {:style {:position "fixed"
              :top 0 :left 0 :right 0 :bottom 0
              :background-color "rgba(0, 0, 0, 0.5)" ; Semi-transparent backdrop
              :z-index 1000
              :display "flex"
              :justify-content "center"
              :align-items "center"}
      :on-click #(reset! show-modal? false)} ; Close modal on backdrop click
     [:div.modal-content
      {
       :style {:padding-bottom 30 :padding-top 10 }
       } 
        [:img {:src (str "https://reallifedata.s3.us-east-1.amazonaws.com/img/" (:img @selected-project) ".jpg") :style { :max-width "90%" :max-height "100vh" :margin "auto" :object-fit "contain"}}]
        [:div
          [:p (:description @selected-project) ]
          [:p (:description2 @selected-project) ]]
      ]
     ])
    [:div.row
     ; left panel filter
     [:div.col-md-2
       ; date filter
      [:div.panel.panel-default
       [:div.panel-heading "Date"]
       [:div.panel-body
         (for [t (cons "All" (sort (distinct (map :date @projects))))]
           [:a {:key (str t) :href "#/art"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
       ; size filter
      [:div.panel.panel-default
       [:div.panel-heading "Size"]
       [:div.panel-body
         (for [t (cons "All" (sort (distinct (map :size @projects))))]
           [:a {:key (str t) :href "#/art"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
      ; tags filter
      [:div.panel.panel-default
       [:div.panel-heading "Tags"]
       [:div.panel-body
        (for [t (cons "All" (sort (distinct (flatten (map :tags @projects)))))]
           [:a {:key (str t):href "#/art"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
      ; season filter
      [:div.panel.panel-default
       [:div.panel-heading "Season"]
       [:div.panel-body
        (for [t (cons "All" (sort (distinct (flatten (map :season @projects)))))]
           [:a {:key (str t):href "#/art"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
     ]
    ; gallery window
    [:div.col-md-10
      [XMasonry {:id "grid" :center true :responsive true }
        (for [project (shuffle
          (filter
            #(or
             (= @projectfilter "All")
             (= @projectfilter (:date %))
             (= @projectfilter (:size %))
             (contains? (set (:season %)) @projectfilter)
             (contains? (set (:tags %)) @projectfilter)
             )
            @projects
            )
          )]
          [XBlock {:key (:key project)}
            [:div.tile.panel.panel-default 
               [:a {:href "#/art" :on-click #(show-project project)}
                [:img {:src (str "https://reallifedata.s3.us-east-1.amazonaws.com/img/thumbs/" (:img project) "_thumbnail.png") :style { :max-width "301px" :max-height "600px" :width "auto" :height "auto"}}]
                [:div {:style {:background-color "#0c0" :color "#222" :padding "0px 5px" }} [:h4 (:key project)]]]
            [:div.tilebody.panel-body (:description project)]
            (if (contains? project :season)
              [:div.season "Seasons: " (for [t (:season project)]
                [:a {:href "#/art"} [:span.badge {:on-click #(reset! projectfilter t)}(str t)]] )])
            (if (contains? project :size)
              [:div.sizes "Sizes: "(for [t (:size project)]
                [:a {:href "#/art"} [:span.badge {:on-click #(reset! projectfilter t)}(str t)]] )])
            (if (contains? project :tags)
              [:div.tags "Tags: "(for [t (:tags project)]
                [:a {:href "#/art"} [:span.badge {:on-click #(reset! projectfilter t)}(str t)]] )])
            (if (contains? project :date)
              [:div.tiledate "Date: "
                [:a {:href "#/art"} [:span.badge {:on-click #(reset! projectfilter (:date project))}(:date project)]]])
             ]])]
      ]]]
    )
