(ns fen-to-graph.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [fen-to-graph.fen :refer [fen-to-board]]
            [fen-to-graph.pieces :as pieces]
            [fen-to-graph.graph :as graph])
  (:gen-class))

(def cli-options
  [["-f" "--fen" :required true :parse-fn #(str/trim %)]
   ["-h" "--help"]])

(defn- piece-to-map [board piece]
  {:name (:name piece)
   :color (name (:color piece))
   :position (:position piece)
   :defends (pieces/legal-moves piece board)})

(defn process [fen]
  (let [board (fen-to-board fen)
        pieces-map (map (partial piece-to-map board) (remove nil? board))]
    (graph/show-graph (graph/create-graph pieces-map))))

(defn -main
  [& args]
  (let [{:keys [options args errors summary]} (parse-opts args cli-options)]
    (cond
      (or (:help options) (not (contains? options :fen))) (println summary)
      (:fen options) (process (:fen options)))))
