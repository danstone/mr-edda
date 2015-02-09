(defproject mixradio/mr-edda "0.1.0-SNAPSHOT"
  :description "For querying edda"
  :url "http://github.com/mixradio/mr-edda"
  :license "https://github.com/mixradio/mr-edda/blob/master/LICENSE"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.3.1"]
                 [clj-http "1.0.1"]
                 [camel-snake-kebab "0.3.0" :exclusions [org.clojure/clojure]]]

  :plugins [[lein-release "1.0.5"]])
