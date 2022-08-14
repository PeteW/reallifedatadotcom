(ns reallifedata.genarttest
  (:require 
            [clojure.string :as str]
            [reallifedata.genartprojects.p1 :as p1]))

(defn view []
  [:div
   [:div.row
    [:div.col-md-10
      [:div {:id "sketch" :style {:height "800px" :width "800px"}}
       (js/setTimeout #(p1/create "sketch") 250)]
     ]
    ]
   ]
  )
