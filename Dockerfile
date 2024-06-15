FROM openjdk:21-slim AS build
WORKDIR /app
COPY src ./src
RUN mvn clean package -DskipTests
COPY --from=build /target/Tumblr-0.0.1-SNAPSHOT.jar Tumblr.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Tumblr.jar"]
