(ns reallifedata.art
  (:require [reagent.core :as reagent :refer [atom]]
            [cljsjs.react-xmasonry :as xmasonry]
            [cljs-aws.base.config :as config]
            [cljs-aws.s3 :as s3]
            [cljs.pprint :as pprint]
            [cljs.core.async :refer [go <!]]
            [reallifedata.utils :refer [tile]]
            [reallifedata.appdb :refer [appdb]]))

(def XMasonry (reagent/adapt-react-class js/XMasonry))
(def XBlock (reagent/adapt-react-class js/XBlock))
(def bucketurl "https://s3.amazonaws.com/reallifedata/")

; use aws cognito to assume an identity for listing/loading objects from s3
(config/set-region! "us-east-1")
(config/load-credentials! :cognito-identity-credentials {:identity-pool-id "us-east-1:44c60791-0fff-410a-b1f6-c290de194fb4"})

; store the s3 objects in a mutable atom
(def s3objects (atom []))

; asynchronously load objects from s3 and update the atom with the response
(go (let [response (<! (s3/list-objects-v2 {:bucket "reallifedata"}))]
        (reset! s3objects (shuffle (:contents response)))))

(defn view []
  [:div [:h2 "art page"]
   [:div>section {:data-featherlight-gallery "" :data-featherlight-filter "a"}
    [XMasonry {:id "grid" :center true :responsive true }
          (for [image @s3objects]
            [XBlock {:key (:key image)}
             [:div.tile
              [:div.tilebody
               [:a {:href (str bucketurl (:key image))}
                 [:img {:src (str bucketurl (:key image))
                        :style { :max-width "300px" :max-height "600px" :width "auto" :height "auto"}}]]]
              ]])]]
  ])
