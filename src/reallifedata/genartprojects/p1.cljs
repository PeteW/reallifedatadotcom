(ns reallifedata.genartprojects.p1
  (:require [reagent.core :as reagent :refer [atom]]
            [quil.core :as q :include-macros true]
            [quil.middleware :as middleware]))

(def w 1024)
(def h 768)
(def noise-zoom 0.003)
(defn set-background [] (q/background 10 10 50))

(def palette1
  {:name       "ultramarine blue"
   :background [10 10 10]
   :colors     [
                [99 53 126]
                [99 53 126]
                [200 0 40]
                [132 20 10]
                [165 10 25]
                [165 0 0]
                ]})

(defn direction
  "calculates the next direction based on the previous position and id of each particle."
  [x y z]
  (* 2
     Math/PI
     (+ (q/noise (* x noise-zoom) (* y noise-zoom))
        (* 0.2 (q/noise (* x noise-zoom) (* y noise-zoom) (* z noise-zoom))))))

(defn velocity
  "given the current velocity and the delta return a new velocity"
  [current delta]
  (/ (+ current delta) 2))

(defn position
  "given current-position, speed and max val return new position"
  [current delta max]
  (mod (+ current delta) max))

(defn sketch-setup
  "returns the initial state to use for the update-render loop."
  []
  (set-background)
  (map 
    (fn [id]
      {:id         id
       :vx         1
       :vy         1
       :size       9
       :direction  0
       :x          (q/random w)
       :y          (q/random h)
       :color      (rand-nth (:colors palette1))})
      (range 0 2000)))

(defn sketch-update
  "given the current state return next state"
  [particles]
  (map (fn [p]
         (assoc p
                :x         (position (:x p) (:vx p) w)
                :y         (position (:y p) (:vy p) h)
                :direction (direction (:x p) (:y p) (:id p))
                :vx        (velocity (:vx p) (Math/cos (:direction p)))
                :vy        (velocity (:vy p) (Math/sin (:direction p)))))
       particles))

(defn sketch-draw
  "given the state draw it to canvas"
  [particles]
  (q/no-stroke)
  (doseq [p particles]
    (apply q/fill (conj (:color p) 9))
    (q/ellipse (:x p) (:y p) (:size p) (:size p))))

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

