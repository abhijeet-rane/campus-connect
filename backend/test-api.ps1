Write-Host "Campus Connect API Testing Script" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green
Write-Host ""

$baseUrl = "http://localhost:8081/api/v1"

# Test 1: Health Check
Write-Host "1. Testing Health Endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/actuator/health" -Method GET
    Write-Host "✅ Health Check: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response: $($response.Content)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Health Check Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: API Info
Write-Host "2. Testing Info Endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/actuator/info" -Method GET
    Write-Host "✅ Info Check: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Info Check Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: User Registration
Write-Host "3. Testing User Registration..." -ForegroundColor Yellow
$registrationData = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/register" -Method POST -Body $registrationData -ContentType "application/json"
    Write-Host "✅ User Registration: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "⚠️ User Registration: $($_.Exception.Message)" -ForegroundColor Yellow
    Write-Host "   (This might fail if user already exists or database is not set up)" -ForegroundColor Gray
}
Write-Host ""

# Test 4: Get All Events (should work without auth)
Write-Host "4. Testing Get Events..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/events" -Method GET
    Write-Host "✅ Get Events: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Get Events Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Get All Projects (should work without auth)
Write-Host "5. Testing Get Projects..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/projects" -Method GET
    Write-Host "✅ Get Projects: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Get Projects Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

Write-Host "API Testing Complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Available Endpoints:" -ForegroundColor Cyan
Write-Host "- Health: GET $baseUrl/actuator/health"
Write-Host "- Register: POST $baseUrl/auth/register"
Write-Host "- Login: POST $baseUrl/auth/login"
Write-Host "- Events: GET $baseUrl/events"
Write-Host "- Projects: GET $baseUrl/projects"
Write-Host "- Users: GET $baseUrl/users (requires auth)"
Write-Host ""
Write-Host "Swagger UI: $baseUrl/swagger-ui.html" -ForegroundColor Magenta