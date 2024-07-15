# Use a base image with JDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven build file and source code
COPY pom.xml .
COPY src ./src

# Copy the built JAR file to the Docker image
COPY target/service-1.0.0.jar todo-service.jar

# Expose the port the application runs on
EXPOSE 8092

# Set the entry point for the Docker container
ENTRYPOINT ["java", "-jar", "todo-service.jar"]