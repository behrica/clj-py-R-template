(ns try-py-R)



(require '[libpython-clj2.require :refer [require-python]]
         '[libpython-clj2.python :as py])

;;;  python
(require-python '[os :as os])
(os/getcwd)

(require-python '[numpy :as np])

(def a (np/array [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12]]))
(println a)
(np/shape a)


;; R
(require '[clojisr.v1.r :as r :refer [r require-r]])
(r "1+1")

(require-r '[base :as base-r])

;;;  numpy -> clj -> R
(def r-matrix
 (-> (np/array [[1 2 3 4] [5 6 7 8] [9 10 11 12]])
     (py/->jvm)
     (r/clj->java->r)
     (base-r/simplify2array)
     (base-r/t)))

(println
 (base-r/dim r-matrix))


(require '[libapl-clj.apl :as apl])

(apl/+ [1 2 3] [4 5 6])

(apl/display! (apl/+ [1 2 3] [4 5 6]))
