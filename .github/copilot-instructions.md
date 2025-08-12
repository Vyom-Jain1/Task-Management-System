# Task Management System - AI Coding Agent Instructions

## Architecture Overview

This is a Spring Boot task management application with Azure cloud integration. The project follows a layered architecture:

- **Controller Layer**: REST API endpoints in `src/main/java/com/example/taskmanagement/controller/`
- **Service Layer**: Business logic with interface/implementation pattern (`TaskService` interface + `TaskServiceImpl`)
- **Repository Layer**: JPA repositories + custom JDBC DAO (`TaskRepository` + `TaskJdbcDao`)
- **Model Layer**: JPA entities (`Task`, `TaskStatus` enum)
- **Azure Integration**: Blob storage service and configuration for cloud deployment

## Key Development Patterns

### Service Layer Design

- Always implement services with interfaces (`TaskService` → `TaskServiceImpl`)
- Place implementations in `service/impl/` subdirectory
- Use constructor injection for dependencies

### Repository Pattern

- Primary: JPA repositories extending `JpaRepository<Task, Long>`
- Alternative: Custom JDBC DAO for complex queries (`TaskJdbcDao`)
- Both patterns coexist - choose based on query complexity

### Controller Structure

- All REST endpoints use `/api/` prefix
- Standardized error handling with try-catch blocks returning `ResponseEntity`
- CORS enabled with `@CrossOrigin(origins = "*")`

## Azure Cloud Integration

### Environment Configuration

- Database connection uses environment variables: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- Azure Blob Storage configured via: `azure.storage.connection-string`, `azure.storage.container-name`
- Local development uses hardcoded values in `application.properties`

### Azure Services

- **Blob Storage**: `AzureBlobStorageService` handles file upload/download/delete
- **Configuration**: `AzureConfiguration` class for cloud-specific beans
- **Deployment Target**: Azure App Service with MySQL Flexible Server

## Critical Development Commands

```bash
# Local development with environment variables
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:mysql://your-azure-host:3306/task_management"

# Build for Azure deployment
mvn clean package

# Run tests (includes service layer unit tests)
mvn test
```

## Database Schema

- Primary entity: `Task` with indexed fields (status, created_at, deadline)
- Uses Lombok for getters/setters/constructors
- Hibernate auto-DDL enabled (`spring.jpa.hibernate.ddl-auto=update`)

## Testing Strategy

- Unit tests focus on service layer (`TaskServiceTest`, `TaskServiceImplTest`)
- Uses Mockito for repository mocking
- No integration tests currently - add when needed

## Azure Deployment Checklist

1. Set environment variables in App Service configuration
2. Ensure Azure MySQL firewall allows App Service access
3. Create Blob Storage container if using file upload features
4. Enable Application Insights for monitoring

## File Structure Conventions

- Tests mirror main package structure
- Azure-specific classes in dedicated packages (`config/`, `service/`)
- Static resources follow Spring Boot conventions (`static/css/`, `static/js/`)
