# --- STAGE 1: Build ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /home/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime ---
FROM eclipse-temurin:17-jre-jammy

# Install curl for the Healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN groupadd -g 10001 app && \
    useradd -u 10001 -g app -s /usr/sbin/nologin -m app

WORKDIR /app

# Copy and set ownership in one layer
COPY --from=build --chown=app:app /home/app/target/*.jar /app/app.jar

USER app

# Dynamic Healthcheck: Use the PORT variable or default to 8080 for local testing
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:${PORT:-8080}/api/actuator/health || exit 1

# Railway ignores EXPOSE, but it's good documentation
EXPOSE 8080

# CRITICAL: Added server.port flag to bind to Railway's dynamic port
ENTRYPOINT ["java", \
            "-Dspring.profiles.active=prod", \
            "-Xms256m", \
            "-Xmx512m", \
            "-jar", \
            "/app/app.jar", \
            "--server.port=${PORT}"]