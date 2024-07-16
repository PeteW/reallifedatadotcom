(ns reallifedata.index
  (:require
    [goog.object :as g]
))

(defn view []
  ((g/get js/window "loadSrc") "index")
  [:div
     [:h2 "Peter Weissbrod"]
     [:h4 "Contact"]
     [:ul
      [:li
       [:a {:href "mailto:talktopete@gmail.com"} "email"]
       ]
      [:li
       [:a {:href "https://reallifedata-site.s3.amazonaws.com/pweissbrod_Resume.docx"} "resume"]
       ]
      ]
     [:p
      [:a {:href "#/oss"} "oss"]
      " | "
      [:a {:href "#/art"} "physical art"]
      " | "
      [:a {:href "#/genart"} "generative art"]
      ]
   ]
 )
