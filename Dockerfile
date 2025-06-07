# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the specific JAR file and rename it inside the container
COPY target/HRM-0.0.1-SNAPSHOT.jar.original app.jar

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
