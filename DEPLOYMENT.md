# Task Management System - Deployment Guide

This guide explains how to build and deploy the Task Management System using GitHub Actions and Maven.

## Overview

This repository includes an automated GitHub Actions workflow that:
- Builds the Spring Boot application using Maven
- Runs unit tests
- Packages the application as a JAR file
- Uploads the JAR artifact for download

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## GitHub Actions Workflow

The automated build workflow is located at `.github/workflows/deploy.yml` and runs automatically on:
- Every push to the `main` branch
- Every pull request to the `main` branch
- Manual trigger via GitHub Actions UI

### Workflow Steps

1. **Checkout Code**: Retrieves the latest code from the repository
2. **Set up JDK 17**: Configures the Java environment
3. **Cache Maven Dependencies**: Caches Maven packages for faster builds
4. **Build with Maven**: Compiles the application (`mvn clean install`)
5. **Run Tests**: Executes unit tests (`mvn test`)
6. **Package Application**: Creates the JAR file (`mvn package`)
7. **Upload JAR Artifact**: Makes the JAR available for download (30-day retention)

### Viewing Build Results

1. Go to the **Actions** tab in your GitHub repository
2. Click on the latest workflow run
3. View build logs and test results
4. Download the JAR artifact from the "Artifacts" section

## Local Development Build

### Build the Application

```bash
# Clone the repository
git clone https://github.com/Vyom-Jain1/Task-Management-System.git
cd Task-Management-System

# Build with Maven
mvn clean install

# Run tests
mvn test

# Package the application
mvn package -DskipTests
```

The JAR file will be created in the `target/` directory:
```
target/task-management-system-0.0.1-SNAPSHOT.jar
```

## Database Configuration

Before running the application, configure your MySQL database:

### 1. Create Database

```sql
CREATE DATABASE taskmanagement;
```

### 2. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanagement
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Running the Application

### Run Locally

```bash
# Using Maven
mvn spring-boot:run

# Or run the JAR directly
java -jar target/task-management-system-0.0.1-SNAPSHOT.jar
```

The application will start on: `http://localhost:8080`

## Deployment Options

### Option 1: Manual Server Deployment

1. **Download the JAR**: Get the JAR from GitHub Actions artifacts or build locally
2. **Transfer to Server**: Upload the JAR to your server using SCP/FTP
   ```bash
   scp target/task-management-system-0.0.1-SNAPSHOT.jar user@server:/path/to/app/
   ```
3. **Configure Database**: Set up MySQL on your server
4. **Run the Application**:
   ```bash
   java -jar task-management-system-0.0.1-SNAPSHOT.jar
   ```

### Option 2: Run as a Service (Linux)

Create a systemd service file `/etc/systemd/system/taskmanagement.service`:

```ini
[Unit]
Description=Task Management System
After=mysql.service

[Service]
User=appuser
ExecStart=/usr/bin/java -jar /opt/taskmanagement/task-management-system-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start the service:
```bash
sudo systemctl enable taskmanagement
sudo systemctl start taskmanagement
sudo systemctl status taskmanagement
```

### Option 3: Traditional Web Server (Tomcat)

1. **Modify pom.xml** to package as WAR:
   ```xml
   <packaging>war</packaging>
   ```

2. **Rebuild the application**:
   ```bash
   mvn clean package
   ```

3. **Deploy to Tomcat**:
   - Copy the WAR file to Tomcat's `webapps/` directory
   - Restart Tomcat
   - Access at `http://server:8080/task-management-system`

## Environment Variables

For production deployment, use environment variables instead of hardcoding credentials:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://production-db:3306/taskmanagement
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=secure_password

java -jar task-management-system-0.0.1-SNAPSHOT.jar
```

Or create an `application-prod.properties` file:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
server.port=${PORT:8080}
```

Run with production profile:
```bash
java -jar task-management-system-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Monitoring and Logs

### Application Logs

```bash
# View logs in real-time
tail -f logs/application.log

# View Spring Boot logs
journalctl -u taskmanagement -f
```

### Health Check

Access the application health endpoint (if Spring Actuator is enabled):
```
http://localhost:8080/actuator/health
```

## Troubleshooting

### Build Fails

1. Check Java version: `java -version` (must be 17+)
2. Check Maven version: `mvn -version` (must be 3.6+)
3. Clear Maven cache: `mvn clean` or delete `~/.m2/repository`
4. Review build logs in GitHub Actions

### Database Connection Issues

1. Verify MySQL is running: `systemctl status mysql`
2. Test connection: `mysql -u username -p -h host`
3. Check firewall rules for port 3306
4. Verify credentials in application.properties

### Application Won't Start

1. Check if port 8080 is already in use: `lsof -i :8080`
2. Review application logs for error messages
3. Verify database is accessible
4. Check Java heap memory settings

## Performance Tuning

### JVM Options

```bash
java -Xms512m -Xmx2048m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar task-management-system-0.0.1-SNAPSHOT.jar
```

### Database Optimization

- Enable connection pooling
- Configure appropriate connection pool size
- Add database indexes for frequently queried columns
- Monitor slow queries

## Security Recommendations

1. **Never commit sensitive data**: Use environment variables or secrets management
2. **Use HTTPS**: Configure SSL/TLS certificates
3. **Database Security**: Use strong passwords, restrict access
4. **Update Dependencies**: Regularly update Spring Boot and libraries
5. **Firewall**: Restrict access to necessary ports only

## Continuous Integration

The GitHub Actions workflow ensures:
- ✅ Code compiles successfully
- ✅ All tests pass
- ✅ JAR artifact is built correctly
- ✅ Build artifacts are available for download

## Next Steps

1. **Download JAR**: Go to Actions → Latest run → Artifacts
2. **Configure Database**: Set up MySQL with proper credentials
3. **Deploy**: Choose your deployment method (server, service, or Tomcat)
4. **Monitor**: Set up logging and monitoring
5. **Scale**: Consider load balancing for high-traffic applications

## Support

For issues or questions:
- Check the [Issues](https://github.com/Vyom-Jain1/Task-Management-System/issues) page
- Review application logs
- Consult Spring Boot documentation: https://spring.io/projects/spring-boot

---

**Repository**: [Task-Management-System](https://github.com/Vyom-Jain1/Task-Management-System)  
**Workflow**: [deploy.yml](.github/workflows/deploy.yml)  
**Technology Stack**: Spring Boot, MySQL, Thymeleaf, Maven
