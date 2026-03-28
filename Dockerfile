# =====================================================
# Mi Aplicación Java - Spring Boot Container
# =====================================================
# Image: tomcat:jre17-temurin-jammy
# Purpose: REST API con Spring Boot 2.7.8

FROM tomcat:jre17-temurin-jammy

LABEL maintainer="Cloud Native Academy <team@cloudnative.academy>"
LABEL description="Docker image for Mi Aplicacion Java - Spring Boot REST API"
LABEL version="2.0.0"

# --- STAGE 1: The Build Environment ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /home/app
# Copy your source code and build the JAR
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: The Final Runtime Image ---
FROM tomcat:jre17-temurin-jammy
RUN groupadd -g 10001 app && \
    useradd -u 10001 -g app -s /usr/sbin/nologin -m app

WORKDIR /app

# Ensure this matches exactly
COPY --from=build /home/app/target/*.jar /app/app.jar

USER app

# USER 10001
# =====================================================
# Health check
# =====================================================
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# =====================================================
# Expose Port and Entry Point
# =====================================================
EXPOSE 8080

ENTRYPOINT ["java", \
            "-Dspring.profiles.active=prod", \
            "-Xms256m", \
            "-Xmx512m", \
            "-jar", \
            "/app/app.jar"]





















