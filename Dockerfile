# Build stage using Maven
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory for the build
WORKDIR /app

# Copy pom.xml and source code into the container
COPY pom.xml ./
COPY src ./src

# Run Maven to build the project, skipping tests
RUN mvn clean install -DskipTests

# Runtime stage using OpenJDK 17
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR from the build stage into the runtime container
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
