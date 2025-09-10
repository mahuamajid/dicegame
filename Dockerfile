FROM maven:3.9.9-eclipse-temurin-21 AS build
LABEL maintainer="mahua"

ADD target/dicegame-0.0.1.jar dicegame-0.0.1.jar
ENTRYPOINT ["java","-jar","dicegame-0.0.1.jar"]


# docker build -t dicegame-app .