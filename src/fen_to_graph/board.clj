(ns fen-to-graph.board)

(def file-key (int \a))
(def rank-key (int \0))
(def max-file-key (int \h))
(def max-rank-key (int \8))

(defn- file-component
  "Takes a Character representing to a file and return its component value.
  Ex: \\a -> 0, \\b -> 1 and so on."
  [file]
  (- (int file) file-key))

(defn- rank-component
  "Takes a Character representing a rank and return its component value.
  Ex: \\8 -> 0, \\7 -> 1 and so on."
  [rank]
  (->> rank-key
       (- (int rank))
       (- 8)
       (* 8)))

(defn- index
  "Takes a file and a rank Characters and return their index in coord-list.
  Ex: [\\a \\8] -> 0, [\\h \\1] -> 63."
  [file rank]
  (+ (file-component file) (rank-component rank)))

(def file-list
  '("a" "b" "c" "d" "e" "f" "g" "h"))

(defonce coord-list
  ;; The coordinates list from :a8 to :h1
  (for [rank (range 8 0 -1)
        file file-list]
    (keyword (str file rank))))

(defn valid-coord?
  "Predicate that returns true if the input coordinates are valid."
  [coord]
  (let [[file rank] (rest (str coord))
        f (int file)
        r (int rank)]
    (and (>= f file-key)
         (<= f max-file-key)
         (> r rank-key)
         (<= r max-rank-key))))

(defn coord->index
  "Takes a keyword representing a coordinate and returns its index in coord-list.
  Ex: :a8 -> 0, :h1 -> 63"
  [coord]
  (let [[file rank] (name coord)]
    (index file rank)))

(defn index->coord
  "Takes an index and returns a keyword representing the coordinate on the board.
  Ex: 0 -> :a1, 63 -> :h1"
  [index]
  (nth coord-list index))

(defn occupied-by
  "Takes a board and a coordinate and returns what's on the board."
  [board coord]
  (nth board (coord->index coord)))

(defn occupied?
  "Takes a board and a coordinate and returns if the square is occupied or not."
  [board coord]
  (not= \_ (occupied-by board coord)))
