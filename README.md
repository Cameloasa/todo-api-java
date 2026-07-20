# Todo API

This project is a complete Java Spring Boot backend for managing users, authentication sessions, personal profiles, to‑do items, and subtasks.
It demonstrates clean layered architecture, DTO‑based request/response mapping, MySQL relational modeling, and a fully functional REST API built with Spring Boot.

Originally part of an older Java/Javalin exercise, the project has now been fully modernized, migrated to Spring Boot, and extended with authentication, roles, email integration, validation, and enterprise‑grade structure. It is prepared for future improvements and migration into a dedicated GitHub organization.

## 🎯 Project Purpose

This project is a fully‑featured Java Spring Boot backend for managing users, authentication sessions, personal profiles, roles, and to‑do items.
It demonstrates clean layered architecture, enterprise‑grade REST API design, and modern backend development practices.

Originally part of a collection of older Java exercises, the project has now been rebuilt from scratch using Spring Boot, MySQL, DTO mapping, validation, converters, and service‑driven logic.
It is prepared for future improvements and migration into a dedicated GitHub organization.

The project focuses on practicing:

Layered architecture (Controller → Service → Repository → Entity → DTO → Converter)

REST API design (Auth, User, Person, Roles, Sessions)

Session‑based authentication using custom tokens

MySQL relational modeling

Entity relationships (User → Person → TodoItem → TodoItemTask)

DTO mapping & converters

Validation (Jakarta Validation)

Exception handling (global handler)

Email service integration

Security headers & CORS configuration

Clean code structure

JUnit testing

Code formatting & linting (Spotless + Checkstyle)

## 🌐 REST API Endpoints

🔹The REST API will be available at:
[http://localhost:8080]

🔹Swagger:
[http://localhost:8080/swagger-ui.html]

## 🧪 Running the Project

Compile:

```bash
mvn compile
```

Run:

```bash
mvn spring-boot:run
```

Run tests:

```bash
mvn test
```

Full rebuild:

```bash
mvn clean install
```

## 🎨 Code Formatting & Linting

Format code (Spotless):

```bash
mvn spotless:apply
```

Check code style (Checkstyle):

```bash
mvn checkstyle:check
```

## 🚀 Future Improvements

- Role & permission management ✔
- Session-based authentication enhancements
- Global exception handling (ControllerAdvice) ✔
- Security headers & CORS global configuration
- Email confirmation flow ✔
- Password hashing (BCrypt) ✔
- Docker Compose (backend + MySQL + email service)
- Unit & integration testing (JUnit + Mockito)
- React + TypeScript frontend
- CI/CD pipeline with GitHub Actions
- Environment variable support (.env, .env.development, .env.example) ✔
