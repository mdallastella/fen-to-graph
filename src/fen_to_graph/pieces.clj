(ns fen-to-graph.pieces
  (:require [fen-to-graph.board :as board]))

(defn- position-to-int [position]
  "Takes a position, split it in characters and return a list of
  int of it. Es. :e2 -> (101 50)."
  (map int (name position)))

(defn- int-to-position [file rank]
  "Takes two ints that represent the file and the rank characters of a
  position and return a keyword of it. Es. 101,50 -> :e2"
  (keyword (str (char file) (char rank))))

(defn up [position]
  "Move the given position up. Es. :e2 -> :e3"
  (let [[file rank] (position-to-int position)]
    (int-to-position file (inc rank))))

(defn down [position]
  "Move the given position down. Es. :e2 -> :e1"
  (let [[file rank] (position-to-int position)]
    (int-to-position file (dec rank))))

(defn left [position]
  "Move the given position left. Es. :e2 -> :d2"
  (let [[file rank] (position-to-int position)]
    (int-to-position (dec file) rank)))

(defn right [position]
  "Move the given position right. Es. :e2 -> :f2"
  (let [[file rank] (position-to-int position)]
    (int-to-position (inc file) rank)))

(def up-left (comp up left))

(def up-right (comp up right))

(def down-left (comp down left))

(def down-right (comp down right))

(defn- pawn-initial? [color position]
  "Check if a pawn is in its initial position."
  (cond
    (and (= color :white) (= (second (name position)) \2)) true
    (and (= color :black) (= (second (name position)) \7)) true
    :else false))

(defn- pawn-moves [color position]
  "Returns a list of functions that calculate the possible moves of a
  pawn."
  (if (= color :white)
    (let [moves [up-left up up-right]]
      (if (pawn-initial? color position)
        (conj moves (comp up up))
        moves))
    (let [moves [down-left down down-right]]
      (if (pawn-initial? color position)
        (conj moves (comp down down))
        moves))))

(defn- move-until-valid [direction position board]
  "Return a list of valid positions given a direction. It stops when a
  position is out of the board or occupied by another piece."
  (loop [next-pos (direction position)
         moves []]
    (cond
      (not (board/valid-coord? next-pos)) moves
      (board/occupied? board next-pos) (conj moves next-pos)
      :else (recur (direction next-pos)
                   (conj moves next-pos)))))

(defn- bishop-moves [position board]
  "Returns all the possible positions of a bishop."
  (loop [functions [up-left up-right down-left down-right]
         moves []]
    (if (empty? functions)
      (flatten moves)
      (recur (rest functions)
             (conj moves (move-until-valid (first functions)
                                           position
                                           board))))))
(defn- rook-moves [position board]
  "Returns all the possible positions of a rook."
  (loop [functions [up right down left]
         moves []]
    (if (empty? functions)
      (flatten moves)
      (recur (rest functions)
             (conj moves (move-until-valid (first functions)
                                           position
                                           board))))))

(defn- queen-moves [position board]
  "Returns all the possible positions of a queen."
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
