# Libpython-clj + ClojisR  clj-template

This template is the easiest way to use R + python from Clojure.

In the world of Java / Clojure Docker is not that common, because on the JVM platform using Docker instead of a JVM dependency manger (mave, lein, gradle ...) is not really required.

This situation changes, the moment we add R / python into our stack.
Then Docker can be very helpfull to get started quickly and work in a reproducible manner.

This template contains a Dockerfile which has Clojure and all dependencies for ClojisR + libpython-clj plus a deps.edn file containing working versions of ClojisR and libpython-clj




## Usage

Clojure projects including libpython-clj + ClojisR can now be created quickly in 2 ways from the latest stable template:


-   **without** clj-new installed in user deps.edn

```bash 

# example
clj -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "1.0.199"}}}' \
  -m clj-new.create clj-py-r-template appcompany.funapp
```

-    **with** clj-new [installed](https://github.com/seancorfield/clj-new) in user deps.edn (recommended)

```bash 
# example
clj -A:new clj-py-r-template appcompany.funapp
```

   **NOTE**: this assumes you have `clj-new` configured in you `~/.clojure/deps.edn`
   profile. 

Specific versions of this template can be used by adding something like "-V 1.0.2" to the upper commands

### Creating docker image
The template creates a Dockerfile in the project folder.
The docker image can be build with

```
cd appcompany.funapp
docker build . -t funapp
```

The Dockerfile assumes that the local project directory gets mounted into a folder in the container and that it becomes the working directory. The docker image runs a nRepl on port 12345 which can be connected to by any other nRepl compatible client (including emacs+Cider)

A typical command line for running the nRepl in the docker container is then this:

```
docker run -ti \
  -v `pwd`:/code -w /code \
 -p 12345:12345 funapp
 ```
 ### Using ClojisR + libpython-clj in repl
 
 
 Now Emacs (or any other nRepl client) can be connected to localhost:12345.
 
 Example to use clj as nRepl client:
 ```
 clj -Sdeps '{:deps {cider/cider-nrepl {:mvn/version "0.25.2"} }}' -m nrepl.cmdline  --middleware "[cider.nrepl/cider-middleware]" -c -p 12345
 ```
 
 
 In this connected repl , cljisr and libpython.clj work out of the box:
 
 ```
(require '[libpython-clj.require :refer [require-python]])
(require-python '[os :as os])
(os/getcwd)

(require '[clojisr.v1.r :refer [r]])
(r "1+1")
         
 ```

### Customizing the Docker image (typicall to add R / python libraries)

As in the Docker image one single R version and a single python version is installed,
libraries can be simply added by adding lines to the Dockerfile, like:
In case native dependencies are required, they can be added via "apt-get install"

The follwoing would add a native library, a python libary and a R package.


```
RUN apt-get install libssl-dev

RUN pip3 install pandas

RUN Rscript -e "install.packages('dplyr')"

```

Clojure dependencies are currently specified in the Dockerfile, but can be added to the deps.edn file.

### Current versions

The versions of this template (Dockerfile + deps.edn) contains the following versions:

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
