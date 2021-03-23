FROM rocker/r-ver:4.0.4
RUN apt-get update && apt-get -y install openjdk-11-jdk curl rlwrap libssl-dev build-essential zlib1g-dev  libncurses5-dev libgdbm-dev libnss3-dev  libreadline-dev libffi-dev
RUN curl -O https://www.python.org/ftp/python/3.9.2/Python-3.9.2.tar.xz
RUN tar xf Python-3.9.2.tar.xz
RUN cd Python-3.9.2 && ./configure --enable-shared --with-ensurepip=install && make && make install && ldconfig
RUN curl -O https://download.clojure.org/install/linux-install-1.10.2.796.sh && chmod +x linux-install-1.10.2.796.sh && ./linux-install-1.10.2.796.sh && clojure -e "\"ok\""
RUN Rscript -e 'install.packages("http://rforge.net/Rserve/snapshot/Rserve_1.8-7.tar.gz")'
RUN curl https://gist.githubusercontent.com/behrica/4ec7fca74fa7ecc2b4def9c675df616f/raw/77ec00f99ad11ba1d43c0637a57165e370615abb/cljbridge.py -o cljbridge.py
COPY deps.edn .
RUN clj -e '"ok"'
RUN pip3 install numpy wheel
RUN export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 && pip3 install python-javabridge
RUN echo "import cljbridge\ncljbridge.init_clojure_repl()" > start_repl.py
RUN pip3 install -U scikit-learn
CMD ["python3", "start_repl.py"]
