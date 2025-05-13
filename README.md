# Task Management System

A full-stack web application for managing tasks, built with Spring Boot, MySQL, and modern web technologies.

## Features

- Create, read, update, and delete tasks
- Task status management (TODO, IN_PROGRESS, COMPLETED, CANCELLED)
- Search tasks by title
- Responsive design using Bootstrap 5
- RESTful API architecture
- Real-time updates
- Form validation
- Error handling

## Technologies Used

### Backend

- Java 11
- Spring Boot 2.7.0
- Spring Data JPA
- MySQL
- Maven
- JUnit 5
- Mockito

### Frontend

- HTML5
- CSS3
- JavaScript (ES6+)
- Bootstrap 5
- Font Awesome
- Thymeleaf

## Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Modern web browser

## Setup and Installation

1. Clone the repository:

```bash
git clone <repository-url>
```

2. Configure MySQL:

   - Create a database named `task_management`
   - Update `application.properties` with your MySQL credentials

3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
```

5. Access the application:
   - Open your browser and navigate to `http://localhost:8080`

## API Endpoints

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `GET /api/tasks/status/{status}` - Get tasks by status
- `GET /api/tasks/search?title={title}` - Search tasks by title

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── taskmanagement/
│   │               ├── controller/
│   │               ├── model/
│   │               ├── repository/
│   │               ├── service/
│   │               └── TaskManagementApplication.java
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   └── js/
│       ├── templates/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── example/
                └── taskmanagement/
                    └── service/
```

## Testing

Run the test suite:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
