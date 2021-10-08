FROM rocker/r-ver:4.1.1
RUN apt-get update && apt-get -y install openjdk-11-jdk curl rlwrap libssl-dev build-essential zlib1g-dev  libncurses5-dev libgdbm-dev libnss3-dev  libreadline-dev libffi-dev git automake libbz2-dev
RUN curl -O https://www.python.org/ftp/python/3.9.5/Python-3.9.5.tar.xz
RUN tar xf Python-3.9.5.tar.xz
RUN cd Python-3.9.5 && ./configure --enable-shared --with-ensurepip=install && make && make install && ldconfig
RUN curl -O https://download.clojure.org/install/linux-install-1.10.3.981.sh && chmod +x linux-install-1.10.3.981.sh && ./linux-install-1.10.3.981.sh
RUN Rscript -e 'install.packages("http://rforge.net/Rserve/snapshot/Rserve_1.8-7.tar.gz")'
RUN clj -P
#RUN clj -P ; exit 0
#RUN clj -P -Sthreads 1
RUN pip3 install -U numpy wheel scikit-learn
RUN git clone https://git.savannah.gnu.org/git/apl.git
RUN cd apl/trunk && ./configure && make develop_lib && make install

ARG USER_ID
ARG GROUP_ID

RUN addgroup --gid $GROUP_ID user
RUN adduser --disabled-password --gecos '' --uid $USER_ID --gid $GROUP_ID user


USER user
WORKDIR /home/user

RUN export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 && pip3 install python-javabridge
RUN echo "import cljbridge\ncljbridge.init_clojure_repl()" > /home/user/start_repl.py

RUN curl https://gist.githubusercontent.com/behrica/4ec7fca74fa7ecc2b4def9c675df616f/raw/77ec00f99ad11ba1d43c0637a57165e370615abb/cljbridge.py -o /home/user/cljbridge.py

RUN curl -O https://julialang-s3.julialang.org/bin/linux/x64/1.5/julia-1.5.3-linux-x86_64.tar.gz \
  && tar -xvzf julia-1.5.3-linux-x86_64.tar.gz

ENV JULIA_HOME=/home/user/julia-1.5.3

# is this a hack ?
RUN cp /usr/local/bin/APserver /home/user/


COPY deps.edn .
RUN clj -P

CMD ["python3", "start_repl.py"]
