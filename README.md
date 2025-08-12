# Task Management System

A full-stack web application for managing tasks, built with Spring Boot, MySQL, and Microsoft Azure cloud technologies.

## Features

- Create, read, update, and delete tasks
- Task status management (TODO, IN_PROGRESS, COMPLETED, CANCELLED)
- Search tasks by title and status
- Responsive design using Bootstrap 5
- RESTful API architecture
- Real-time updates
- Form validation and error handling
- Azure cloud integration for scalable deployment
- Azure Blob Storage for file uploads (optional)
- Application Insights monitoring

## Technologies Used

### Backend

- Java 20
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL
- Maven
- Lombok

### Frontend

- HTML5
- CSS3
- JavaScript (ES6+)
- Bootstrap 5
- Font Awesome
- Thymeleaf

### Cloud Services (Azure)

- Azure App Service
- Azure Database for MySQL
- Azure Blob Storage
- Azure Application Insights

## Prerequisites

- Java 20 or higher
- MySQL 8.0 or higher (local development)
- Maven 3.6 or higher
- Modern web browser
- Azure subscription (for cloud deployment)

## Setup and Installation

### Local Development

1. **Clone the repository:**

```bash
git clone https://github.com/Vyom-Jain1/Task-Management-System.git
cd Task-Management-System
```

2. **Configure MySQL:**

   - Create a database named `task_management`
   - Update `src/main/resources/application.properties` with your MySQL credentials

3. **Build the project:**

```bash
mvn clean install
```

4. **Run the application:**

```bash
mvn spring-boot:run
```

5. **Access the application:**
   - Open your browser and navigate to `http://localhost:8082`

### Azure Cloud Deployment

1. **Set up Azure resources:**

   - Create Azure App Service for Java
   - Create Azure Database for MySQL
   - (Optional) Create Azure Storage Account for file uploads

2. **Configure environment variables in Azure App Service:**

   - `SPRING_DATASOURCE_URL`: Your Azure MySQL connection string
   - `SPRING_DATASOURCE_USERNAME`: MySQL username
   - `SPRING_DATASOURCE_PASSWORD`: MySQL password
   - `AZURE_STORAGE_CONNECTION_STRING`: (Optional) For blob storage
   - `AZURE_STORAGE_CONTAINER_NAME`: (Optional) Storage container name

3. **Deploy using Azure CLI:**

```bash
mvn clean package
az webapp deploy --resource-group <resource-group> --name <app-name> --src-path target/task-management-0.0.1-SNAPSHOT.jar
```

## API Endpoints

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `GET /api/tasks/status/{status}` - Get tasks by status
- `GET /api/tasks/search?title={title}` - Search tasks by title

## Configuration

### Local Development (application.properties)

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=your_username
spring.datasource.password=your_password

# Server Configuration
server.port=8082
```

### Azure Cloud Configuration (Environment Variables)

```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://your-server.mysql.database.azure.com:3306/task_management
SPRING_DATASOURCE_USERNAME=your_username@your-server
SPRING_DATASOURCE_PASSWORD=your_password

# Azure Storage (Optional)
AZURE_STORAGE_CONNECTION_STRING=your_connection_string
AZURE_STORAGE_CONTAINER_NAME=your_container_name
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── taskmanagement/
│   │               ├── config/          # Azure and app configuration
│   │               ├── controller/      # REST API controllers
│   │               ├── model/          # JPA entities
│   │               ├── repository/     # Data access layer
│   │               ├── service/        # Business logic layer
│   │               └── TaskManagementApplication.java
│   └── resources/
│       ├── static/                     # CSS, JS, images
│       ├── templates/                  # Thymeleaf templates
│       └── application.properties      # Configuration
```

## Key Features

### Cloud-Ready Architecture

- Environment-based configuration for seamless cloud deployment
- Azure Blob Storage integration for file handling
- Application Insights for monitoring and diagnostics
- Scalable design for cloud environments

### Modern Tech Stack

- Spring Boot 3.2.0 with Java 20
- Reactive programming patterns
- RESTful API design
- Responsive web interface

## Development Commands

```bash
# Local development
mvn spring-boot:run

# Build for production
mvn clean package

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=azure
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
