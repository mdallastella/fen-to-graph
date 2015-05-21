(ns fen-to-graph.pieces-test
  (:require [midje.sweet :refer :all]
            [fen-to-graph.pieces :as pieces]))

(def empty-board (into [] (take 64 (repeat 64 \_))))

(facts "Testing simple movements"
       (fact (pieces/up :a1) => :a2)
       (fact (pieces/down :a8) => :a7)
       (fact (pieces/left :h1) => :g1)
       (fact (pieces/right :a1) => :b1))

(facts "Testing pawn moves"
       (let [initial-white-pawn (pieces/->Pawn :white :e2)
             initial-black-pawn (pieces/->Pawn :black :e7)
             white-pawn (pieces/->Pawn :white :e4)
             black-pawn (pieces/->Pawn :black :e5)]
         (fact (pieces/legal-moves initial-white-pawn empty-board)
               =>
               (just [:e3 :e4 :d3 :f3] :in-any-order))
         (fact (pieces/legal-moves initial-black-pawn empty-board)
               =>
               (just [:e6 :e5 :d6 :f6] :in-any-order))
         (fact (pieces/legal-moves white-pawn empty-board)
               =>
               (just [:e5 :d5 :f5] :in-any-order))
         (fact (pieces/legal-moves black-pawn empty-board)
               =>
               (just [:e4 :d4 :f4] :in-any-order))))

(facts "Testing knight moves"
       (let [knight (pieces/->Knight :white :e4)
             knight-on-corner (pieces/->Knight :white :a1)]
         (fact (pieces/legal-moves knight empty-board)
               =>
               (just [:c3 :c5 :d2
                      :d6 :f6 :f2
                      :g5 :g3] :in-any-order))
         (fact (pieces/legal-moves knight-on-corner empty-board)
               =>
               (just [:b3 :c2] :in-any-order))))

(facts "Testing bishop moves"
       (let [bishop (pieces/->Bishop :white :e4)]
         (fact (pieces/legal-moves bishop empty-board)
               =>
               (just [:b1 :c2 :d3
                      :d5 :c6 :b7
                      :a8 :f5 :g6
                      :h7 :f3 :g2
                      :h1] :in-any-order))))

(facts "Testing rook moves"
       (let [rook (pieces/->Rook :white :e4)]
         (fact (pieces/legal-moves rook empty-board)
               =>
               (just [:e3 :e2 :e1
                      :e5 :e6 :e7
                      :e8 :a4 :b4
                      :c4 :d4 :f4
                      :g4 :h4] :in-any-order))))


(facts "Testing queen moves"
       (let [queen (pieces/->Queen :white :e4)]
         (fact (pieces/legal-moves queen empty-board)
               =>
               (just [:b1 :c2 :d3 :d5
                      :c6 :b7 :a8 :f5
                      :g6 :h7 :f3 :g2
                      :h1 :e3 :e2 :e1
                      :e5 :e6 :e7 :e8
                      :a4 :b4 :c4 :d4
                      :f4 :g4 :h4] :in-any-order))))

(facts "Testing king moves"
       (let [king (pieces/->King :white :e4)]
         (fact (pieces/legal-moves king empty-board)
               =>
               (just [:e5 :d5 :d4 :d3 :e3 :f3 :f4 :f5] :in-any-order))))
