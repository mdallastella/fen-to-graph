(ns fen-to-graph.pieces
  (:require [fen-to-graph.board :as board]))

(defn- position-to-int
  "Takes a position, split it in characters and return a list of
  int of it. Es. :e2 -> (101 50)."
  [position]
  (map int (name position)))

(defn- int-to-position
  "Takes two ints that represent the file and the rank characters of a
  position and return a keyword of it. Es. 101,50 -> :e2"
  [file rank]
  (keyword (str (char file) (char rank))))

(defn up
  "Move the given position up. Es. :e2 -> :e3"
  [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position file (inc rank))))

(defn down
  "Move the given position down. Es. :e2 -> :e1"
  [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position file (dec rank))))

(defn left
  "Move the given position to left. Es. :e2 -> :d2"
  [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position (dec file) rank)))

(defn right
  "Move the given position to the right. Es. :e2 -> :f2"
  [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position (inc file) rank)))

(def up-left (comp up left))

(def up-right (comp up right))

(def down-left (comp down left))

(def down-right (comp down right))

(defn- pawn-initial? [color position]
  "Check if a pawn is in its initial position."
  (let [initial-position {:white \2 :black \7}]
    (= (second (name position)) (initial-position color))))

(defn- pawn-moves
  "Returns a list of functions that calculate the possible moves of a
  pawn."
  [color position]
  (if (= color :white)
    (let [moves [up-left up up-right]]
      (if (pawn-initial? color position)
        (conj moves (comp up up))
        moves))
    (let [moves [down-left down down-right]]
      (if (pawn-initial? color position)
        (conj moves (comp down down))
        moves))))

(defn- move-until-valid
  "Return a list of valid positions given a direction. It stops when a
  position is out of the board or occupied by another piece."
  [direction position board]
  (loop [next-pos (direction position)
         moves []]
    (cond
      (not (board/valid-coord? next-pos)) moves
      (board/occupied? board next-pos) (conj moves next-pos)
      :else (recur (direction next-pos)
                   (conj moves next-pos)))))

(defn- bishop-moves
  "Returns all the possible positions of a bishop."
  [position board]
  (loop [functions [up-left up-right down-left down-right]
         moves []]
    (if (empty? functions)
      (flatten moves)
      (recur (rest functions)
             (conj moves (move-until-valid (first functions)
                                           position
                                           board))))))
(defn- rook-moves
  "Returns all the possible positions of a rook."
  [position board]
  (loop [functions [up right down left]
         moves []]
    (if (empty? functions)
      (flatten moves)
      (recur (rest functions)
             (conj moves (move-until-valid (first functions)
                                           position
                                           board))))))

(defn- queen-moves
  "Returns all the possible positions of a queen."
  [position board]
  (concat
   (bishop-moves position board)
   (rook-moves position board)))

(defprotocol Piece
  "Common protocol for all type of pieces."
  (legal-moves [this board]))

(defrecord Pawn [name color position]
  Piece
  (legal-moves [this board]
    (let [functions (pawn-moves color position)]
      (filter board/valid-coord? ((apply juxt functions) position)))))

(defrecord Knight [name color position]
  Piece
  (legal-moves [this board]
    (let [functions [(comp up-left left)
                     (comp up-left up)
                     (comp up-right up)
                     (comp up-right right)
                     (comp down-right right)
                     (comp down-right down)
                     (comp down-left down)
                     (comp down-left left)]]
      (filter board/valid-coord? ((apply juxt functions) position)))))

(defrecord Bishop [name color position]
  Piece
  (legal-moves [this board]
    (bishop-moves position board)))

(defrecord Rook [name color position]
  Piece
  (legal-moves [this board]
    (rook-moves position board)))

(defrecord Queen [name color position]
  Piece
  (legal-moves [this board]
    (queen-moves position board)))

(defrecord King [name color position]
  Piece
  (legal-moves [this board]
    (let [functions [up up-right right down-right down down-left left up-left]]
      (filter board/valid-coord? ((apply juxt functions) position)))))
