# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Create app directory
WORKDIR /app

# Copy everything into container
COPY . .

# Fix line endings (if on Windows)
RUN apt-get update && apt-get install -y dos2unix && dos2unix mvnw

# Give permission and build
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run the built JAR
CMD ["java", "-jar", "target/todoapp-0.0.1-SNAPSHOT.jar"]
