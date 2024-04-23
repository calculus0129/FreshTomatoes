#!/usr/bin/env bash

cd milestone1
mongod --fork --logpath /var/log/mongod.log # Background에서 돌리는 거
# Wait for MongoDB to be fully ready. netcat must be installed.
# while ! nc -z localhost 27017; do   
#   sleep 0.1
# done
mvn jacoco:report
mvn package
java -jar ./target/cse364-project-1.0-SNAPSHOT.jar
