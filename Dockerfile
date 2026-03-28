# =====================================================
# Mi Aplicación Java - Spring Boot Container
# =====================================================
# Image: tomcat:jre17-temurin-jammy
# Purpose: REST API con Spring Boot 2.7.8

FROM tomcat:jre17-temurin-jammy

LABEL maintainer="Cloud Native Academy <team@cloudnative.academy>"
LABEL description="Docker image for Mi Aplicacion Java - Spring Boot REST API"
LABEL version="2.0.0"

# Create user and group
RUN groupadd -g 10001 app && \
    useradd -u 10001 -g app -s /usr/sbin/nologin -m app

WORKDIR /app

# Combine the copy and ownership change into one step
COPY --chown=app:app target/*.jar /app/app.jar

# Adjust permissions if necessary (though 755 is usually default for files)
RUN chmod 755 /app/app.jar

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





















