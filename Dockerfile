# Use Ubuntu 22.04 as base image
FROM ubuntu:22.04

# Install prerequisites
# netcat is just for connection checking. It is subsidiary.
RUN apt-get update && DEBIAN_FRONTEND=noninteractive
# For git installation
RUN apt-get install -y tzdata
RUN apt-get install -y wget
RUN apt-get install -y gnupg2
RUN apt-get install -y vim
RUN apt-get install -y curl
RUN apt-get install -y openjdk-17-jdk
RUN apt-get install -y maven
RUN apt-get install -y git
RUN rm -rf /var/lib/apt/lists/*

# Import MongoDB public GPG key
RUN wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | apt-key add -

# Add MongoDB repository to sources list
RUN echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/6.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-6.0.list

# Update package list and install MongoDB
RUN apt-get update
RUN apt-get install -y mongodb-org \
    && rm -rf /var/lib/apt/lists/*

# Create necessary directories
RUN mkdir -p /data/db /data/configdb

# Change ownership of directories
RUN chown -R mongodb:mongodb /data/db /data/configdb

# Add your stuff below:

# create /root/project directory and set it as WORKDIR
RUN mkdir /root/project
WORKDIR /root/project

# add "milestone1" directory and "run.sh" file under WORKDIR
# RUN mkdir /milestone1
#COPY milestone1 ./milestone1
COPY run.sh .

# A container execute a bash shell by default when the built image is launched
# Comment-out this line when submitting!
# CMD ["sh", "run.sh"]

# Running "a bash shell"
CMD ["/bin/bash"]
