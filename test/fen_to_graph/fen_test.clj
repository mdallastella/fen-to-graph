(ns fen-to-graph.fen-test
  (:require [midje.sweet :refer :all]
            [fen-to-graph.fen :as fen]
            [fen-to-graph.pieces :as pieces]))

(def fen "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
(def board '(\r \n \b \q \k \b \n \r \p \p \_ \p \p \p \p \p \_ \_ \_ \_ \_ \_ \_ \_ \_ \_ \p \_ \_ \_ \_ \_ \_ \_ \_ \_ \P \_ \_ \_ \_ \_ \_ \_ \_ \N \_ \_ \P \P \P \P \_ \P \P \P \R \N \B \Q \K \B \_ \R))

(fact "Transforming a FEN string into a board"
      (flatten (fen/pieces-to-board (:pieces (fen/split-fen-string fen)))) => board)

(facts "Creating pieces"
       (fact (fen/char->piece \p :e7) => (pieces/->Pawn :black :e7))
       (fact (fen/char->piece \N :c1) => (pieces/->Knight :white :c1))
       (fact (fen/char->piece \P :e2) => (pieces/->Pawn :white :e2))
       (fact (fen/char->piece \K :e1) => (pieces/->King :white :e1))
       (fact (fen/char->piece \q :d8) => (pieces/->Queen :black :d8)))
