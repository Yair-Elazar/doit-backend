# DoIt - Backend (Spring Boot) 🔙

This is the backend service for the DoIt task management application.

## 🛠️ Technologies Used
- Java 17
- Spring Boot
- Spring Security (JWT-based)
- PostgreSQL
- Hibernate (JPA)
- Maven

## 📦 Features
- User registration and login with JWT
- Create / update / delete tasks
- Share tasks with other users
- Fetch own tasks + shared tasks
- RESTful API

## 🧪 Run Locally

1. Clone the repo:
   ```bash
   git clone https://github.com/Yair-Elazar/doit-backend.git
   cd doit-backend
   ```

2. Configure `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/doitdb
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   jwt.secret=your_secret
   ```

3. Run:
   ```bash
   mvn spring-boot:run
   ```

## 📁 API Endpoints

- `POST /api/users/register` – Register new user
- `POST /api/users/login` – Login
- `GET /api/tasks` – Get user's tasks (created + shared)
- `POST /api/tasks` – Add new task
- `PUT /api/tasks/{id}` – Update task
- `DELETE /api/tasks/{id}` – Delete task
- `POST /api/tasks/share` – Share task with users
- `GET /api/users/all` – Get list of all usernames

---
