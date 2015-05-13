# fen-to-graph

fen-to-graph is an attempt to write a small program in Clojure. It takes a FEN
position as argument and output a graph of some sort (haven't decided yet, maybe
GraphML).

Here's is an example of my idea. In the initial chessboard position, the white
knight on B1 and the pawn on D2 have the following relationships with the
squares:

![](https://github.com/mdallastella/fen-to-graph/example-graph.png)

It's just a toy I'm doing for better understand Clojure and graphs.

## Installation

Download from here.

## Usage

    $ java -jar fen-to-graph-0.1.0-standalone.jar --fen [fen position] --output
    [output file]

## License

Copyright © 2015 Marco Dalla Stella

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
