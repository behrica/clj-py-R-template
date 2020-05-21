# Libpython-clj + cljisr clj-template

This template is the easieest way to use R + python from Clojure.
It contains a Dockerfile which has all dependencies for cljisr + libpython-clj pluts a deps.end containing working versions of cljisr and libpython-clj

* https://github.com/seancorfield/clj-new


## Usage

### libpython-clj + cljisr projects can now be created quickly in 2 ways from the latest stable template:


-   **without** clj-new installed in user deps.edn

```bash 

# example
clj -Sdeps '{:deps {seancorfield/clj-new {:mvn/version "0.8.6"}}}' \
  -m clj-new.create \
  https://github.com/behrica/clj-template@4cfeb2b435282ace38c21a3efe2328594f7e34c4 \
  appcompany.funapp
```

-    **with** clj-new [installed](https://github.com/seancorfield/clj-new) in user deps.edn (recommended)

```bash 
# example
clj -A:new \
   https://github.com/behrica/clj-template/commit@4cfeb2b435282ace38c21a3efe2328594f7e34c4\
  appcompany.funapp
```

   **NOTE**: this assumes you have `clj-new` configured in you `~/.clojure/deps.edn`
   profile. If you do not, you can use the following:
   
### Creating docker image
The template creates Dockerfile in teh project folder.
The image can be build with

```
docker build . -t my-project
```

The Docker files assumes that the local project directory gets mounted into a folder in the container and that it becomes teh working directory. The docker image runs a nrepl on port 12345 which can be connected to by any other nRepl compatible client (including emacs+Cider)

A typical command line for running the nRepl in the docker container is then this:

```
docker run -ti \
  -v `pwd`:/code -w /code \
 -p 12345:12345 my-project
 ```
 ### Using clijsr + libpython clj
 
 Now Emacs (or any other nRepl client) can be connected to localhost:12345.
 In this repl, cljisr and libpython.clj work out of the box:
 
 ```
(require '[libpython-clj.require :refer [require-python]])
(require-python '[os :as os])
(os/getwd)

(require '[clojisr.v1.r :refer [r]])
(r "1+1")
         
 ```





For help please visit our [help-wanted](https://clojurians.zulipchat.com/#narrow/stream/215609-libpython-clj-dev/topic/help-wanted) topic.

For configuration option requests, please file a Github issue or visit our [feature requests]( https://clojurians.zulipchat.com/#narrow/stream/215609-libpython-clj-dev/topic/feature-requests) topic.  
