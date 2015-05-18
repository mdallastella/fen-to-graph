(ns fen-to-graph.moves)

(defprotocol Piece
  "Protocol for all pieces"
  (legal-moves [this]))

(defrecord Pawn [color position]
  Piece
  (legal-moves [this]
    (str color " pawn is in " position)))

(defrecord Knight [color position]
  Piece
  (legal-moves [this]
    (str color " knight is in " position)))

(defrecord Bishop [color position]
  Piece
  (legal-moves [this]
    (str color " bishop is in " position)))

(defrecord Rook [color position]
  Piece
  (legal-moves [this]
    (str color " rook is in " position)))

(defrecord Queen [color position]
  Piece
  (legal-moves [this]
    (str color " queen is in " position)))

(defrecord King [color position]
  Piece
  (legal-moves [this]
    (str color " king is in " position)))
