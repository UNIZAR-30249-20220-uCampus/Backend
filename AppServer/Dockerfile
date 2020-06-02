FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=app-snapshot/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]