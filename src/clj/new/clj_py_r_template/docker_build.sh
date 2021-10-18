#!/usr/bin/env bash

docker build -t {{name}} --build-arg USER_ID=$(id -u) --build-arg GROUP_ID=$(id -g) .
