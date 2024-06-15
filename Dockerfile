# Stage 1: Build the application
FROM maven:3.8.4-openjdk-21-slim AS build
WORKDIR /app

## Copy the POM file and download dependencies
#COPY pom.xml .
#RUN mvn dependency:go-offline -B

# Copy the rest of the application source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final image based on OpenJDK
FROM adoptopenjdk:21-jre-hotspot AS final
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /target/Tumblr-0.0.1-SNAPSHOT.jar Tumblr.jar

# Expose the port that the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "Tumblr.jar"]
