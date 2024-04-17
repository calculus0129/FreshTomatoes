#!/usr/bin/env bash

docker build -t ms1:tag .
docker run --name mongo-container -it ms1:tag
docker stop mongo-container
docker rm mongo-container
# docker exec -it mongo-container bash