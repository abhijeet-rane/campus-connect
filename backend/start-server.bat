@echo off
echo Starting Campus Connect Backend Server...
echo.
echo Server will be available at: http://localhost:8081/api/v1
echo Health check: http://localhost:8081/api/v1/actuator/health
echo API Documentation: http://localhost:8081/api/v1/swagger-ui.html
echo.
mvn spring-boot:run "-Dspring-boot.run.jvmArguments=-Dserver.port=8081"