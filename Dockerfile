# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven project files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Pre-fetch dependencies (optional, speeds up builds)
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY src src

# Build the JAR file
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17
WORKDIR /app

# Copy the built JAR from stage 1
COPY --from=build /app/target/HRM-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
