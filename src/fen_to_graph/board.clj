(ns fen-to-graph.board)

(def file-key (int \a))
(def rank-key (int \0))
(def max-file-key (int \h))
(def max-rank-key (int \8))

(defn- file-component [file]
  (- (int file) file-key))

(defn- rank-component [rank]
  (->> rank-key
       (- (int rank))
       (- 8)
       (* 8)))

(defn- index [file rank]
  (+ (file-component file) (rank-component rank)))

(def coord-list
  (let [take-repeatedly #(take 8 (repeat %))
        c (take-repeatedly [\a \b \c \d \e \f \g \h])
        n (map take-repeatedly (range 8 0 -1))]
    (map #(keyword (str %1 %2))
         (flatten c) (flatten n))))

(defn valid-coord? [coord]
  (let [[file rank] (rest (str coord))
        f (int file)
        r (int rank)]
    (and (>= f file-key)
         (<= f max-file-key)
         (> r rank-key)
         (<= r max-rank-key))))

(defn coord->index [coord]
  (let [[file rank] (name coord)]
    (index file rank)))

(defn index->coord [index]
  (nth coord-list index))

(defn occupied-by [board coord]
  (nth board (coord->index coord)))

(defn occupied? [board coord]
  (not= \_ (occupied-by board coord)))
