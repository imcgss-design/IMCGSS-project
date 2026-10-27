# IMCGSS - Internal Marks Consolidation & Grade Sheet System

A complete academic management web application built with **Spring Boot 4 / Java 17**, **PostgreSQL**, and a modern responsive HTML5/CSS/JavaScript frontend.

Configured and optimized for seamless hosting on **[Render](https://render.com/)** using **Render Web Service + Render PostgreSQL Database**.

---

## 🚀 Quick Render Deployment Options

### Option 1: Automatic Blueprint Deployment (Recommended - 1 Click)

1. Push this project to your GitHub repository.
2. Go to your [Render Dashboard](https://dashboard.render.com/).
3. Click **New +** and select **Blueprint**.
4. Select your repository.
5. Render reads [`render.yaml`](./render.yaml), automatically sets up:
   - **Render PostgreSQL Database** (`imcgss-db`, free tier)
   - **Render Web Service** (`imcgss-app`, Docker container)
   - Automatically links `DATABASE_URL` between them.
6. Click **Apply**. Within a few minutes, your site will be live!

---

### Option 2: Manual Dashboard Deployment (Step-by-Step)

#### Step 1: Create the Render PostgreSQL Database
1. Go to [Render Dashboard](https://dashboard.render.com/) → Click **New +** → **PostgreSQL**.
2. Set the following fields:
   - **Name:** `imcgss-db`
   - **Database:** `imcgss_db`
   - **User:** `imcgss_user`
   - **Region:** Choose the region closest to you (e.g., `Oregon (US West)` or `Frankfurt`).
   - **Plan:** `Free`.
3. Click **Create Database**.
4. Once created, copy the **Internal Database URL** (e.g., `postgres://imcgss_user:xxxx@dpg-xxxx-a:5432/imcgss_db`).

#### Step 2: Create the Render Web Service
1. Click **New +** → **Web Service**.
2. Connect your GitHub repository.
3. Configure the service:
   - **Name:** `imcgss-app`
   - **Region:** Same region as your database.
   - **Language / Environment:** `Docker`
   - **Dockerfile Path:** `./Dockerfile`
   - **Plan:** `Free`
4. Under **Environment Variables**, add:
   - `DATABASE_URL`: Paste the **Internal Database URL** from Step 1.
   - `PORT`: `8080` (Render binds to this automatically).
5. Click **Create Web Service**.

---

## 🔑 Default Login Credentials

When deployed to Render with a fresh PostgreSQL database, the system automatically initializes default accounts:

| Role | Username | Password |
|---|---|---|
| **Administrator** | `admin` | `admin123` |
| **Faculty** | `faculty` | `faculty123` |
| **Coordinator** | `coordinator` | `coord123` |
| **HOD / Reviewer** | `hod` | `hod123` |
| **Student** | `CS036` | `student123` |

---

## 🛠️ Key Architectural Configurations

1. **Automatic Render Database Parsing (`DatabaseConfig.java`)**:
   - Render passes database connection strings in `postgres://user:pass@host:port/dbname` format.
   - `DatabaseConfig.java` intercepts and transforms this into standard `jdbc:postgresql://host:port/dbname` format, eliminating the common JDBC connection crash on Render.

2. **Unified Single-Service Hosting (`src/main/resources/static/`)**:
   - The frontend HTML/CSS/JS files are packaged directly inside the Spring Boot executable JAR.
   - One Render Web Service serves both the web interface and the REST API (`/api/*`), eliminating CORS issues and mixed HTTP/HTTPS errors.

3. **Multi-Stage Dockerfile**:
   - Compiles with Maven in `maven:3.9.6-eclipse-temurin-17-alpine`.
   - Runs in a minimal `eclipse-temurin:17-jre-alpine` container with a secure non-root user.

4. **Dynamic Client API Base**:
   - The frontend automatically detects its host environment. If served from Render/Spring Boot, it calls relative `/api/*` endpoints. If opened locally as `file://`, it falls back to `http://localhost:8080`.

---

## 💻 Local Development

### Requirements
- JDK 17+
- PostgreSQL or MySQL (or run with default local settings)

### Running locally
```bash
cd backend/imcgss-backend
./mvnw clean spring-boot:run
```
Visit: [http://localhost:8080](http://localhost:8080)
