(defproject reallifedata "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :description "clojurescript reallifedata site"
  :url "https://reallifedata.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [reagent "0.8.1"]
                 [cljs-http "0.1.46"]
                 [reagent-utils "0.3.2"]
                 [cljsjs/aws-sdk-js "2.94.0-0"]
                 [clj-commons/secretary  "1.2.4"]
                 [cljsjs/react-xmasonry "2.5.3-1"]
                 [cljs-aws "0.4.3"]
                 ]
  :externs ["lightbox/imagelightbox.min.js"]
  :compiler {:foreign-libs [
                            {:file (str "https://sdk.amazonaws.com/js/aws-sdk-2.341.0.js") :provides ["aws-sdk"]}
                            ]}

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.16"]]

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["public"]

  :figwheel {:http-server-root "."
             :nrepl-port 7002
             :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
             :css-dirs ["public/css"]}

  :cljsbuild {:builds {:app
                       {:source-paths ["src" "env/dev/cljs"]
                        :jar true
                        :compiler
                        {:main "reallifedata.dev"
                         :output-to "public/js/app.js"
                         :output-dir "public/js/out"
                         :asset-path   "js/out"
                         :source-map true
                         :optimizations :none
                         :pretty-print  true}
                        :figwheel
                        {:on-jsload "reallifedata.core/mount-root"
                         :open-urls ["http://localhost:3449/home.html"]}}
                       :release
                       {:source-paths ["src" "env/prod/cljs"]
                        ;:hooks [leiningen.cljsbuild]
                        :jar true
                        :compiler
                        {:output-to "public/js/app.js"
                         :output-dir "public/js/release"
                         :asset-path   "js/out"
                         :optimizations :advanced
                         :pretty-print false}}}}

  :aliases {"package" ["do" "clean" ["cljsbuild" "once" "release"]]}

  :profiles {:dev {:source-paths ["src" "env/dev/clj"]
                   :dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.16"]
                                  [nrepl "0.4.4"]
                                  [cider/piggieback "0.3.8"]]}})
