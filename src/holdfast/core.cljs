(ns holdfast.core
    (:require [reagent.core :as reagent :refer [atom]]
      [holdfast.tiles :as tiles]
      [holdfast.actor :as actor]))

(enable-console-print!)

(println "This text is printed from src/holdfast/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload


(def layout-data [[1 0 0 0 0 0 1 1 1 1 1 1 1 1 1 1 1 ]
                  [1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 ]
                  [0 0 0 0 0 0 1 1 1 0 1 0 1 1 1 1 1 ]
                  [1 1 1 1 1 1 0 0 0 0 0 0 0 0 1 1 1 ]
                  [1 1 1 1 0 0 0 0 0 0 0 1 1 1 1 1 1 ]                  
                  [0 0 0 0 0 0 1 1 1 0 1 0 1 1 1 1 1 ]
                  [1 1 1 1 1 1 0 0 0 0 0 0 0 0 1 1 1 ]
                  [1 1 1 1 0 0 0 0 0 0 0 1 1 1 1 1 1 ]])

(defonce app-state (atom (actor/actor "Ravian" 13 :npc 390199199 1 :pilgrim {:x 0 :y 2})))


(defn hello-world []
  [:div
   [:h1 (:name @app-state) ]
   [:h2 (:x (:position @app-state)) "," (:y (:position @app-state))]
   [:h3 "Edit this and watch it change! too 52"]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
