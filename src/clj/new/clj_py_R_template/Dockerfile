FROM rocker/r-ver:4.0.2
RUN apt-get update && apt-get -y install openjdk-11-jdk curl rlwrap libssl-dev libpython3.8 python3-pip
RUN curl -O https://download.clojure.org/install/linux-install-1.10.1.536.sh && chmod +x linux-install-1.10.1.536.sh && ./linux-install-1.10.1.536.sh && clojure -e "\"ok\""
RUN Rscript -e 'install.packages("Rserve",,"http://rforge.net")'
CMD ["clojure", "-A:cider-clj"]
