# dice-game
A family-friendly game

# special message
If the sum of your winning game scores is greater than or equal to 100 (configurable), you win a prize!!!

# Tools and Technologies
1. JDK-21
2. Spring Boot 3.4.2
3. spring cloud 2024.0.0
4. maven
5. PostgreSQL
6. flyway
7. querydsl
8. feign client
9. Redis
10. kafka

# Build Application
mvn clean install

# Run Application
mvn spring-boot:run

# Build the Docker image and run container using
docker compose up -d

# Remove Docker container using
docker compose down

# see log
docker logs dicegame-app -f

# swagger-ui
http://localhost:9080/swagger-ui/index.html

# api documentation
http://localhost:9080/v3/api-docs

