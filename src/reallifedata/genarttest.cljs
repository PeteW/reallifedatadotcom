(ns reallifedata.genarttest
  (:require 
            [clojure.string :as str]
            [reallifedata.genartprojects.p2 :as proj]))

(defn view []
  [:div
   [:div.row
    [:div.col-md-10
      [:div {:id "sketch" :style {:height "800px" :width "800px"}}
       (js/setTimeout #(proj/create "sketch") 250)]
     ]
    ]
   ]
  )
