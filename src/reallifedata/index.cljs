(ns reallifedata.index
  (:require
    [goog.object :as g]
))

(defn view []
  ((g/get js/window "loadSrc") "index")
  [:div
     [:h2 "Peter Weissbrod"]
     [:p
      [:a {:href "#/oss"} "oss"]
      " | "
      [:a {:href "#/art"} "physical art"]
      " | "
      [:a {:href "#/genart"} "generative art"]
      ]
   ]
 )
