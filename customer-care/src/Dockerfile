FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/care-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]