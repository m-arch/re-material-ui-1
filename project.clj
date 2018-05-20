(defproject re-material-ui-1 "0.1.0-SNAPSHOT"
  :description "Components for using material-ui-1 with reagent"
  :url "https://github.com/m-arch/reagent-material-ui-1"
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [cljsjs/material-ui "1.0.0-beta.40-0"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [reagent "0.8.0"]
                 [org.clojure/clojurescript "1.10.238"
                  :scope "provided"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-npm "0.6.2"]]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src" "target/classes"]
  :clean-targets [:target-path "out" "release"]
  :target-path "target")
