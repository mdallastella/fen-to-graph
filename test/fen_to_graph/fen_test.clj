(ns fen-to-graph.fen-test
  (:require [clojure.test :refer :all]
            [fen-to-graph.fen :as fen]
            [fen-to-graph.moves :as moves]))

(def fen "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
(def board '(\r \n \b \q \k \b \n \r \p \p \_ \p \p \p \p \p \_ \_ \_ \_ \_ \_ \_ \_ \_ \_ \p \_ \_ \_ \_ \_ \_ \_ \_ \_ \P \_ \_ \_ \_ \_ \_ \_ \_ \N \_ \_ \P \P \P \P \_ \P \P \P \R \N \B \Q \K \B \_ \R))

(deftest fen-to-list-test
  (testing "Transforming a FEN string into a board"
    (is (= (fen/fen-to-list fen) board))))

(deftest char->piece-test
  (testing "Creating pieces"
    (is (= (fen/char->piece \p :e7)
           (moves/->Pawn :black :e7)))
    (is (= (fen/char->piece \N :c1)
           (moves/->Knight :white :c1)))
    (is (= (fen/char->piece \P :e2)
           (moves/->Pawn :white :e2)))
    (is (= (fen/char->piece \K :e1)
           (moves/->King :white :e1)))
    (is (= (fen/char->piece \q :d8)
           (moves/->Queen :black :d8)))))
