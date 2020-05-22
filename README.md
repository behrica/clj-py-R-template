# Libpython-clj + ClojisR  clj-template

This template is the easiest way to use R + python from Clojure.

In the world of Java / Clojure Docker is not that common, because on the JVM platform and using a dependency manger it is not really required.

This situation changes, the moment we add R / python into our stack.
Then Docker can be very helpfull to get started quickly and work in a reproducible manner.

This template contains a Dockerfile which has all dependencies for ClojisR + libpython-clj plus a deps.edn file containing working versions of ClojisR and libpython-clj




## Usage

### libpython-clj + cljisr projects can now be created quickly in 2 ways from the latest stable template:


-   **without** clj-new installed in user deps.edn

```bash 

# example
clj -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "0.8.6"}}}' \
  -m clj-new.create \
  https://github.com/behrica/clj-template@12dfe54329f21807517808f141ddc48ce87f044d \
  appcompany.funapp
```

-    **with** clj-new [installed](https://github.com/seancorfield/clj-new) in user deps.edn (recommended)

```bash 
# example
clj -A:new \
   https://github.com/behrica/clj-template@12dfe54329f21807517808f141ddc48ce87f044d \
  appcompany.funapp
```

   **NOTE**: this assumes you have `clj-new` configured in you `~/.clojure/deps.edn`
   profile. 
   
### Creating docker image
The template creates a Dockerfile in the project folder.
The docker image can be build with

```
cd appcompany.funapp
docker build . -t my-project
```

The Docker files assumes that the local project directory gets mounted into a folder in the container and that it becomes the working directory. The docker image runs a nRepl on port 12345 which can be connected to by any other nRepl compatible client (including emacs+Cider)

A typical command line for running the nRepl in the docker container is then this:

```
docker run -ti \
  -v `pwd`:/code -w /code \
 -p 12345:12345 my-project
 ```
 ### Using ClojisR + libpython-clj in repl
 
 
 Now Emacs (or any other nRepl client) can be connected to localhost:12345.
 In this repl, cljisr and libpython.clj work out of the box:
 
 ```
(require '[libpython-clj.require :refer [require-python]])
(require-python '[os :as os])
(os/getcwd)

(require '[clojisr.v1.r :refer [r]])
(r "1+1")
         
 ```

### Customizing the Docker image (typicall to add R / python libraries)

As in the Docker image one concrete R version and a single python version is installed,
libraries can be simply added by adding lines to the Dockerfile, like:
In case native dependencies are required, they can be added via apt-get install xxx
```
RUN apt-get install libssl-dev

RUN pip3 install pandas

RUN Rscript -e "install.packages('dplyr')"

```

### Current versions

The current version of this template (Dockerfile + deps.edn) contains the following versions:
* R: 4.0.0
* RServe: latest from rforge.net
* python: 3.8.2
* java:  openjdk 14.0.1
* clojure: 1.10.1
* clj-python/libpython-clj 1.44
* scicloj/clojisr 1.0.0-BETA11






For help please visit our [help-wanted](https://clojurians.zulipchat.com/#narrow/stream/215609-libpython-clj-dev/topic/help-wanted) topic.

For configuration option requests, please file a Github issue or visit our [feature requests]( https://clojurians.zulipchat.com/#narrow/stream/215609-libpython-clj-dev/topic/feature-requests) topic.  
