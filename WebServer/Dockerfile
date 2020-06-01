FROM openjdk:11-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=web-snapshot/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]