# Task Management System

A full-stack web application for managing tasks, built with Spring Boot, MySQL, and Thymeleaf.

## Features

- Create, read, update, and delete tasks
- Task status management (TODO, IN_PROGRESS, COMPLETED, CANCELLED)
- Search tasks by title and status
- Responsive design using Bootstrap 5
- RESTful API architecture
- Real-time updates
- Form validation and error handling

## Technologies Used

### Backend
- Java 20
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL
- Maven
- Lombok

### Frontend
- Thymeleaf
- Bootstrap 5
- HTML/CSS/JavaScript

## Prerequisites

- Java 20 or higher
- Maven 3.6+
- MySQL 8.0+

## Local Setup

### 1. Clone the repository
```bash
git clone https://github.com/Vyom-Jain1/Task-Management-System.git
cd Task-Management-System
```

### 2. Configure MySQL Database

Create a MySQL database:
```sql
CREATE DATABASE task_management;
```

### 3. Configure Application Properties

Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_management?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Build and Run

Using Maven:
```bash
mvn clean install
mvn spring-boot:run
```

Or using the Maven wrapper:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 5. Access the Application

Open your browser and navigate to:
```
http://localhost:8080
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/taskmanagement/
│   │   ├── controller/      # REST Controllers
│   │   ├── model/           # Entity classes
│   │   ├── repository/      # JPA Repositories
│   │   └── service/         # Business logic
│   └── resources/
│       ├── static/          # Static resources (CSS, JS, images)
│       ├── templates/       # Thymeleaf templates
│       └── application.properties
└── test/                    # Test classes
```

## API Endpoints

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| DELETE | `/api/tasks/{id}` | Delete task |
| GET | `/api/tasks/search` | Search tasks by title and status |

## Database Schema

### Task Table

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key, auto-increment |
| title | VARCHAR(255) | Task title |
| description | TEXT | Task description |
| status | VARCHAR(50) | Task status (TODO, IN_PROGRESS, COMPLETED, CANCELLED) |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

## Usage

1. **Create a Task**: Click the "New Task" button and fill in the task details
2. **View Tasks**: All tasks are displayed on the main page
3. **Edit Task**: Click the "Edit" button on any task to modify it
4. **Delete Task**: Click the "Delete" button to remove a task
5. **Search Tasks**: Use the search bar to filter tasks by title or status
6. **Change Status**: Update task status using the status dropdown

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/task-management-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Verify database credentials in `application.properties`
- Check if the database `task_management` exists

### Port Already in Use
If port 8080 is already in use, change it in `application.properties`:
```properties
server.port=8081
```

## Contributing

Feel free to submit issues and enhancement requests!

## License

This project is open source and available under the MIT License.

## Author

Vyom Jain

## Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive UI components
