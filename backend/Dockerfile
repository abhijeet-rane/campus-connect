# Campus Connect Backend Dockerfile
# Multi-stage build for optimized production image

# Build stage
FROM maven:3.9.5-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim AS runtime

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN groupadd -r campusconnect && useradd -r -g campusconnect campusconnect

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the JAR file from build stage
COPY --from=build /app/target/campus-connect-backend-*.jar app.jar

# Create logs directory
RUN mkdir -p /var/log/campus-connect && chown -R campusconnect:campusconnect /var/log/campus-connect

# Create uploads directory
RUN mkdir -p /app/uploads && chown -R campusconnect:campusconnect /app/uploads

# Change ownership of the app directory
RUN chown -R campusconnect:campusconnect /app

# Switch to non-root user
USER campusconnect

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]