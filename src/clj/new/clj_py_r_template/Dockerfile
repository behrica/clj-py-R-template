FROM behrica/clj-py-r:1.5.1

#ENV JULIA_HOME=/home/user/julia-1.5.3


ARG USER_ID
ARG GROUP_ID

RUN addgroup --gid $GROUP_ID user
RUN adduser --disabled-password --gecos '' --uid $USER_ID --gid $GROUP_ID user

USER user
WORKDIR /home/user


# is this a hack ?
RUN cp /usr/local/bin/APserver /home/user


