# =====================================================
# Mi Aplicación Java - Spring Boot Container
# =====================================================
# Image: tomcat:jre17-temurin-jammy
# Purpose: REST API con Spring Boot 2.7.8

FROM tomcat:jre17-temurin-jammy

LABEL maintainer="Cloud Native Academy <team@cloudnative.academy>"
LABEL description="Docker image for Mi Aplicacion Java - Spring Boot REST API"
LABEL version="2.0.0"

# =====================================================
# Create non-root user for security
# =====================================================
RUN groupadd -g 10001 app && \
    useradd -u 10001 -g app -s /usr/sbin/nologin -m app

# =====================================================
# Setup Application Directory
# =====================================================
WORKDIR /app

# =====================================================
# Copy and configure JAR file
# =====================================================
COPY target/*.jar /app/app.jar
RUN chown -R app:app /app && \
    chmod 755 /app

# =====================================================
# Health check
# =====================================================
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# =====================================================
# Execute as non-root user
# =====================================================
USER 10001

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



















