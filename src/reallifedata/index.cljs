(ns reallifedata.update
  (:require [reagent.core :as reagent :refer [atom]]
            [reallifedata.appdb :refer [appdb]]))

(defn input-data []
  [:input {:type "text"
           :value @appdb
           :on-change #(reset! appdb (-> % .-target .-value))}])

(defn home-page []
  [:div>h2 "Peter Weissbrod"]
   )
