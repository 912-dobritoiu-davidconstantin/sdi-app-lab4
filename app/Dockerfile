# Use a base image that supports Java
FROM openjdk:latest

# Set the working directory
WORKDIR /app

# Copy the application code to the Docker image
COPY . .

# Build your backend application
RUN mvn clean install

# Expose the necessary port(s)
EXPOSE 8080

# Specify the command to start your backend application
CMD ["java", "-jar", "target/your-backend-jar-file.jar"]
