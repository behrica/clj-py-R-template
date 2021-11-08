[![Clojars Project](https://img.shields.io/clojars/v/clj-py-r-template/clj-template.svg)](https://clojars.org/clj-py-r-template/clj-template)
- latest snapshot: [![Gitpod ready-to-code latest-snapshot](https://img.shields.io/badge/Gitpod-ready--to--code-908a85?logo=gitpod)](https://gitpod.io/#https://github.com/behrica/clj-py-r-template)

# Clojure polyglot  clj-template

# Quickstart - Get a working clojure repl supporting python, R and Julia in 5 lines

Only requirements is [clojure](https://clojure.org/guides/getting_started) and [docker](https://docs.docker.com/get-docker) installed.

1. Create Clojure polyglot project from template

```bash
clojure -Sdeps '{:deps {com.github.seancorfield/clj-new {:mvn/version "1.2.362"}}}' -M -m clj-new.create clj-py-r-template me/my-app
```

2.Build and run Docker image, which starts a headless repl on port 12345
in a docker container

This assumes a Linux OS and bash as shell. It might be slightly different on
other platforms or shell.



```bash
cd my-app
docker build -t my-app --build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g) .
docker run -it --rm -v $HOME/.m2:/home/user/.m2 -v "$(pwd):/workdir" -p  12345:12345 my-app
```

3. In other shell: Connect normal repl to it
 
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

# For non Linux users:

The `docker run` commands above assume a Linux OS and bash as shell
The same is true for the Dockerfile produced by this template.
Line 15-19 of the Dockerfile 
https://github.com/behrica/clj-py-r-template/blob/2f1ec12690c09917c4c7608d6f625e8032d0d294/src/clj/new/clj_py_r_template/Dockerfile#L15
and the `docker run` id settings play together and this
works as-is only on Linux. 

Line 15-19 of the Dockerfile and the `-build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g)` 
are only  needed if volume is mounted as in the example.
Without these, any file written to the shared volume by the container
gets wrong permissions, which is inconvenient.

# Motivation
This template is the easiest way to use R, python and Julia from Clojure.


In the world of Java / Clojure usgae of containers is not that common, because on the
JVM plattform using Docker instead of a JVM 
dependency manger (maven, lein, gradle ...) is not really required.

This situation changes, the moment we add R / python or Julia into our stack,
because both might have operating system
dependencies in their packages.

Then containers can be very helpfull to get started quickly and work
in a reproducible manner.

This template contains a Dockerfile / singularity definition file which has Clojure and all dependencies for [ClojisR](https://github.com/scicloj/clojisr), [libpython-clj](https://github.com/clj-python/libpython-clj), [julia-clj](https://github.com/cnuernber/libjulia-clj) and [
libapl-clj](https://github.com/jjtolton/libapl-clj)
plus a deps.edn file containing working versions of ClojisR,libpython-clj and Julia-clj.




## Usage

Clojure projects including libpython-clj, ClojisR and Julia-clj can
now be created quickly in 2 ways from the latest stable template:


-   **without** clj-new installed in user deps.edn

```bash 

# example
clj -Sdeps '{:deps {com.github.seancorfield/clj-new {:mvn/version "1.2.362"}}}' \
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

The templates provided config files for three different wyas to run the container:
1. Docker  (typically on local machine)
2. Singularity (typically on local machine)
3. Gitpod (one way to run Docker cntainers in cloud)

### Run polyglot nrepl via Docker
The template creates a Dockerfile in the project folder.
The docker image can be build with

```
cd appcompany.funapp
docker build -t funapp --build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g) .

```

The Dockerfile assumes that the local project directory gets mounted into a 
folder in the container and that it becomes the working directory.
The docker image runs a nRepl on port 12345 which can be connected
to by any other nRepl compatible client (including emacs+Cider)

A typical command line for running the nRepl server in a docker
container is then this:

```
docker run -it --rm -v "$(pwd):/code" -p 12345:12345 funapp
 ```
 
 ### Run polyglot nrepl via Singularity
 
 This 2 lines 
 ```bash
 singularity build /tmp/my-app.sif my-project.def
 singularity run /tmp/my-app.sif
 ```

build first a Singularity image containing Clojure, python, R, Julia and APL.
The the image is run which starts a nrepl on port 12345.

To get this working the working directory  needs:

- to have a `deps.edn` with all needed Clojure deps (as created by this template)
- be writable by singularity


How to make this sure, is installation / project dependent and can be controlled by the options to `singularity run`

### Singularity vs Docker

Be aware that the 2 differ fundamentaly regaring their default settings of host / container isolation.
In "our use case" here the defaults of Singulriy are normaly fine, while we need to tell Docker to share
volumes and ports explicitely.


### Use Gitpod
The template creates the 2  gitpod configuration files. `.gitpod.yml` and
`.gitpod.Dockerfile`.
Launching a workspace pointing to a github repo with them,
configures Gitpod to use the Dockerfile in `.gitpod.Dockerfile`. 
So the Gitpod workspace will have Clojure, python, R, Julia and APL setup correctly and 
the Clojure polyglot libraries will work out-of-the-box.

The workspace launch will start the repl automatically an we can use VSCode in browser to connect to it.

Advanced:
Gitpod can be as well configured and used to expose the nrepl connection and ssh over the Internet.
This allows to connect from local machine to a Gitpod workspace (nrepl + ssh filesystem) with for example Emacs (cider + tramp)
This requires to use [gitpod local-companion](https://www.gitpod.io/blog/local-app)

 ### Using ClojisR,libpython-clj,julia-clj and libapl-clj in repl
 
 If a local repl was started as described before,
 Emacs (or any other nRepl client) can be connected to localhost:12345.
 
 Example to use clj as nRepl client:
 ```
 clj -Sdeps '{:deps {cider/cider-nrepl {:mvn/version "0.27.2"} }}' -m nrepl.cmdline  --middleware "[cider.nrepl/cider-middleware]" -c -p 12345
 ```
 
 
 In this connected repl  cljisr, libpython-clj , julia-clj and
 libapl-clj work out of the box:
 
 ```clojure
(require '[libpython-clj2.require :refer [require-python]])
(require-python '[os :as os])
(os/getcwd)

(require '[clojisr.v1.r :refer [r]])
(r "1+1")
         
(require '[libjulia-clj.julia :as julia])
(julia/initialize!)
(def ones-fn (julia/jl "Base.ones"))
(ones-fn 3 4)

(require '[libapl-clj.apl :as apl])
(apl/+ [1 2 3] [4 5 6])
 ```

### Customizing the container image (typically to add some libraries)

As in the container images one single R version and one single python 
version is installed,
libraries can be simply added by adding a few lines to the image configuratyion file (Dockerfile / my-project.def).

In case native dependencies are required, they can be added via "apt-get install"

The following would add a native library, a python library and a R package.

Example how to add to Dockefile
```
RUN apt-get install libssl-dev

RUN pip3 install pandas

RUN Rscript -e "install.packages('dplyr')"

```
Example to add to Singularity .def file:

```
%post
apt-get install libssl-dev
pip3 install pandas
Rscript -e "install.packages('dplyr')"
```



### Changing Clojure dependencies
Clojure dependencies are currently not specified in the image configuration file, but
can be added as usual to the deps.edn file.

### Current versions

The versions of this template contains the following versions of dependencies 
in either image configuration file or deps.end

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

### 1.5.0

Added scripts for Docker

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

### 1.5.1



|dependency|version|
|----------|-------|
|clojure | 1.10.3.981|
|R         | 4.1.1 |
|java |  openjdk 11|
|python| 3.10.0|
|julia | 1.5.3 |
|APL | latest |
|RServe| 1.8-7|
|tablecloth  | 6.023|
|tech.ml.dataset | 6.023 |
|clj-python/libpython-clj| 2.0.0|
|julia-clj| 0.0.7|
|scicloj.ml| 0.1.0|
|scicloj/clojisr |1.0.0-BETA19|
|notespace | 3-beta9 |
|cider-nrepl | 0.25.9|
|libapl-clj |0.1.2-ALPHA-SNAPSHOT|

### 1.5.2



|dependency|version|
|----------|-------|
|clojure | 1.10.3.981|
|R         | 4.1.1 |
|java |  openjdk 11|
|python| 3.10.0|
|julia | 1.5.3 |
|APL | latest |
|RServe| 1.8-7|
|tablecloth  | 6.025|
|tech.ml.dataset | 6.025 |
|clj-python/libpython-clj| 2.003|
|julia-clj| 0.0.7|
|scicloj.ml| 0.1.1|
|scicloj/clojisr |1.0.0-BETA19|
|notespace | 3-beta9 |
|cider-nrepl | 0.25.9|
|libapl-clj |0.1.2-ALPHA-SNAPSHOT|

