FROM maven:3.8.4-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

# Build the application
RUN mvn package -DskipTests


FROM openjdk:21-slim AS build
COPY --from=build /target/Tumblr-0.0.1-SNAPSHOT.jar Tumblr.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Tumblr.jar"]
