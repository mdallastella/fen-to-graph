(ns fen-to-graph.fen
  (:require [clojure.string :as str]
            [fen-to-graph.pieces :as pieces]
            [fen-to-graph.board :as board]))

(defn char->color [char]
  "Return the color of a piece based on its case."
  (if (Character/isUpperCase char) :white :black))

(defn char->piece [char position]
  (let [color (char->color char)
        upper-char (Character/toUpperCase char)]
    (case upper-char
      \P (pieces/->Pawn "pawn" color position)
      \N (pieces/->Knight "knight" color position)
      \B (pieces/->Bishop "bishop" color position)
      \R (pieces/->Rook "rook" color position)
      \Q (pieces/->Queen "queen" color position)
      \K (pieces/->King "king" color position)
      \_ nil)))

(defn- number->underscore [row n]
  "Insert into row a n number of underscores."
  (vec (concat row (repeat (Integer/parseInt (str n)) \_))))

(defn- expand-row-empty-squares [row]
  "Takes a fen row definition and expands the empty squares indicated
  by a number. Es. p4p -> (p _ _ _ _ p)"
  (loop [pieces (seq row)
         new-row []]
    (if (empty? pieces)
      new-row
      (let [piece (first pieces)]
        (if (Character/isDigit piece)
          (recur (rest pieces) (number->underscore new-row piece))
          (recur (rest pieces) (conj new-row piece)))))))

(defn split-fen-string [fen]
  "Split the FEN string into a map, transforming the pieces field in a
  8x8 vector."
  (let [[pieces move-to castling passant halfmove fullmove]
        (str/split fen #" " 6)]
    {:pieces (apply concat (map
                            expand-row-empty-squares
                            (str/split pieces #"/")))
     :move-to move-to
     :castling castling
     :passant passant
     :halfmove halfmove
     :fullmove fullmove}))

(defn transform-into-pieces [list]
  (map char->piece list board/coord-list))

(defn fen-to-board [fen]
  (-> fen
      split-fen-string
      :pieces
      transform-into-pieces))
