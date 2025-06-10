# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven wrapper files (important!)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make wrapper executable
RUN chmod +x mvnw

# Pre-fetch dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the project (including /src)
COPY src src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy the JAR built in Stage 1
COPY --from=build /app/target/HRM-0.0.1-SNAPSHOT.jar app.jar

# Expose app port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
