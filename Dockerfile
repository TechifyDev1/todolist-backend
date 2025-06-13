# Use a Java 17 base image
FROM eclipse-temurin:17-jdk

# Create app directory
WORKDIR /app

# Copy everything into the container
COPY . .

# Make the wrapper executable & build the app
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Run the Spring Boot app
CMD ["java", "-jar", "target/todoapp-0.0.1-SNAPSHOT.jar"]
