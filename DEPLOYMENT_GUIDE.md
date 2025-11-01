# Task Management System - Deployment Guide

## Overview

This Task Management System is a **Spring Boot application** with a MySQL database backend and a web frontend. Since it's a full-stack Java application with server-side components, it **cannot be deployed directly on GitHub Pages** (which only supports static HTML/CSS/JavaScript sites).

This guide provides multiple deployment options for hosting your application publicly.

---

## 🚀 Recommended Deployment Options

### Option 1: Railway (Easiest - One-Click Deploy)

**Railway** offers free tier hosting for Spring Boot applications with built-in database support.

#### Steps:

1. **Sign up for Railway**
   - Visit [railway.app](https://railway.app)
   - Sign in with your GitHub account

2. **Deploy from GitHub**
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose `Vyom-Jain1/Task-Management-System`
   - Railway will auto-detect Spring Boot

3. **Add MySQL Database**
   - In your project, click "New"
   - Select "Database" → "MySQL"
   - Railway will provision a MySQL instance

4. **Configure Environment Variables**
   - Click on your app service
   - Go to "Variables" tab
   - Add these variables:
     ```
     SPRING_DATASOURCE_URL=jdbc:mysql://${{MYSQL_HOST}}:${{MYSQL_PORT}}/${{MYSQL_DATABASE}}
     SPRING_DATASOURCE_USERNAME=${{MYSQL_USER}}
     SPRING_DATASOURCE_PASSWORD=${{MYSQL_PASSWORD}}
     ```

5. **Deploy**
   - Railway automatically builds and deploys
   - Get your public URL from the "Settings" tab

**Live URL**: `https://your-app.up.railway.app`

---

### Option 2: Render (Free Tier with Database)

**Render** provides free hosting for web services and PostgreSQL databases.

#### Steps:

1. **Update for PostgreSQL** (if using Render's free database)
   - Update `pom.xml` to include PostgreSQL driver:
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

2. **Sign up for Render**
   - Visit [render.com](https://render.com)
   - Sign in with GitHub

3. **Create PostgreSQL Database**
   - Click "New" → "PostgreSQL"
   - Name: `task-management-db`
   - Select free tier
   - Copy the Internal Database URL

4. **Deploy Web Service**
   - Click "New" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     - **Build Command**: `mvn clean install`
     - **Start Command**: `java -jar target/task-management-0.0.1-SNAPSHOT.jar`
     - **Environment**:
       ```
       SPRING_DATASOURCE_URL=<your-postgres-internal-url>
       SPRING_DATASOURCE_USERNAME=<your-db-username>
       SPRING_DATASOURCE_PASSWORD=<your-db-password>
       SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
       ```

5. **Deploy**
   - Click "Create Web Service"
   - Render builds and deploys automatically

**Live URL**: `https://task-management-system.onrender.com`

---

### Option 3: Heroku (Popular Platform)

**Heroku** is a well-established platform with free tier options.

#### Steps:

1. **Install Heroku CLI**
   ```bash
   # Download from https://devcenter.heroku.com/articles/heroku-cli
   ```

2. **Create Heroku App**
   ```bash
   heroku login
   heroku create task-management-vyom
   ```

3. **Add MySQL Database**
   ```bash
   heroku addons:create jawsdb:kitefin
   ```

4. **Configure Environment**
   ```bash
   heroku config:set SPRING_DATASOURCE_URL=<jawsdb-url>
   heroku config:set SPRING_DATASOURCE_USERNAME=<db-user>
   heroku config:set SPRING_DATASOURCE_PASSWORD=<db-password>
   ```

5. **Deploy**
   ```bash
   git push heroku main
   ```

**Live URL**: `https://task-management-vyom.herokuapp.com`

---

### Option 4: AWS Elastic Beanstalk (Production-Grade)

For production deployments with scalability.

#### Steps:

1. **Build JAR file**
   ```bash
   mvn clean package
   ```

2. **Install AWS CLI and EB CLI**
   ```bash
   pip install awsebcli
   ```

3. **Initialize Elastic Beanstalk**
   ```bash
   eb init -p java-17 task-management-system
   ```

4. **Create Environment**
   ```bash
   eb create task-management-env
   ```

5. **Add RDS MySQL Database**
   - Via AWS Console: RDS → Create MySQL instance
   - Link to Elastic Beanstalk environment

6. **Deploy**
   ```bash
   eb deploy
   ```

**Live URL**: Via AWS Elastic Beanstalk console

---

### Option 5: Docker + Cloud Platform

**Containerize and deploy anywhere**

#### Create Dockerfile:

```dockerfile
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/task-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Deploy Options:
- **Google Cloud Run** (Serverless containers)
- **Azure Container Apps**
- **DigitalOcean App Platform**
- **Fly.io**

---

## ⚠️ Why Not GitHub Pages?

**GitHub Pages only supports static sites** (HTML, CSS, JavaScript) and **cannot run backend servers** or databases.

Your Task Management System:
- ✅ Has HTML/CSS/JavaScript frontend (40.9% JS, 25.8% HTML, 14.7% CSS)
- ❌ Requires Java Spring Boot backend (18.6% Java)
- ❌ Needs MySQL database
- ❌ Requires server-side processing

For GitHub Pages deployment, you would need to:
1. Separate frontend into standalone static files
2. Deploy backend to a server platform (Railway/Render/Heroku)
3. Configure frontend to call backend API via CORS

---

## 🎯 Quick Start (Recommended: Railway)

1. **Fork this repository** (if you haven't already)
2. **Sign up at [Railway.app](https://railway.app)** with GitHub
3. **Click "New Project" → "Deploy from GitHub repo"**
4. **Select this repository**
5. **Add MySQL database** in Railway
6. **Set environment variables** (Railway provides database vars automatically)
7. **Get your live URL** from Railway dashboard
8. **Update README.md** with your live URL

---

## 📝 Post-Deployment Checklist

- [ ] Application is accessible via public URL
- [ ] Database connection is working
- [ ] Can create tasks
- [ ] Can view tasks
- [ ] Can mark tasks as complete
- [ ] Can delete tasks
- [ ] Update README.md with live URL
- [ ] Test from different devices/browsers

---

## 🔗 Update README.md

Once deployed, add this to your README.md:

```markdown
## 🌐 Live Demo

**Access the application here**: [https://your-app-url.com](https://your-app-url.com)

Deployed on: [Railway/Render/Heroku/etc.]
```

---

## 🆘 Troubleshooting

### Database Connection Issues
- Verify environment variables are set correctly
- Check database credentials
- Ensure database service is running

### Application Won't Start
- Check build logs for errors
- Verify Java version (17 required)
- Ensure `pom.xml` dependencies are correct

### 502/503 Errors
- Application may be starting up (wait 1-2 minutes)
- Check memory limits on free tier
- Review application logs

---

## 📞 Support

For deployment issues:
1. Check platform-specific documentation
2. Review application logs
3. Open an issue in this repository

---

## 🎉 Success!

Once deployed, your Task Management System will be publicly accessible and fully functional. Share your live URL with others!

**Example Live URL Format**:
- Railway: `https://task-management-system-production.up.railway.app`
- Render: `https://task-management-system.onrender.com`
- Heroku: `https://task-management-vyom.herokuapp.com`

---

*Last Updated: November 2025*
