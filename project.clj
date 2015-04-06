(defproject halake-api "0.1.0-SNAPSHOT"
  :description "HaLake API server"
  :url "https://github.com/nyampass/halake-api"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.4.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "3.4.0"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.65"]
                 [environ "1.0.0"]
                 [im.chit/cronj "1.4.3"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring-middleware-format "0.5.0"]
                 [noir-exception "0.2.3"]
                 [bouncer "0.3.2"]
                 [prone "0.8.1"]
                 [com.novemberain/monger "2.0.1"]]
  :min-lein-version "2.0.0"
  :uberjar-name "halake-api.jar"
  :repl-options {:init-ns halake-api.handler
                 :timeout 120000}
  :jvm-opts ["-server"]
  :main halake-api.core
  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            ]
  :ring {:handler halake-api.handler/app
         :init    halake-api.handler/init
         :destroy halake-api.handler/destroy
         :uberwar-name "halake-api.war"}
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]
         :repl-options {:init-ns halake-api.repl}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
