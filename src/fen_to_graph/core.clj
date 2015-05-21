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

(defn process [fen]
  (let [board (fen-to-list fen)]
    (dorun
     (map #(println (pieces/legal-moves % board))
          (remove nil? board)))))

(defn -main
  [& args]
  (let [{:keys [options args errors summary]} (parse-opts args cli-options)]
    (cond
      (or (:help options) (not (contains? options :fen))) (println summary)
      (:fen options) (process (:fen options)))))
