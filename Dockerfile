# Use the official OpenJDK 17 image from Docker Hub
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build folder (replace with the correct JAR filename)
COPY build/libs/customer-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application is running on (adjust as necessary)
EXPOSE 8080

# Command to run the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]
