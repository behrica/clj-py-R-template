FROM behrica/clj-py-r:1.5.1

#ENV JULIA_HOME=/home/user/julia-1.5.3


ARG USER_ID
ARG GROUP_ID

RUN addgroup --gid $GROUP_ID user
RUN adduser --disabled-password --gecos '' --uid $USER_ID --gid $GROUP_ID user

RUN curl https://raw.githubusercontent.com/clj-python/libpython-clj/dcdcc77ba582a7a09d698d80c2a991cf3d8e151a/cljbridge.py -o /usr/local/lib/python3.10/cljbridge.py
USER user
WORKDIR /home/user


# is this a hack ?
RUN cp /usr/local/bin/APserver /home/user


CMD ["python3", "-c", "import cljbridge\ncljbridge.init_jvm(start_repl=True,port=12345,bind='0.0.0.0')"]
