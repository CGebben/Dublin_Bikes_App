FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy everything into the container
COPY . .

# Build the JAR inside the container
RUN ./mvnw clean package -DskipTests

# Expose default Spring Boot port
EXPOSE 8080

# Run the generated JAR
ENTRYPOINT ["java", "-jar", "target/dublinbikes-0.0.1-SNAPSHOT.jar"]
