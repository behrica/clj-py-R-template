(defproject clj-python/clj-template "1.36.3-SNAPSHOT"
  :description "Template to quickly create clojure + R + python projects"
  :url "https://github.com/behrica/clj-py-R-template"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-tools-deps "0.4.5"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]})
