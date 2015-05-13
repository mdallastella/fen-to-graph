(ns fen-to-graph.fen
  (:require [clojure.string :as str]))

(defn char->piece [char]
  (case (str/upper-case char)
    "R" :rook
    "N" :knight
    "B" :bishop
    "Q" :queen
    "K" :king
    "P" :pawn
    "_" :empty))

(defn char->color [char]
  (if (Character/isUpperCase char) :white :black))

(defn split-fen-string [fen]
  (let [[pieces move-to castling passant halfmove fullmove]
        (str/split fen #" " 6)]
    {:pieces (str/split pieces #"/")
     :move-to move-to
     :castling castling
     :passant passant
     :halfmove halfmove
     :fullmove fullmove}))

(defn- number->underscore [row n]
  (into [] (concat row (repeat (Integer/parseInt n) \_))))

(defn- expand-row-empty-squares [row]
  (loop [pieces (seq row)
         new-row []]
    (if (empty? pieces)
      new-row
      (let [piece (first pieces)]
        (if (Character/isDigit piece)
          (recur (rest pieces) (number->underscore new-row piece))
          (recur (rest pieces) (conj new-row piece)))))))

(defn pieces-to-board [pieces]
  (map #(expand-row-empty-squares %) pieces))
