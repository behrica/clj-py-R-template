(ns clj.new.clj-py-r-template
  (:require [clj.new.templates :refer [renderer project-name name-to-path ->files]]))

(def render (renderer "clj_py_r_template"))


(defn file-map->files [data file-map]
  (apply ->files data file-map))


(defn clj-py-r-template! [name & {force :force? dir :dir}]
  (let [data         {:name      (project-name name)
                      :base      (clojure.string/replace
                                  (project-name name)
                                  #"(.*?)[.](.*$)"
                                  "$1")
                      :suffix    (clojure.string/replace
                                  (project-name name)
                                  #"(.*?)[.](.*$)"
                                  "$2")
                      :sanitized (name-to-path name)}
        {base :base} data]

    (println (str  "Generating clj-py-r template for "
                   (:name data) " at " (:sanitized data) ".\n\n"))
                   


        

    (with-bindings {#'clj.new.templates/*force?* force}
                #'clj.new.templates/*dir*    dir
      (file-map->files
       data
       [["docker_build.sh" (render "docker_build.sh" data) :executable true]
        ["docker_repl.sh" (render "docker_repl.sh" data) :executable true]
        ["docker_run_xxx.sh" (render "docker_run_xxx.sh" data) :executable true]
        ["Dockerfile" (render "Dockerfile" data)]
        ["my-project.def" (render "my-project.def" data)]
        ["deps.edn" (render "deps.edn" data)]
        [".gitpod.Dockerfile" (render "gitpod.Dockerfile" data)]
        [".gitpod.yml" (render "gitpod.yml" data)]
        ["src/try_py_R.clj" (render "src/try_py_R.clj" data)]]))))




(defn clj-py-r-template
  ([name] (clj-py-r-template! name))
  ([name & args] (clj-py-r-template name)))

(comment
  (newline)
  (clj-py-r-template!
   "mydomain.myapp1"
   :dir "testdir"
   :force? true))
  
