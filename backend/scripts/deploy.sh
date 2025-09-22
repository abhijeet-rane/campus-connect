#!/bin/bash

# Campus Connect Backend Deployment Script
# This script handles deployment to various environments

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="campus-connect-backend"
VERSION=${VERSION:-"latest"}
ENVIRONMENT=${ENVIRONMENT:-"development"}

# Functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    # Check if Docker is installed
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    # Check if Docker Compose is installed
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    # Check if .env file exists
    if [ ! -f ".env" ]; then
        log_warning ".env file not found. Copying from .env.example..."
        if [ -f ".env.example" ]; then
            cp .env.example .env
            log_info "Please update the .env file with your configuration before proceeding."
            exit 1
        else
            log_error ".env.example file not found. Please create a .env file with your configuration."
            exit 1
        fi
    fi
    
    log_success "Prerequisites check completed"
}

# Build the application
build_app() {
    log_info "Building Campus Connect Backend..."
    
    # Clean previous builds
    if [ -d "target" ]; then
        rm -rf target
    fi
    
    # Build with Maven
    if command -v mvn &> /dev/null; then
        log_info "Building with Maven..."
        mvn clean package -DskipTests
    else
        log_info "Maven not found. Building with Docker..."
        docker run --rm -v "$(pwd)":/app -w /app maven:3.9.5-openjdk-17-slim mvn clean package -DskipTests
    fi
    
    log_success "Application build completed"
}

# Build Docker image
build_docker() {
    log_info "Building Docker image..."
    
    docker build -t ${APP_NAME}:${VERSION} .
    docker tag ${APP_NAME}:${VERSION} ${APP_NAME}:latest
    
    log_success "Docker image built successfully"
}

# Deploy to development environment
deploy_development() {
    log_info "Deploying to development environment..."
    
    # Stop existing containers
    docker-compose down
    
    # Start services
    docker-compose up -d
    
    # Wait for services to be healthy
    log_info "Waiting for services to be ready..."
    sleep 30
    
    # Check if backend is healthy
    if curl -f http://localhost:8080/api/v1/actuator/health > /dev/null 2>&1; then
        log_success "Backend is healthy and ready!"
    else
        log_error "Backend health check failed. Please check the logs."
        docker-compose logs backend
        exit 1
    fi
    
    log_success "Development deployment completed"
}

# Deploy to production (Render)
deploy_production() {
    log_info "Preparing for production deployment..."
    
    # Check if production environment variables are set
    if [ -z "$DATABASE_URL" ] || [ -z "$JWT_SECRET" ]; then
        log_error "Production environment variables are not set. Please set DATABASE_URL and JWT_SECRET."
        exit 1
    fi
    
    # Build production image
    docker build -f Dockerfile.prod -t ${APP_NAME}:prod .
    
    log_info "Production image built. Deploy to your cloud provider using the built image."
    log_info "For Render deployment:"
    log_info "1. Connect your GitHub repository to Render"
    log_info "2. Set environment variables in Render dashboard"
    log_info "3. Deploy using the Dockerfile"
    
    log_success "Production preparation completed"
}

# Run database migrations
run_migrations() {
    log_info "Running database migrations..."
    
    # Check if database is accessible
    if docker-compose exec postgres pg_isready -U postgres -d campus_connect > /dev/null 2>&1; then
        log_info "Database is ready. Running schema..."
        docker-compose exec postgres psql -U postgres -d campus_connect -f /docker-entrypoint-initdb.d/01-schema.sql
        log_success "Database migrations completed"
    else
        log_error "Database is not accessible. Please check the database service."
        exit 1
    fi
}

# Show logs
show_logs() {
    log_info "Showing application logs..."
    docker-compose logs -f backend
}

# Stop services
stop_services() {
    log_info "Stopping services..."
    docker-compose down
    log_success "Services stopped"
}

# Clean up
cleanup() {
    log_info "Cleaning up..."
    
    # Remove stopped containers
    docker container prune -f
    
    # Remove unused images
    docker image prune -f
    
    # Remove unused volumes (be careful with this in production)
    if [ "$ENVIRONMENT" = "development" ]; then
        docker volume prune -f
    fi
    
    log_success "Cleanup completed"
}

# Show help
show_help() {
    echo "Campus Connect Backend Deployment Script"
    echo ""
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  build       Build the application and Docker image"
    echo "  deploy      Deploy to the specified environment (development/production)"
    echo "  migrate     Run database migrations"
    echo "  logs        Show application logs"
    echo "  stop        Stop all services"
    echo "  cleanup     Clean up Docker resources"
    echo "  help        Show this help message"
    echo ""
    echo "Environment Variables:"
    echo "  ENVIRONMENT   Target environment (development/production) [default: development]"
    echo "  VERSION       Application version [default: latest]"
    echo ""
    echo "Examples:"
    echo "  $0 build"
    echo "  $0 deploy"
    echo "  ENVIRONMENT=production $0 deploy"
    echo "  $0 logs"
}

# Main script logic
main() {
    case "${1:-help}" in
        "build")
            check_prerequisites
            build_app
            build_docker
            ;;
        "deploy")
            check_prerequisites
            build_app
            build_docker
            if [ "$ENVIRONMENT" = "production" ]; then
                deploy_production
            else
                deploy_development
            fi
            ;;
        "migrate")
            run_migrations
            ;;
        "logs")
            show_logs
            ;;
        "stop")
            stop_services
            ;;
        "cleanup")
            cleanup
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# Run main function with all arguments
main "$@"