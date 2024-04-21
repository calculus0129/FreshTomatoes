cd milestone1
mvn jacoco:report
mvn package
mongod --fork --logpath /var/log/mongod.log # Background에서 돌리는거
java -jar ./target/cse364-project-1.0-SNAPSHOT.jar
