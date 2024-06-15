FROM eclipse-temurin:21-jdk-alpine as build
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/Tumblr-0.0.1-SNAPSHOT.jar Tumblr.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Tumblr.jar"]
