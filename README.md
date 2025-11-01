# Task Management System

A simple web-based task management application built with Spring Boot.

---

## 🚀 **DEPLOYMENT AVAILABLE**

**⚠️ IMPORTANT**: This is a **Spring Boot application** with a MySQL database backend. It **cannot be deployed directly on GitHub Pages** (which only supports static HTML/CSS/JavaScript sites).

### ✅ Ready to Deploy?

📋 **[View Complete Deployment Guide →](DEPLOYMENT_GUIDE.md)**

The comprehensive deployment guide includes:
- **Railway** (Recommended - Easiest one-click deployment)
- **Render** (Free tier with database)
- **Heroku** (Popular platform)
- **AWS Elastic Beanstalk** (Production-grade)
- **Docker + Cloud Platforms**

### 🎯 Quick Deploy (3 Steps)

1. **Sign up at [Railway.app](https://railway.app)** (Free tier available)
2. **Deploy from GitHub** - Select this repository
3. **Add MySQL database** - Railway provisions it automatically

**That's it!** You'll get a live URL like: `https://task-management-system.up.railway.app`

---

## 📖 Local Setup Instructions

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

---

## 💻 Usage

### Add a Task

1. Open `http://localhost:8080` in your browser
2. Fill in the task title and description
3. Click "Add Task"

### View Tasks

- All tasks are displayed on the home page
- Tasks can be marked as completed or deleted

---

## 🔄 CI/CD - GitHub Actions

GitHub Actions automatically builds the project on every push. The compiled JAR file is available as an artifact in the Actions tab.

To download and run the deployed JAR:

1. Go to the [Actions tab](https://github.com/Vyom-Jain1/Task-Management-System/actions)
2. Click on the latest workflow run
3. Download the JAR artifact
4. Run: `java -jar task-management-0.0.1-SNAPSHOT.jar`

---

## 🛠️ Technology Stack

- **Backend**: Spring Boot (Java)
- **Frontend**: HTML, CSS, JavaScript
- **Database**: MySQL
- **Build Tool**: Maven

---

## 📚 Project Structure

```
Task-Management-System/
├── src/main/
│   ├── java/
│   │   └── com/example/taskmanagement/
│   └── resources/
│       ├── static/        # CSS, JS, Images
│       ├── templates/     # HTML templates
│       └── application.properties
├── DEPLOYMENT_GUIDE.md   # 📋 Complete deployment instructions
├── README.md
└── pom.xml
```

---

## 🌐 Why Not GitHub Pages?

**GitHub Pages** only supports static websites (HTML/CSS/JS files that run in the browser).

This application:
- ✅ Has HTML/CSS/JavaScript frontend
- ❌ Requires Java Spring Boot backend (server-side processing)
- ❌ Needs MySQL database
- ❌ Cannot run without a server

**Solution**: Deploy the full application to a cloud platform like Railway, Render, or Heroku (see [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md))

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is open source and available under the MIT License.

---

## 📞 Support

For deployment help, refer to [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) or open an issue.

---

**Ready to deploy?** Check out the [📋 DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for step-by-step instructions!
