FROM rocker/r-ver:4.0.0
RUN apt-get update && apt-get -y install openjdk-14-jdk curl rlwrap libssl-dev libpython3.8
RUN curl -O https://download.clojure.org/install/linux-install-1.10.1.536.sh && chmod +x linux-install-1.10.1.536.sh && ./linux-install-1.10.1.536.sh && clojure -e "\"ok\""
# RUN curl -o /usr/local/bin/lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && chmod a+x /usr/local/bin/lein && lein version
RUN Rscript -e 'install.packages("Rserve",,"http://rforge.net")'
#CMD ["lein","repl",":headless",":port","12345",":host", "0.0.0.0"]
CMD ["clojure", "-A:cider-clj"]
