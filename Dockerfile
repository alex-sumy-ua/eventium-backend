# Use an official OpenJDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY target/eventium-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
