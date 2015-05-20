(ns fen-to-graph.pieces
  (:require [fen-to-graph.board :as board]))

(defn- position-to-int [position]
  (map #(int %) (name position)))

(defn- int-to-position [file rank]
  (keyword (str (char file) (char rank))))

(defn up [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position file (inc rank))))

(defn down [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position file (dec rank))))

(defn left [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position (dec file) rank)))

(defn right [position]
  (let [[file rank] (position-to-int position)]
    (int-to-position (inc file) rank)))

(def up-left (comp up left))

(def up-right (comp up right))

(def down-left (comp down left))

(def down-right (comp down right))

(defn- pawn-initial? [color position]
  (cond
    (and (= color :white) (= (second (name position)) \2)) true
    (and (= color :black) (= (second (name position)) \7)) true
    :else false))

(defn- pawn-moves [color position]
  (if (= color :white)
    (let [moves [up-left up up-right]]
      (if (pawn-initial? color position)
        (conj moves (comp up up))
        moves))
    (let [moves [down-left down down-right]]
      (if (pawn-initial? color position)
        (conj moves (comp down down))
        moves))))

(defn- move-until-valid [f position board]
  (loop [next-pos (f position)
         moves []]
    (cond
      (not (board/valid-coord? next-pos)) moves
      (board/occupied? board next-pos) (conj moves next-pos)
      :else (recur (f next-pos)
                   (conj moves next-pos)))))

(defn- bishop-moves [position board]
  (loop [functions [up-left up-right down-left down-right]
         moves []]
    (if (empty? functions)
      (flatten moves)
      (recur (rest functions)
             (conj moves (move-until-valid (first functions)
                                           position
                                           board))))))
(defn- rook-moves [position board]
  (loop [functions [up right down left]
         moves []]
    (if (empty? functions)
      (flatten moves)
      (recur (rest functions)
             (conj moves (move-until-valid (first functions)
                                           position
                                           board))))))

(defn- queen-moves [position board]
  (concat
   (bishop-moves position board)
   (rook-moves position board)))

(defprotocol Piece
  "Protocol for all pieces"
  (legal-moves [this board]))

(defrecord Pawn [color position]
  Piece
  (legal-moves [this board]
    (let [functions (pawn-moves color position)]
      (filter board/valid-coord? (map #(apply % [position]) functions)))))

(defrecord Knight [color position]
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
      (filter board/valid-coord? (map #(apply % [position]) functions)))))

(defrecord Bishop [color position]
  Piece
  (legal-moves [this board]
    (bishop-moves position board)))

(defrecord Rook [color position]
  Piece
  (legal-moves [this board]
    (rook-moves position board)))

(defrecord Queen [color position]
  Piece
  (legal-moves [this board]
    (queen-moves position board)))

(defrecord King [color position]
  Piece
  (legal-moves [this board]
    (let [functions [up right down left]]
      (filter board/valid-coord? (map #(apply % [position]) functions)))))
