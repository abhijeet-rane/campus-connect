Write-Host "Starting Campus Connect Backend Server..." -ForegroundColor Green
Write-Host ""
Write-Host "Server will be available at: http://localhost:8081/api/v1" -ForegroundColor Cyan
Write-Host "Health check: http://localhost:8081/api/v1/actuator/health" -ForegroundColor Cyan
Write-Host "API Documentation: http://localhost:8081/api/v1/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""

mvn spring-boot:run "-Dspring-boot.run.jvmArguments=-Dserver.port=8081"