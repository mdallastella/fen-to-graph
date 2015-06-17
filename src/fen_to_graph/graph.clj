(ns fen-to-graph.graph
  (:require [loom.graph :as g]
            [loom.attr :as attr]
            [loom.io :refer [view]]))

(defn- populate-graph-from-piece [graph piece]
  (let [name (str (:color piece) "-" (:name piece))
        position (:position piece)]
    (loop [new-graph (g/add-edges graph [name position])
           defends (:defends piece)]
      (if (empty? defends)
        new-graph
        (recur (g/add-edges new-graph [name (first defends)])
               (rest defends))))))

(defn create-graph [board]
  (loop [graph (g/graph)
         pieces board]
    (if (empty? pieces)
      graph
      (recur (populate-graph-from-piece graph (first pieces))
             (rest pieces)))))

(defn show-graph
  [graph]
  (view graph))
