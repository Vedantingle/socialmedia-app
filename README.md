# 🚀 SocialMedia App — Java Spring Boot

A full-stack social media web application built with **Java Spring Boot**, **Spring Security**, **JPA/Hibernate**, **H2/MySQL**, and **Thymeleaf**.

---

## 📋 Features

- ✅ **User Registration & Login** (Spring Security with BCrypt password hashing)
- ✅ **Create / Delete Posts** (up to 500 characters)
- ✅ **Like / Unlike Posts**
- ✅ **Comment on Posts**
- ✅ **Follow / Unfollow Users**
- ✅ **Personalized Feed** (posts from people you follow)
- ✅ **Explore Page** (all posts + search)
- ✅ **User Profiles** with follower/following stats
- ✅ **Edit Profile** (bio, location, website)
- ✅ **Search Posts** by keyword
- ✅ **H2 In-Memory Database** for dev (no setup needed)
- ✅ **MySQL Support** for production
- ✅ **Responsive Design**
- ✅ **Demo Data** auto-seeded on startup

---

## 🛠️ Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Backend    | Java 17, Spring Boot 3.2            |
| Security   | Spring Security 6 + BCrypt          |
| Database   | H2 (dev) / MySQL (prod)             |
| ORM        | Spring Data JPA + Hibernate         |
| Frontend   | Thymeleaf + HTML5 + CSS3 + Vanilla JS |
| Build Tool | Maven                               |

---

## ✅ Prerequisites — Install These First

### 1. Java 17 (JDK)
- Download from: https://adoptium.net/en-GB/temurin/releases/?version=17
- After install, verify:
  ```
  java -version
  ```
  You should see: `openjdk version "17.x.x"`

### 2. Apache Maven
- Download from: https://maven.apache.org/download.cgi
- Or on Windows, use: https://chocolatey.org/packages/maven
- Verify:
  ```
  mvn -version
  ```

### 3. (Optional, for production) MySQL
- Download from: https://dev.mysql.com/downloads/mysql/
- Or install MySQL Workbench: https://dev.mysql.com/downloads/workbench/

### 4. A code editor (recommended)
- **IntelliJ IDEA Community** (free): https://www.jetbrains.com/idea/download/
- OR **VS Code** with Java Extension Pack

---

## 🚀 How to Run (Step by Step)

### STEP 1 — Extract the project
Unzip `socialmedia-app.zip` to any folder, e.g., `C:\Projects\socialmedia-app` or `~/projects/socialmedia-app`.

### STEP 2 — Open Terminal / Command Prompt
Navigate to the project folder:
```bash
cd socialmedia-app
```
You should see `pom.xml` in this folder. Verify with:
```bash
ls          # on Mac/Linux
dir         # on Windows
```

### STEP 3 — Build the project
```bash
mvn clean install -DskipTests
```
This downloads all dependencies (takes 2–5 minutes on first run). You'll see `BUILD SUCCESS`.

### STEP 4 — Run the application
```bash
mvn spring-boot:run
```
OR run the JAR directly:
```bash
java -jar target/socialmedia-app-1.0.0.jar
```

### STEP 5 — Open in browser
Visit: **http://localhost:8080**

You'll be redirected to the login page. Three demo accounts are pre-loaded:

| Username | Password    |
|----------|-------------|
| alice    | password123 |
| bob      | password123 |
| charlie  | password123 |

### STEP 6 — View the Database (optional)
Visit: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:socialmediadb`
- Username: `sa`
- Password: *(leave blank)*
- Click **Connect**

---

## 🗄️ Switching to MySQL (Production)

### Step 1 — Create MySQL database
```sql
CREATE DATABASE socialmediadb;
```

### Step 2 — Edit `src/main/resources/application-prod.properties`
Change:
```properties
spring.datasource.password=your_mysql_password_here
```
to your actual MySQL root password.

### Step 3 — Run with prod profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
OR
```bash
java -jar target/socialmedia-app-1.0.0.jar --spring.profiles.active=prod
```

---

## 📁 Project Structure

```
socialmedia-app/
├── pom.xml                          ← Maven build config + dependencies
└── src/
    ├── main/
    │   ├── java/com/socialmedia/
    │   │   ├── SocialMediaApplication.java     ← Entry point + demo data seeder
    │   │   ├── model/
    │   │   │   ├── User.java                   ← User entity (DB table)
    │   │   │   ├── Post.java                   ← Post entity
    │   │   │   └── Comment.java                ← Comment entity
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java         ← DB queries for users
    │   │   │   ├── PostRepository.java         ← DB queries for posts
    │   │   │   └── CommentRepository.java      ← DB queries for comments
    │   │   ├── service/
    │   │   │   ├── UserService.java            ← Business logic for users
    │   │   │   └── PostService.java            ← Business logic for posts
    │   │   ├── controller/
    │   │   │   ├── AuthController.java         ← Login, Register, Home routes
    │   │   │   ├── FeedController.java         ← Feed, Post CRUD, Likes
    │   │   │   └── ProfileController.java      ← Profile, Follow, Search
    │   │   └── config/
    │   │       └── SecurityConfig.java         ← Spring Security setup
    │   └── resources/
    │       ├── application.properties          ← App config (H2/dev)
    │       ├── application-prod.properties     ← MySQL/prod config
    │       ├── templates/                      ← Thymeleaf HTML pages
    │       │   ├── auth/login.html
    │       │   ├── auth/register.html
    │       │   ├── feed.html
    │       │   ├── profile.html
    │       │   ├── explore.html
    │       │   ├── post-detail.html
    │       │   └── edit-profile.html
    │       └── static/
    │           ├── css/style.css               ← All styles
    │           └── js/app.js                   ← Frontend JavaScript
    └── test/
        └── java/com/socialmedia/
            └── SocialMediaApplicationTests.java
```

---

## 🌍 Deploy to the Cloud

### Option A — Deploy to Railway (easiest, free tier available)

1. Create account at https://railway.app
2. Install Railway CLI:
   ```bash
   npm install -g @railway/cli
   railway login
   ```
3. Inside your project folder:
   ```bash
   railway init
   railway add --database mysql
   railway up
   ```
4. Railway automatically provides `DATABASE_URL`. Update `application-prod.properties` to use environment variable or use Railway's dashboard to set:
   ```
   SPRING_PROFILES_ACTIVE=prod
   ```

### Option B — Deploy to Render (free tier)

1. Push your project to GitHub
2. Go to https://render.com → New → Web Service
3. Connect GitHub repo
4. Build command: `mvn clean package -DskipTests`
5. Start command: `java -jar target/socialmedia-app-1.0.0.jar`
6. Add environment variable: `SPRING_PROFILES_ACTIVE=prod`
7. Add a MySQL database from Render's dashboard

### Option C — Deploy on any Linux VPS (e.g., AWS, DigitalOcean)

```bash
# 1. Build the JAR
mvn clean package -DskipTests

# 2. Copy JAR to server
scp target/socialmedia-app-1.0.0.jar user@your-server:/home/user/

# 3. SSH into server and run
ssh user@your-server
java -jar socialmedia-app-1.0.0.jar --spring.profiles.active=prod &

# 4. Run as background service with nohup
nohup java -jar socialmedia-app-1.0.0.jar --spring.profiles.active=prod > app.log 2>&1 &
```

---

## 🔧 Running in IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open** → select the `socialmedia-app` folder
2. Wait for Maven to download dependencies (progress bar at bottom)
3. Open `src/main/java/com/socialmedia/SocialMediaApplication.java`
4. Click the green ▶️ **Run** button next to `public static void main`
5. Open browser at **http://localhost:8080**

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| `java: error: release version 17 not supported` | Install Java 17 JDK and set `JAVA_HOME` |
| `Port 8080 already in use` | Change port in `application.properties`: `server.port=9090` |
| `BUILD FAILURE` on first run | Check internet connection, run `mvn dependency:resolve` |
| H2 console says "Connection failed" | Make sure `spring.h2.console.enabled=true` in properties |
| Blank/white page | Check browser console (F12) and Spring Boot terminal logs |
| Can't login with demo accounts | App may have restarted (H2 is in-memory, data reseeds on restart) |

---

## 📝 Database Schema (Auto-created by Hibernate)

```
users        → id, username, email, password, fullName, bio, profilePicture, location, website, joinedAt
posts        → id, content, imageUrl, createdAt, updatedAt, user_id (FK → users)
comments     → id, content, createdAt, user_id (FK), post_id (FK)
user_follows → follower_id (FK → users), following_id (FK → users)
post_likes   → user_id (FK → users), post_id (FK → posts)
```

---

## 👨‍💻 Author
Built for College Project — Full Stack Java Social Media Application  
Stack: Spring Boot 3.2 · Spring Security · JPA · H2/MySQL · Thymeleaf
