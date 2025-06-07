# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the specific JAR file and rename it to app.jar inside the container
COPY target/HRM-0.0.1-SNAPSHOT.jar.original app.jar

# Expose the port your app runs on (adjust if different)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
