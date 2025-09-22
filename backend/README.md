# Campus Connect Backend

A comprehensive Spring Boot backend for the Campus Connect student community platform.

## 🚀 Features

- **User Management**: Registration, authentication, and profile management
- **Event Management**: Create, manage, and register for campus events
- **Project Hub**: Share project ideas and collaborate with peers
- **Badge System**: Gamified achievements and progress tracking
- **Announcements**: System-wide notifications and updates
- **Role-based Access**: Student and Administrator roles
- **RESTful APIs**: Complete REST API with proper HTTP status codes
- **Security**: JWT authentication and authorization
- **Logging**: Comprehensive logging with SLF4J
- **Exception Handling**: Global exception handling with proper error responses
- **Database**: PostgreSQL with JPA/Hibernate
- **Documentation**: OpenAPI/Swagger documentation

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (JSON Web Tokens)**
- **Maven**
- **Docker** (ready)

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd campus-connect/backend
```

### 2. Database Setup

#### Install PostgreSQL
- **Windows**: Download from [PostgreSQL official site](https://www.postgresql.org/download/windows/)
- **macOS**: `brew install postgresql`
- **Linux**: `sudo apt-get install postgresql postgresql-contrib`

#### Create Database
```sql
-- Connect to PostgreSQL as superuser
psql -U postgres

-- Create database
CREATE DATABASE campus_connect;

-- Create user (optional)
CREATE USER campus_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE campus_connect TO campus_user;

-- Exit psql
\q
```

#### Run Database Schema
```bash
# Navigate to database directory
cd database

# Run the schema script
psql -U postgres -d campus_connect -f schema.sql
```

### 3. Environment Configuration

#### Copy Environment File
```bash
# Copy the example environment file
cp .env.example .env
```

#### Update Environment Variables
Edit the `.env` file with your configuration:

```env
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/campus_connect
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

# JWT Configuration (Generate a strong secret)
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_here
JWT_EXPIRATION=86400000

# Server Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

### 4. Build and Run

#### Using Maven
```bash
# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

#### Using Java
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/campus-connect-backend-1.0.0.jar
```

### 5. Verify Installation

The application will start on `http://localhost:8080`

#### Health Check
```bash
curl http://localhost:8080/api/v1/actuator/health
```

#### API Documentation
Visit: `http://localhost:8080/api/v1/swagger-ui.html`

## 🐳 Docker Setup

### Build Docker Image
```bash
# Build image
docker build -t campus-connect-backend .

# Run container
docker run -p 8080:8080 --env-file .env campus-connect-backend
```

### Docker Compose (with PostgreSQL)
```bash
# Start all services
docker-compose up -d

# Stop services
docker-compose down
```

## 📚 API Documentation

### Authentication Endpoints
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/refresh` - Refresh JWT token
- `POST /api/v1/auth/logout` - User logout

### User Endpoints
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Event Endpoints
- `GET /api/v1/events` - Get all events
- `POST /api/v1/events` - Create event (Admin only)
- `GET /api/v1/events/{id}` - Get event by ID
- `PUT /api/v1/events/{id}` - Update event
- `POST /api/v1/events/{id}/register` - Register for event
- `DELETE /api/v1/events/{id}/register` - Unregister from event

### Project Endpoints
- `GET /api/v1/projects` - Get all projects
- `POST /api/v1/projects` - Create project
- `GET /api/v1/projects/{id}` - Get project by ID
- `PUT /api/v1/projects/{id}` - Update project
- `POST /api/v1/projects/{id}/like` - Like/unlike project
- `POST /api/v1/projects/{id}/comments` - Add comment

## 🔒 Security

### JWT Authentication
- JWT tokens are used for authentication
- Tokens expire after 24 hours (configurable)
- Refresh tokens available for extended sessions

### Password Security
- Passwords are hashed using BCrypt
- Configurable BCrypt strength (default: 12)

### CORS Configuration
- Configurable allowed origins
- Supports credentials for authenticated requests

## 📊 Monitoring

### Health Checks
- Spring Boot Actuator endpoints
- Database connectivity checks
- Custom health indicators

### Logging
- Structured logging with SLF4J
- Configurable log levels
- File and console output
- Request/response logging

### Metrics
- Application metrics with Micrometer
- Prometheus integration
- Custom business metrics

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Database
Tests use H2 in-memory database by default.

## 🚀 Deployment

### Render Deployment

1. **Create Render Account**: Sign up at [render.com](https://render.com)

2. **Create PostgreSQL Database**:
   - Go to Dashboard → New → PostgreSQL
   - Note the connection details

3. **Create Web Service**:
   - Go to Dashboard → New → Web Service
   - Connect your GitHub repository
   - Configure environment variables from `.env.production`

4. **Environment Variables**:
   ```
   DATABASE_URL=<your-render-postgres-url>
   JWT_SECRET=<your-jwt-secret>
   SPRING_PROFILES_ACTIVE=prod
   CORS_ALLOWED_ORIGINS=<your-frontend-url>
   ```

### Other Cloud Platforms

The application is ready for deployment on:
- **Heroku**: Use `Procfile` and environment variables
- **AWS**: Use Elastic Beanstalk or ECS
- **Google Cloud**: Use App Engine or Cloud Run
- **Azure**: Use App Service

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/campus_connect` |
| `DATABASE_USERNAME` | Database username | `postgres` |
| `DATABASE_PASSWORD` | Database password | `password` |
| `JWT_SECRET` | JWT signing secret | Required |
| `JWT_EXPIRATION` | JWT expiration time (ms) | `86400000` |
| `SERVER_PORT` | Server port | `8080` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `dev` |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | `http://localhost:3000,http://localhost:5173` |

### Profiles

- **dev**: Development profile with debug logging
- **prod**: Production profile with optimized settings
- **test**: Test profile with H2 database

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check PostgreSQL is running
   - Verify connection details in `.env`
   - Ensure database exists

2. **Port Already in Use**
   - Change `SERVER_PORT` in `.env`
   - Kill process using the port: `lsof -ti:8080 | xargs kill`

3. **JWT Secret Error**
   - Ensure `JWT_SECRET` is set and long enough
   - Generate new secret: `openssl rand -base64 64`

4. **CORS Issues**
   - Update `CORS_ALLOWED_ORIGINS` in `.env`
   - Check frontend URL matches exactly

### Logs

Check application logs:
```bash
# View logs
tail -f logs/campus-connect.log

# View specific log level
grep "ERROR" logs/campus-connect.log
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Contact the development team
- Check the documentation

---

**Campus Connect Backend** - Empowering student communities through technology 🎓