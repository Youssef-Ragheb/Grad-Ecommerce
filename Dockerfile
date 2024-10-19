# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Ensure dependencies are installed for alpine if needed
RUN apk add --no-cache bash

# Copy the JAR file into the container
COPY ./target/ecommerce.jar /app/ecommerce.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/ecommerce.jar"]