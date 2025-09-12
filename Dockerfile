FROM maven:3.9.9-eclipse-temurin-21 AS build

RUN mkdir -p /usr/local/apps/dicegame
WORKDIR /usr/local/apps/dicegame

ADD target/dicegame-0.0.1.jar .
ENTRYPOINT ["java","-jar","dicegame-0.0.1.jar"]


# docker build -t dicegame-app .