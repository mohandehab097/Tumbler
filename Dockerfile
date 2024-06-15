FROM openjdk:21-slim AS build
WORKDIR /app

# Copy the Maven POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Create the final image based on OpenJDK
FROM openjdk:21-slim

WORKDIR /app

# Copy the JAR file from the build stage to the final image
COPY --from=build /app/target/Tumblr-0.0.1-SNAPSHOT.jar Tumblr.jar

# Expose the port that the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "Tumblr.jar"]