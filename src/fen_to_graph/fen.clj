(ns fen-to-graph.fen
  (:require [clojure.string :as str]
            [fen-to-graph.moves :as moves]))

(defn char->color [char]
  (if (Character/isUpperCase char) :white :black))

(defn char->piece [char position]
  (let [color (char->color char)
        upper-char (Character/toUpperCase char)]
    (case upper-char
      \P (moves/->Pawn color position)
      \N (moves/->Knight color position)
      \B (moves/->Bishop color position)
      \R (moves/->Rook color position)
      \Q (moves/->Queen color position)
      \K (moves/->King color position)
      \_ nil)))

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
  (into [] (concat row (repeat (Integer/parseInt (str n)) \_))))

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
  (map expand-row-empty-squares pieces))

(defn fen-to-board [fen]
  (-> fen
      split-fen-string
      :pieces
      pieces-to-board))
