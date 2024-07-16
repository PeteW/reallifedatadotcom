(ns reallifedata.genartprojects.p3
  (:require [reagent.core :as reagent :refer [atom]]
            [quil.core :as q :include-macros true]
            [quil.middleware :as middleware]
            [cljs.pprint :refer [pprint]]
            [goog.array :as garray]
            ))

(def img (new (.-Image js/window)))
(def hiddencanv (.createElement js/document "canvas"))
(def thecanvas (atom {}))
(.appendChild (.-body js/document) hiddencanv)

(defn canvaswidth [] (if hiddencanv (.-width hiddencanv) 0))
(defn canvasheight [] (if hiddencanv (.-width hiddencanv) 0))
(defn trim-points-offsides [points] (filter #(and (> (:x %) 0) (> (:y %) 0) (< (:x %) (canvaswidth)) (< (:y %) (canvasheight))) points))
(defn get-random-line-from-point [p] (let [m (rand 100)] {:m m :b (- (:y p) (* m (:x p)))}))
(defn get-random-point [] {:x (rand (canvaswidth)) :y (rand (canvasheight))})

(defn fetchImageAsBase64
  "Given a URL and a callback issue a GET call using the URL store the results in a base64 blob. Pass result to callback."
  [url callback]
  (let [xhr (new js/XMLHttpRequest)]
    (set!
      (.-onload xhr)
      (fn []
        (let [reader (new js/FileReader)]
          (set! (.-onloadend reader) (fn [] (callback (.-result reader))))
          (.readAsDataURL reader (.-response xhr)))))
    (.open xhr "GET" url)
    (set! (.-responseType xhr) "blob")
    (.send xhr)))

(defn get-intersections-with-canvas [l]
  [
   ; point where x = 0
   {:x 0 :y (:b l)}
   ; point where x = canvas width
   {:x (canvaswidth) :y (* (canvaswidth) (:b l))}
   ; point where y = 0
   {:x (/ (* -1 (:b l)) (:m l)) :y 0}
   ; point where y = height
   {:x (/ (- (canvasheight) (:b l)) (:m l)) :y (canvasheight)}
   ])

(defn get-intersections-between-lines [l1 l2]
  { :x (/ (- (:b l1) (:b l2)) (- (:m l1) (:m l2)))
    :y (/ (- (* (:b l1) (:m l2)) (* (:b l2) (:m l1))) (- (:m l1) (:m l2)))
   })

(defn get-intersections [l lines]
  (filter #(and (>= (:x %) 0) (>= (:y %) 0) (<= (:x %) (canvaswidth)) (<= (:y %) (canvasheight)))
  (cons 
    (get-intersections-with-canvas l)
    (map #(get-intersections-between-lines l %) lines)
    )))

(defn sketch-setup
  "returns the initial state to use for the update-render loop."
  []
  (do
    (q/background 40 15 75)
    (q/frame-rate 0.5)
    {
     :lines ([])
     }))

(defn sketch-update
  "given the current state return next state"
  [state]
  (let 
    [
     p (get-random-point)
     l (get-random-line-from-point p)
     intersections (get-intersections l (:lines state))
     ; todo: given all intersections find the 2 closest intersections this is your x1/y1/x2/y2
     ])
  state)

(defn sketch-draw
  "given the state draw it to canvas"
  [state]
  (q/stroke-weight 4)
  (q/stroke 255 255 0)
  (let [
        currentline (last (:lines state))
        pix (array-seq (js->clj (.-data (.getImageData (.getContext hiddencanv "2d") 1 1 1 1))))
        col {:r (nth pix 0) :g (nth pix 1) :b (nth pix 2) :a (nth pix 3)}
        ]
    ;(js/debugger)
    (q/line (:dx currentline) (:dy currentline) (:x currentline) (:y currentline))
    ;(.floodFillCanvasInDiv js/window "sketch" 0 0 (clj->js col)))
  ))

(defn ^:export create
  "given the html element create the canvas and start the cycle"
  [canvas]
  (reset! thecanvas canvas))

(.addEventListener img "load"
  (fn [e]
    (let [
         w (.-width img) 
         h (.-height img) 
          ]
    (set! (.-width hiddencanv) w)
    (set! (.-height hiddencanv) h)
    (js/alert (canvaswidth))
    (q/sketch
        :host @thecanvas
        :size [w h]
        :draw #'sketch-draw
        :setup #'sketch-setup
        :update #'sketch-update
        :middleware [middleware/fun-mode]
        :settings (fn [] (q/random-seed 600) (q/noise-seed 600)))
    (let [
          ctx (.getContext hiddencanv "2d")
          ]
      (.clearRect ctx 0 0 (canvaswidth) (canvasheight))
      (.drawImage ctx img 0 0)
      ;(set! (.-hidden hiddencanv) false)
      ))))

(fetchImageAsBase64
  "/img/model1.jpg"
  (fn [dataUrl]
    (.log js/console "fetchImageAsBase64 RESULT:" dataUrl)
    (.setAttribute img "src" dataUrl)))

