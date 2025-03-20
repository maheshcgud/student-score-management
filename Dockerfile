# Java 17 OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Making argument for better maintenance
ARG JAR_FILE=target/*.jar

# Working directory inside the container
WORKDIR /app

# Copying the application JAR file into the container
COPY ${JAR_FILE} studentapp.jar

# Exposing the port the application runs on
EXPOSE 8081

# Running the application
ENTRYPOINT ["java", "-jar", "studentapp.jar"]