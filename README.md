# Clojure polyglot  clj-template

# Quickstart - Get a working clojure repl supporting python, R and Julia in 5 lines

Only requirements is [clojure](https://clojure.org/guides/getting_started) and [docker](https://docs.docker.com/get-docker) installed.

1. Create Clojure polyglot project from template

```bash
clojure -Sdeps '{:deps {com.github.seancorfield/clj-new {:mvn/version "1.1.331"}}}' -M -m clj-new.create clj-py-r-template me/my-app
```

2.Build and run Docker image, which starts a headless repl on port 12345 in a docker container

This assumes a Linux OS and bash as shell. It might be slide different on other platforms or shell.
```bash
cd my-app
docker build -t my-app --build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g) .
docker run -it --rm -v $HOME/.m2:/home/user/.m2 -v "$(pwd):/workdir" -p  12345:12345 my-app
```

3. Connect normal repl to it
```bash
clj -Sdeps '{:deps {cider/cider-nrepl {:mvn/version "0.25.2"} }}' -m nrepl.cmdline  --middleware "[cider.nrepl/cider-middleware]" -c -p 12345
```

😀 Have fun with some interop code: 😀

```clojure
;; go from clj -> python -> clj -> R
(require '[libpython-clj2.require :refer [require-python]]
         '[libpython-clj2.python :as py]
         '[clojisr.v1.r :as r :refer [r require-r]])

(require-python '[numpy :as np])
(require-r '[base :as base-r])

(def r-matrix
 (-> (np/array [[1 2 3 4] [5 6 7 8] [9 10 11 12]])
     (py/->jvm)
     (r/clj->java->r)
     (base-r/simplify2array)
     (base-r/t)))

(println
 (base-r/dim r-matrix))
```



# Motivation
This template is the easiest way to use R, python and Julia from Clojure.


In the world of Java / Clojure Docker is not that common, because on the JVM platform using Docker instead of a JVM dependency manger (maven, lein, gradle ...) is not really required.

This situation changes, the moment we add R / python or Julia into our stack, because both might have operating system dependencies in their packages.

Then Docker can be very helpfull to get started quickly and work in a reproducible manner.

This template contains a Dockerfile which has Clojure and all dependencies for [ClojisR](https://github.com/scicloj/clojisr), [libpython-clj](https://github.com/clj-python/libpython-clj) and [julia-clj](https://github.com/cnuernber/libjulia-clj)
plus a deps.edn file containing working versions of ClojisR,libpython-clj and Julia-clj.




## Usage

Clojure projects including libpython-clj, ClojisR and Julia-clj can now be created quickly in 2 ways from the latest stable template:


-   **without** clj-new installed in user deps.edn

```bash 

# example
clj -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "1.0.199"}}}' \
  -m clj-new.create clj-py-r-template appcompany.funapp
```

-    **with** clj-new [installed](https://github.com/seancorfield/clj-new) in user deps.edn (recommended)

```bash 
# example
clj  -X:new :template clj-py-r-template  :name appcompany.funapp
```

   **NOTE**: this assumes you have `clj-new` configured in your `~/.clojure/deps.edn`
   profile. 

Specific versions of this template can be used by adding something like "-V 1.0.2" to the upper commands

### Creating docker image
The template creates a Dockerfile in the project folder.
The docker image can be build with

```
cd appcompany.funapp
docker build -t funapp --build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g) .

```

The Dockerfile assumes that the local project directory gets mounted into a folder in the container and that it becomes the working directory. The docker image runs a nRepl on port 12345 which can be connected to by any other nRepl compatible client (including emacs+Cider)

A typical command line for running the nRepl server in a docker container is then this:

```
docker run -it --rm -v "$(pwd):/code" -p 12345:12345 funapp
 ```
 ### Using ClojisR,libpython-clj and Jluli-clj in repl
 
 
 Now Emacs (or any other nRepl client) can be connected to localhost:12345.
 
 Example to use clj as nRepl client:
 ```
 clj -Sdeps '{:deps {cider/cider-nrepl {:mvn/version "0.25.2"} }}' -m nrepl.cmdline  --middleware "[cider.nrepl/cider-middleware]" -c -p 12345
 ```
 
 
 In this connected repl  cljisr, libpython-clj and julia-clj work out of the box:
 
 ```
(require '[libpython-clj2.require :refer [require-python]])
(require-python '[os :as os])
(os/getcwd)

(require '[clojisr.v1.r :refer [r]])
(r "1+1")
         
(require '[libjulia-clj.julia :as julia])
(julia/initialize!)
(def ones-fn (julia/jl "Base.ones"))
(ones-fn 3 4)

 ```

### Customizing the Docker image (typicall to add R / python /Julia libraries)

As in the Docker image one single R version and one single python version is installed,
libraries can be simply added by adding a few lines to the Dockerfile.

In case native dependencies are required, they can be added via "apt-get install"

The following would add a native library, a python library and a R package.

```
RUN apt-get install libssl-dev

RUN pip3 install pandas

RUN Rscript -e "install.packages('dplyr')"

```

### Changing Clojure dependencies
Clojure dependencies are currently not specified in the Dockerfile, but can be added as usual to the deps.edn file.

### Current versions

The versions of this template contains the following versions of dependencies in either Dockerfile or deps.end

#### 1.0.2

Docker base image: rocker/r-ver:4.0.0

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.0 |
|java |  openjdk 11|
| python| 3.8.2|
| RServe| latest from rforge.net|
|clj-python/libpython-clj| 1.45|
|scicloj/clojisr |1.0.0-BETA11|
|cider-nrepl | 0.25.2|

#### 1.0.3

Docker base image: rocker/r-ver:4.0.0

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.0 |
|java |  openjdk 11|
| python| 3.8.2|
| RServe| 1.8-7|
|clj-python/libpython-clj| 1.45|
|scicloj/clojisr |1.0.0-BETA11|
|cider-nrepl | 0.25.2|

#### 1.0.5

Docker base image: rocker/r-ver:4.0.2

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.2 |
|java |  openjdk 11|
| python| 3.8.5|
| RServe| 1.8-7|
|clj-python/libpython-clj| 1.45|
|scicloj/clojisr |1.0.0-BETA15|
|cider-nrepl | 0.25.2|


#### 1.1.0 

Docker base image: rocker/r-ver:4.0.3

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.3 |
|java |  openjdk 11|
|python| 3.8.6|
|RServe| 1.8-7|
|tablecloth  | 5.00-beta-5a |
|tech.ml.dataset | 5.00-beta-5 |
|clj-python/libpython-clj| 2.0.0-alpha-6|
|scicloj/clojisr |1.0.0-BETA16|
|notespace | 3-alpha2 |
|cider-nrepl | 0.25.5|

#### 1.1.1

Docker base image: rocker/r-ver:4.0.3

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.3 |
|java |  openjdk 11|
|python| 3.8.7|
|RServe| 1.8-7|
|tablecloth  | 5.00-beta-28|
|tech.ml.dataset | 5.00-beta-5 |
|tech.ml   |5.00-beta-14 | 
|clj-python/libpython-clj| 2.0.0-alpha-7|
|scicloj/clojisr |1.0.0-BETA16|
|notespace | 3-alpha2 |
|cider-nrepl | 0.25.8|

### 1.1.2

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.4 |
|java |  openjdk 11|
|python| 3.9.2|
|RServe| 1.8-7|
|tablecloth  | 5.05|
|tech.ml.dataset | 5.01 |
|tech.ml   |5.05 | 
|clj-python/libpython-clj| 2.0.0-beta-8|
|scicloj/clojisr |1.0.0-BETA18|
|notespace | 3-beta4 |
|cider-nrepl | 0.25.9|


### 1.2.0

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.4 |
|java |  openjdk 11|
|python| 3.9.2|
|RServe| 1.8-7|
|tablecloth  | 5.05|
|tech.ml.dataset | 5.01 |
|tech.ml   |5.05 | 
|clj-python/libpython-clj| 2.0.0-beta-12|
|scicloj/clojisr |1.0.0-BETA18|
|notespace | 3-beta4 |
|cider-nrepl | 0.25.9|

### 1.2.0

|dependency|version|
|----------|-------|
| clojure | 1.10.1|
|R         | 4.0.4 |
|java |  openjdk 11|
|python| 3.9.2|
|RServe| 1.8-7|
|tablecloth  | 5.05|
|tech.ml.dataset | 5.01 |
|tech.ml   |5.05 | 
|clj-python/libpython-clj| 2.0.0-beta-12|
|scicloj/clojisr |1.0.0-BETA18|
|notespace | 3-beta4 |
|cider-nrepl | 0.25.9|

### 1.3.0

|dependency|version|
|----------|-------|
| clojure | 1.10.3.967|
|R         | 4.1.1 |
|java |  openjdk 11|
|python| 3.9.5|
|RServe| 1.8-7|
|tablecloth  | 6.012|
|tech.ml.dataset | 6.012 |
|clj-python/libpython-clj| 2.0.0|
|scicloj.ml| 0.1.0-beta4|
|scicloj/clojisr |1.0.0-BETA19|
|notespace | 3-beta9 |
|cider-nrepl | 0.25.9|

### 1.4.0

|dependency|version|
|----------|-------|
| clojure | 1.10.3.981|
|R         | 4.1.1 |
|java |  openjdk 11|
|python| 3.9.5|
|RServe| 1.8-7|
|tablecloth  | 6.012|
|tech.ml.dataset | 6.012 |
|clj-python/libpython-clj| 2.0.0|
|julia-clj| 0.0.7|
|scicloj.ml| 0.1.0-beta4|
|scicloj/clojisr |1.0.0-BETA19|
|notespace | 3-beta9 |
|cider-nrepl | 0.25.9|

