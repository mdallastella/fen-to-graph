(ns fen-to-graph.fen
  (:require [clojure.string :as str]
            [fen-to-graph.pieces :as pieces]
            [fen-to-graph.board :as board]))

(defn char->color
  "Return the color of a piece based on its case."
  [char]
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

(defn- char->int
  "Transforms a Character into an Integer"
  [char]
  (Integer/parseInt (str char)))

(defn- number->underscore
  "Insert into row a n number of underscores."
  [row n]
  (vec (concat row (repeat (char->int n) \_))))

(defn- expand-row-empty-squares
  "Takes a fen row definition and expands the empty squares indicated
  by a number. Es. p4p -> (p _ _ _ _ p)"
  [row]
  (loop [pieces (seq row)
         new-row []]
    (if (empty? pieces)
      new-row
      (let [piece (first pieces)]
        (if (Character/isDigit piece)
          (recur (rest pieces) (number->underscore new-row piece))
          (recur (rest pieces) (conj new-row piece)))))))

(defn split-fen-string
  "Split the FEN string into a map, transforming the pieces field in a
  8x8 vector."
  [fen]
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

(defn transform-into-pieces
  "Takes a list of characters and transforms it in a list of pieces"
  [list]
  (map char->piece list board/coord-list))

(defn fen-to-board
  "The main function that transforms a fen string into a list of Piece objects"
  [fen]
  (-> fen
      split-fen-string
      :pieces
      transform-into-pieces))
