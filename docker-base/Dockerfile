FROM rocker/r-ver:4.1.2
RUN apt-get update && apt-get -y install --reinstall ca-certificates && update-ca-certificates
RUN apt-get update && apt-get -y install openjdk-11-jdk curl rlwrap libssl-dev build-essential zlib1g-dev  libncurses5-dev libgdbm-dev libnss3-dev  libreadline-dev libffi-dev libbz2-dev \
    automake-1.15 git liblzma-dev
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install intel-mkl

# python
RUN curl -O https://www.python.org/ftp/python/3.10.0/Python-3.10.0.tar.xz
RUN tar xf Python-3.10.0.tar.xz
RUN cd Python-3.10.0 && ./configure --enable-shared --with-ensurepip=install && make && make install && ldconfig
RUN curl -O https://download.clojure.org/install/linux-install-1.10.3.986.sh && chmod +x linux-install-1.10.3.986.sh && ./linux-install-1.10.3.986.sh
RUN Rscript -e 'install.packages("http://rforge.net/Rserve/snapshot/Rserve_1.8-7.tar.gz")'
RUN clj -P
RUN pip3 install -U numpy wheel scikit-learn cython

#apl
RUN git clone https://git.savannah.gnu.org/git/apl.git
RUN cd apl/trunk && ./configure && make develop_lib && make install

RUN curl -O https://julialang-s3.julialang.org/bin/linux/x64/1.7/julia-1.7.2-linux-x86_64.tar.gz \
  && tar -xvzf julia-1.7.2-linux-x86_64.tar.gz
RUN mv julia-1.7.2/ /opt/
RUN ln -s /opt/julia-1.7.2/bin/julia /usr/local/bin/julia


RUN export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 && pip3 install cljbridge==0.0.7
COPY deps.edn .
RUN clj -P  -Srepro -Sdeps '{:mvn/local-repo "/tmp/.m2/repository"}'

EXPOSE 12345
ENV JULIA_HOME=/opt/julia-1.7.2

RUN apt-get install -y wget
# CUDA
RUN wget https://developer.download.nvidia.com/compute/cuda/repos/ubuntu2004/x86_64/cuda-ubuntu2004.pin
RUN mv cuda-ubuntu2004.pin /etc/apt/preferences.d/cuda-repository-pin-600
RUN wget https://developer.download.nvidia.com/compute/cuda/11.4.1/local_installers/cuda-repo-ubuntu2004-11-4-local_11.4.1-470.57.02-1_amd64.deb
RUN dpkg -i cuda-repo-ubuntu2004-11-4-local_11.4.1-470.57.02-1_amd64.deb
RUN apt-key add /var/cuda-repo-ubuntu2004-11-4-local/7fa2af80.pub
RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install cuda

# opencl
RUN apt-get install -y vim nvidia-opencl-dev leiningen
RUN apt-get install -y software-properties-common intel-opencl-icd

ENV OS=ubuntu2004

#cudnn
RUN wget https://developer.download.nvidia.com/compute/cuda/repos/${OS}/x86_64/cuda-${OS}.pin
RUN mv cuda-${OS}.pin /etc/apt/preferences.d/cuda-repository-pin-600
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys A4B469963BF863CC
RUN add-apt-repository "deb https://developer.download.nvidia.com/compute/cuda/repos/${OS}/x86_64/ /"
RUN apt-get update

ENV cudnn_version=8.2.4.15
ENV cuda_version=cuda11.4

RUN apt-get install libcudnn8=${cudnn_version}-1+${cuda_version}
RUN apt-get install libcudnn8-dev=${cudnn_version}-1+${cuda_version}

# clone deep diamnond
WORKDIR /tmp
RUN git clone https://github.com/uncomplicate/deep-diamond.git
WORKDIR /tmp/deep-diamond

#having this pass is the goal 
#RUN cd deep-diamond && rm -rf test/uncomplicate/diamond/functional && lein test
CMD ["python3", "-c", "from clojurebridge import cljbridge;cljbridge.init_jvm(start_repl=True,port=12345,bind='0.0.0.0',mvn_local_repo='/tmp/.m2/repository')"]

#clojure -Sdeps '{:deps {nrepl/nrepl {:mvn/version "0.8.3"} cider/cider-nrepl {:mvn/version "0.25.5"}}}' -Sdeps '{:mvn/local-repo "/tmp/.m2/repository"}' '-Spath'
