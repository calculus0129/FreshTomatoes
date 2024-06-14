#!/usr/bin/env bash

# Define paths
PROJECT_DIR="milestone1"
TOMCAT_HOME="tomcat"

# Stop Tomcat server
echo "Stopping Tomcat..."
sh $TOMCAT_HOME/bin/catalina.sh stop

# Wait for Tomcat to fully stop (adjust time as necessary)
# sleep 5

# Build the project and skip tests for faster deployment
echo "Building project..."
cd "$PROJECT_DIR"
mvn clean package -DskipTests # -P war-build -P init 
# mvn clean package -Dmaven.test.skip=true # compiles the test, but does not run the test.
# skipping test generation: -DskipTests

cd ..


# Remove previous deployment
echo "Removing previous deployment..."
# rm -rf $TOMCAT_HOME/webapps/cse364-project*
rm -rf $TOMCAT_HOME/webapps/ROOT

rm -rf $TOMCAT_HOME/work/Catalina/localhost/*


# Copy new WAR file to the Tomcat webapps directory
echo "Deploying new version..."
# cp $PROJECT_DIR/target/cse364-project.war $TOMCAT_HOME/webapps/

# DIR=$TOMCAT_HOME/webapps/cse364-project/WEB-INF/classes/data/

# if [[ -d $DIR ]]; then
#     echo "Removing existing directory: $DIR"
#     mkdir "$DIR"
# fi

# cp $PROJECT_DIR/data/* $DIR
cp $PROJECT_DIR/target/cse364-project.war $TOMCAT_HOME/webapps/ROOT.war

# Start Tomcat server
echo "Starting Tomcat..."
sh $TOMCAT_HOME/bin/catalina.sh run

echo "Deployment complete."