# Task Management System

A simple web-based task management application built with Spring Boot.

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Vyom-Jain1/Task-Management-System.git
cd Task-Management-System
```

### 2. Configure Database

Edit `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Create the database:
```sql
CREATE DATABASE task_management;
```

### 3. Run the Application

**Using Maven:**
```bash
mvn spring-boot:run
```

**Using JAR:**
```bash
mvn clean package
java -jar target/task-management-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Usage

### Add a Task
1. Open `http://localhost:8080` in your browser
2. Fill in the task title and description
3. Click "Add Task"

### View Tasks
- All tasks are displayed on the home page
- Tasks can be marked as completed or deleted

## Deployment

GitHub Actions automatically builds the project on every push. The compiled JAR file is available as an artifact in the Actions tab.

To download and run the deployed JAR:
1. Go to the [Actions tab](https://github.com/Vyom-Jain1/Task-Management-System/actions)
2. Click on the latest workflow run
3. Download the JAR artifact
4. Run: `java -jar task-management-0.0.1-SNAPSHOT.jar`
