(ns reallifedata.genartprojects.p2
  (:require [reagent.core :as reagent :refer [atom]]
            [quil.core :as q :include-macros true]
            [quil.middleware :as middleware]
            [cljs.pprint :refer [pprint]]
            ))

(def w 1024)
(def h 800)
(def clockradius 20)
(defn set-background [] (q/background 40 15 75))
(defn dist [clock endtime] (Math/abs (- endtime (:time clock))))
(defn clockspeed [clock endtime digit] (/ (dist clock endtime) (/ 30.0 (+ (* digit 5) 1))))

(def numbershapes 
  "specs on how to arrange a 4x6 set of clocks for each digit 0-9"
  [
   {
    :number "0"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 360 } { :row 2 :col 2 :endtime 375 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime 360 } { :row 3 :col 2 :endtime 360 } { :row 3 :col 3 :endtime 360 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime 360 } { :row 4 :col 2 :endtime 360 } { :row 4 :col 3 :endtime 360 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime 360 } { :row 5 :col 2 :endtime 180 } { :row 5 :col 3 :endtime 540 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime 180 } { :row 6 :col 2 :endtime 555 } { :row 6 :col 3 :endtime 555 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "1"
    :shape [
      { :row 1 :col 1 :endtime   0 } { :row 1 :col 2 :endtime 375 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime   0 } { :row 2 :col 2 :endtime 180 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime   0 } { :row 3 :col 2 :endtime   0 } { :row 3 :col 3 :endtime 360 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime   0 } { :row 4 :col 2 :endtime   0 } { :row 4 :col 3 :endtime 360 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime   0 } { :row 5 :col 2 :endtime   0 } { :row 5 :col 3 :endtime 360 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime   0 } { :row 6 :col 2 :endtime   0 } { :row 6 :col 3 :endtime 180 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "2"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 180 } { :row 2 :col 2 :endtime 555 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime 375 } { :row 3 :col 2 :endtime 555 } { :row 3 :col 3 :endtime 540 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime 360 } { :row 4 :col 2 :endtime 375 } { :row 4 :col 3 :endtime 555 } { :row 4 :col 4 :endtime 540 }
      { :row 5 :col 1 :endtime 360 } { :row 5 :col 2 :endtime 180 } { :row 5 :col 3 :endtime 555 } { :row 5 :col 4 :endtime 570 }
      { :row 6 :col 1 :endtime 180 } { :row 6 :col 2 :endtime 555 } { :row 6 :col 3 :endtime 555 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "3"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 180 } { :row 2 :col 2 :endtime 555 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime 375 } { :row 3 :col 2 :endtime 555 } { :row 3 :col 3 :endtime 540 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime 180 } { :row 4 :col 2 :endtime 555 } { :row 4 :col 3 :endtime 570 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime 375 } { :row 5 :col 2 :endtime 555 } { :row 5 :col 3 :endtime 540 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime 180 } { :row 6 :col 2 :endtime 555 } { :row 6 :col 3 :endtime 555 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "4"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 570 } { :row 1 :col 3 :endtime 375 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 360 } { :row 2 :col 2 :endtime 360 } { :row 2 :col 3 :endtime 360 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime 360 } { :row 3 :col 2 :endtime 180 } { :row 3 :col 3 :endtime 540 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime 180 } { :row 4 :col 2 :endtime 555 } { :row 4 :col 3 :endtime 570 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime   0 } { :row 5 :col 2 :endtime   0 } { :row 5 :col 3 :endtime 360 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime   0 } { :row 6 :col 2 :endtime   0 } { :row 6 :col 3 :endtime 180 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "5"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 360 } { :row 2 :col 2 :endtime 375 } { :row 2 :col 3 :endtime 555 } { :row 2 :col 4 :endtime 540 }
      { :row 3 :col 1 :endtime 360 } { :row 3 :col 2 :endtime 180 } { :row 3 :col 3 :endtime 555 } { :row 3 :col 4 :endtime 570 }
      { :row 4 :col 1 :endtime 180 } { :row 4 :col 2 :endtime 555 } { :row 4 :col 3 :endtime 570 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime 375 } { :row 5 :col 2 :endtime 555 } { :row 5 :col 3 :endtime 540 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime 180 } { :row 6 :col 2 :endtime 555 } { :row 6 :col 3 :endtime 555 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "6"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 360 } { :row 2 :col 2 :endtime 375 } { :row 2 :col 3 :endtime 555 } { :row 2 :col 4 :endtime 540 }
      { :row 3 :col 1 :endtime 360 } { :row 3 :col 2 :endtime 180 } { :row 3 :col 3 :endtime 555 } { :row 3 :col 4 :endtime 570 }
      { :row 4 :col 1 :endtime 360 } { :row 4 :col 2 :endtime 375 } { :row 4 :col 3 :endtime 570 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime 360 } { :row 5 :col 2 :endtime 180 } { :row 5 :col 3 :endtime 540 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime 180 } { :row 6 :col 2 :endtime 555 } { :row 6 :col 3 :endtime 555 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "7"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 180 } { :row 2 :col 2 :endtime 555 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime   0 } { :row 3 :col 2 :endtime   0 } { :row 3 :col 3 :endtime 360 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime   0 } { :row 4 :col 2 :endtime   0 } { :row 4 :col 3 :endtime 360 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime   0 } { :row 5 :col 2 :endtime   0 } { :row 5 :col 3 :endtime 360 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime   0 } { :row 6 :col 2 :endtime   0 } { :row 6 :col 3 :endtime 180 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "8"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 360 } { :row 2 :col 2 :endtime 375 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime 360 } { :row 3 :col 2 :endtime 180 } { :row 3 :col 3 :endtime 540 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime 360 } { :row 4 :col 2 :endtime 375 } { :row 4 :col 3 :endtime 570 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime 360 } { :row 5 :col 2 :endtime 180 } { :row 5 :col 3 :endtime 540 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime 180 } { :row 6 :col 2 :endtime 555 } { :row 6 :col 3 :endtime 555 } { :row 6 :col 4 :endtime 540 }
    ]
   }
   {
    :number "9"
    :shape [
      { :row 1 :col 1 :endtime 375 } { :row 1 :col 2 :endtime 555 } { :row 1 :col 3 :endtime 555 } { :row 1 :col 4 :endtime 570 }
      { :row 2 :col 1 :endtime 360 } { :row 2 :col 2 :endtime 375 } { :row 2 :col 3 :endtime 570 } { :row 2 :col 4 :endtime 360 }
      { :row 3 :col 1 :endtime 360 } { :row 3 :col 2 :endtime 180 } { :row 3 :col 3 :endtime 540 } { :row 3 :col 4 :endtime 360 }
      { :row 4 :col 1 :endtime 180 } { :row 4 :col 2 :endtime 555 } { :row 4 :col 3 :endtime 570 } { :row 4 :col 4 :endtime 360 }
      { :row 5 :col 1 :endtime   0 } { :row 5 :col 2 :endtime   0 } { :row 5 :col 3 :endtime 360 } { :row 5 :col 4 :endtime 360 }
      { :row 6 :col 1 :endtime   0 } { :row 6 :col 2 :endtime   0 } { :row 6 :col 3 :endtime 180 } { :row 6 :col 4 :endtime 540 }
    ]
   }
  ]
)

(defn currentnumbershapes []
  "given the current time return 6 number-shapes for HHMMSS"
  (let [
        now (.split (.substring (.toLocaleTimeString (new js/Date) "it-IT") 0 9) ":")
        hors (nth now 0)
        mins (nth now 1)
        secs (nth now 2)
        ]
  (list 
    (assoc (first (filter #(= (:number %) (nth hors 0)) numbershapes)) :id 0)
    (assoc (first (filter #(= (:number %) (nth hors 1)) numbershapes)) :id 1)
    (assoc (first (filter #(= (:number %) (nth mins 0)) numbershapes)) :id 2)
    (assoc (first (filter #(= (:number %) (nth mins 1)) numbershapes)) :id 3)
    (assoc (first (filter #(= (:number %) (nth secs 0)) numbershapes)) :id 4)
    (assoc (first (filter #(= (:number %) (nth secs 1)) numbershapes)) :id 5)
    )))

(defn currentclock [myclock clocks] (first (filter #(and (= (:row %) (:row myclock)) (= (:col %) (:col myclock))) clocks)))

(defn sketch-setup
  "returns the initial state to use for the update-render loop."
  []
  (set-background)
  (map 
    (fn [digit]
      {:id digit
       :col (mod digit 2)
       :row (Math/floor (/ digit 2))
       :clocks [
        { :row 1 :col 1 :time 0 } { :row 1 :col 2 :time 0 } { :row 1 :col 3 :time 0 }
        { :row 1 :col 4 :time 0 } { :row 2 :col 1 :time 0 } { :row 2 :col 2 :time 0 }
        { :row 2 :col 3 :time 0 } { :row 2 :col 4 :time 0 } { :row 3 :col 1 :time 0 }
        { :row 3 :col 2 :time 0 } { :row 3 :col 3 :time 0 } { :row 3 :col 4 :time 0 }
        { :row 4 :col 1 :time 0 } { :row 4 :col 2 :time 0 } { :row 4 :col 3 :time 0 }
        { :row 4 :col 4 :time 0 } { :row 5 :col 1 :time 0 } { :row 5 :col 2 :time 0 }
        { :row 5 :col 3 :time 0 } { :row 5 :col 4 :time 0 } { :row 6 :col 1 :time 0 }
        { :row 6 :col 2 :time 0 } { :row 6 :col 3 :time 0 } { :row 6 :col 4 :time 0 }
       ]}
    )
  (range 0 6)))

(defn sketch-update
  "given the current state return next state"
  [digits]
  ; (pprint (first(filter #(= (:id %) 1) (currentnumbershapes))))
  (map (fn [digit]
     (let [numbershape (first (filter #(= (:id %) (:id digit)) (currentnumbershapes)))]
       (assoc digit :clocks
          (map (fn [clock]
             (let [endtime (:endtime (currentclock clock (:shape numbershape))) ]
             (assoc clock
                :time (mod (+ (:time clock) (clockspeed clock endtime (:id digit))) 720)
                :dist (dist clock endtime)
              )))
             (:clocks digit)))))
     digits))

(defn sketch-draw
  "given the state draw it to canvas"
  [digits]
  (set-background)
  (q/stroke-weight 4)
  (doseq [digit digits]
    (doseq [clock (:clocks digit)]
      (let [
            ;dx1 (+ (* 4 2 clockradius (:col digit)) (* (Math/floor (/ (:col digit) 2)) clockradius))
            dx1 (* 4 2 clockradius (:col digit))
            dy1 (+ (* 6 2 clockradius (:row digit)) clockradius)
            x1 (+ dx1 (* 2 clockradius (:col clock)))
            y1 (+ dy1 (* 2 clockradius (:row clock)))
            ]
        (q/stroke 50 50 50)
        (q/fill 0 (- (:dist clock) 255) (- (:dist clock) 255))
        (q/ellipse x1 y1 (* clockradius 2) (* clockradius 2))
        ; (pprint (str (:time clock)))
        ; (q/text (str (:time clock)) x1 (+ clockradius clockradius y1))
        (q/stroke 255 255 255)
        (if (not= 0 (:time clock))
          (do
            (q/line x1 y1
              (+ x1 (*  1 (Math/sin (* (/ (:time clock) 60.0) 2 3.14159)) clockradius))
              (+ y1 (* -1 (Math/cos (* (/ (:time clock) 60.0) 2 3.14159)) clockradius)))
            (q/stroke 255 255 255)
            (q/line x1 y1
              (+ x1 (*  1 (Math/sin (* (/ (:time clock) 720.0) 2 3.14159)) clockradius))
              (+ y1 (* -1 (Math/cos (* (/ (:time clock) 720.0) 2 3.14159)) clockradius)))
              )
        )))))

(defn ^:export create
  "given the html element create the canvas and start the cycle"
  [canvas]
  (q/sketch
   :host canvas
   :size [w h]
   :draw #'sketch-draw
   :setup #'sketch-setup
   :update #'sketch-update
   :middleware [middleware/fun-mode]
   :settings (fn []
               (q/random-seed 600)
               (q/noise-seed 600))))

