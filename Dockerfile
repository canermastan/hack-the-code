FROM openjdk:17-jdk-slim-buster

COPY ./target/KodlaSavas-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch KodlaSavas-0.0.1-SNAPSHOT.jar'

ARG JAR_FILE=target/KodlaSavas-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","KodlaSavas-0.0.1-SNAPSHOT.jar"]