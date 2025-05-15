# Stage 1: Build stage
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN chmod +x mvnw
RUN ./mvnw -B clean install -DskipTests

# Stage 2: Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar  # Chỉ định tên file cụ thể
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar"]
