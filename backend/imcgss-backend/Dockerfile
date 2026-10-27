# ========================================================
# Dockerfile for IMCGSS (When Root Dir is backend/imcgss-backend)
# Multi-stage build for Spring Boot + PostgreSQL + Web
# ========================================================

# Stage 1: Build application with Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Minimal, fast, lightweight JRE runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/imcgss-backend-0.0.1-SNAPSHOT.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENV PORT=8080 \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
