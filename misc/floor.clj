(ns holdfast.floor)

(defn vec2d [sx sy f]
    (mapv (fn[x](mapv (fn[y] (f x y)) (range sx))) (range sy)))

(defn makeMap
  [height width]
  (let [w (- width 1)
        h (- height 1)
        w2 (rand-int w)
        w3 (rand-int w)
        h1 (rand-int h)]
    (vec2d width height (fn [x y] (cond
                                    (and (= x w2)(= y h)) 0
                                    (and (= x w3)(= y h)) 0
                                    (and (= x 0)(= y h1)) 0
                                    (= x 0) 1
                                    (= x w) 1
                                    (= y 0) 1
                                    (= y h) 1
                                    :else 0)))
    ))



