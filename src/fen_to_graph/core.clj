(ns fen-to-graph.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [fen-to-graph.fen :refer [fen-to-list]]
            [fen-to-graph.moves :as moves])
  (:gen-class))

(def cli-options
  [["-f" "--fen" :required true :parse-fn #(str/trim %)]
   ["-h" "--help"]])

(defn process [fen]
  (dorun
   (map #(println (moves/legal-moves %))
        (remove nil? (fen-to-list fen)))))

(defn -main
  [& args]
  (let [{:keys [options args errors summary]} (parse-opts args cli-options)]
    (cond
      (or (:help options) (not (contains? options :fen))) (println summary)
      (:fen options) (process (:fen options)))))
