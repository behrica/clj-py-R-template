(ns clj.new.clj-py-r-template
  (:require [clj.new.templates :refer [renderer project-name name-to-path ->files]]))

(def render (renderer "clj_py_r_template"))

(defn file-map->files [data file-map]
  (apply ->files data (seq file-map)))

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
                   (:name data) " at " (:sanitized data) ".\n\n"
                   ))

    (with-bindings {#'clj.new.templates/*force?* force
                    #'clj.new.templates/*dir*    dir}
      (file-map->files
       data
       {"Dockerfile"                                           (render "Dockerfile" data)
         "deps.edn"                                            (render "deps.edn" data)
        (format  "src/%s/%s.clj" (:base data) (:suffix data)) (render "core.clj" data)
        (format  "src/%s/python.clj" (:base data))            (render "python.clj" data)}))))


(defn clj-py-r-template
  ([name] (clj-py-r-template! name))
  ([name & args] (clj-py-r-template name)))

(comment
  (newline)
  (clj-py-r-template!
   "mydomain.myapp"
   :dir "testdir"
   :force? true)
  )
