FROM rocker/r-ver:4.0.2
RUN apt-get update && apt-get -y install openjdk-11-jdk curl rlwrap libssl-dev build-essential zlib1g-dev  libncurses5-dev libgdbm-dev libnss3-dev  libreadline-dev libffi-dev
RUN curl -O https://www.python.org/ftp/python/3.8.5/Python-3.8.5.tar.xz
RUN tar xf Python-3.8.5.tar.xz
RUN cd Python-3.8.5 && ./configure --enable-shared && make && make install
RUN curl -O https://download.clojure.org/install/linux-install-1.10.1.561.sh && chmod +x linux-install-1.10.1.561.sh && ./linux-install-1.10.1.561.sh && clojure -e "\"ok\""
RUN Rscript -e 'install.packages("http://rforge.net/Rserve/snapshot/Rserve_1.8-7.tar.gz")'
COPY deps.edn .
RUN clj -e '"ok"'
CMD ["clojure", "-A:cider-clj"]
