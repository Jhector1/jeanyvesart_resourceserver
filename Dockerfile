# --- Stage 1: build your JAR ---
FROM maven:3.8.6-jdk-17 AS builder
WORKDIR /app

# Cache-pom layer to speed rebuilds
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source & package
COPY src ./src
RUN mvn package -DskipTests -B

# --- Stage 2: run the JAR ---
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Tell Fly.io which port your app listens on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
