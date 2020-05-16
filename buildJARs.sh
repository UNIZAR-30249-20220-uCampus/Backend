#!/bin/bash
echo  "Hi, i'm working on your JAR files bro"

pwd=$(pwd)
#echo $pwd

# Build DomainObjects JAR file
cd $pwd/uCampusLibs/domainObjects
echo "I'm in $(pwd)"
gradle build

# Build DtoObjects JAR file
cd $pwd/uCampusLibs/dtoObjects
echo "I'm in $(pwd)"
cp $pwd/uCampusLibs/domainObjects/build/libs/domainObjects.jar $pwd/uCampusLibs/dtoObjects/libs/domainObjects.jar
gradle build

# Import JAR files into AppServer
cp $pwd/uCampusLibs/domainObjects/build/libs/domainObjects.jar $pwd/AppServer/libs/domainObjects.jar
cp $pwd/uCampusLibs/dtoObjects/build/libs/dtoObjects.jar $pwd/AppServer/libs/dtoObjects.jar

# Import JAR files into WebServer
cp $pwd/uCampusLibs/domainObjects/build/libs/domainObjects.jar $pwd/WebServer/libs/domainObjects.jar
cp $pwd/uCampusLibs/dtoObjects/build/libs/dtoObjects.jar $pwd/WebServer/libs/dtoObjects.jar

# Return to original path
cd $pwd
