(defproject fen-to-graph "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [aysylu/loom "0.5.0"]]
  :plugins [[lein-environ "1.0.0"]
            [codox "0.8.12"]]
  :main ^:skip-aot fen-to-graph.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
