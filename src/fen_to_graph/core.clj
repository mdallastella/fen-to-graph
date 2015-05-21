(ns fen-to-graph.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [fen-to-graph.fen :refer [fen-to-list]]
            [fen-to-graph.pieces :as pieces])
  (:gen-class))

(def cli-options
  [["-f" "--fen" :required true :parse-fn #(str/trim %)]
   ["-h" "--help"]])

(defn- piece-to-map [piece board]
  {:name (:name piece)
   :color (:color piece)
   :occupy (:position piece)
   :defend (pieces/legal-moves piece board)})

(defn- create-graph [pieces-map]
  (pp/pprint pieces-map))

(defn process [fen]
  (let [board (fen-to-list fen)
        pieces-map (map #(piece-to-map % board) (remove nil? board))]
    (create-graph pieces-map)))

(defn -main
  [& args]
  (let [{:keys [options args errors summary]} (parse-opts args cli-options)]
    (cond
      (or (:help options) (not (contains? options :fen))) (println summary)
      (:fen options) (process (:fen options)))))
