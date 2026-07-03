# ToDo App – Java JDBC + Javalin REST API

This project is a complete Java application for managing to‑do items and subtasks.
It demonstrates clean object‑oriented design, layered architecture, and a fully functional REST API built with Javalin.

Originally part of a collection of older Java exercises, the project has now been modernized and prepared for future improvements and migration into a dedicated GitHub organization.

## 🎯 Project Purpose

This project focuses on practicing:

- Object‑oriented programming

- Class relationships (Person → TodoItem → TodoItemTask)

- Encapsulation and validation

- Working with dates (LocalDate)

- Basic business logic

- Clean code structure

- JUnit 5 testing

- Code formatting and linting (Spotless + Checkstyle)

## 📦 Current Features

**Person**.Represents a user with:

```json
id
first name
last name
email
```

**TodoItem**.Represents a task with:

```json
id
title
description
deadline (LocalDate)
done status
method isOverdue()
```

**TodoItemTask**.Represents the assignment of a TodoItem to a Person:

```json
id
assigned status
assignee (Person)
todo item (TodoItem)
```

## 📁 Project Structure

```text
errors/
img/
scripts/
src/
  main/
    java/
      dev/cameloasa/
        App.java
        WebApp.java
        db/
          schema.sql
          DatabaseInitializer.java
        dao/
          Person.java
          TodoItem.java
          TodoItemTask.java 
        daoimpl/
          Person.java
          TodoItem.java
          TodoItemTask.java 
        service/
          Person.java
          TodoItem.java
          TodoItemTask.java 
        controller/
          Person.java
          TodoItem.java
          TodoItemTask.java 
        web/
          Person.java
          TodoItem.java
          TodoItemTask.java 
  test/
    java/
      dev/cameloasa/
```

## 🌐 REST API Endpoints

```text
👤 Person — endpointuri REST
🔹 CRUD
GET /person — retun a list with people
GET /person/{id} — 
POST /person — create a new person
PATCH /person/{id} — 
DELETE /person/{id} — delete person

📝 TodoItem — endpointuri REST
🔹 CRUD
GET /items — returnează toate item‑urile
GET /items/{id} — returnează item‑ul cu ID‑ul dat
POST /items — creează un item nou
PATCH /items/{id} — update parțial pe item
DELETE /items/{id} — șterge item‑ul

🔹 Search
GET /items/done/{status} — item‑uri finalizate / nefinalizate
GET /items/unassigned — item‑uri fără assignee
GET /items/overdue — item‑uri cu deadline depășit
GET /items/deadline/{date} — item‑uri cu deadline exact
GET /items/title/{title} — item‑uri filtrate după titlu

🧩 TodoItemTask — endpointuri REST
🔹 CRUD
GET /tasks — returnează toate task‑urile
GET /tasks/{id} — returnează task‑ul cu ID‑ul dat
POST /tasks — creează un task nou
PATCH /tasks/{id} — update parțial pe task
DELETE /tasks/{id} — șterge task‑ul

🔹 Search
GET /tasks/todoitem/{todoItemId} — task‑uri ale unui item
GET /tasks/done/{status} — task‑uri finalizate / nefinalizate
GET /tasks/title/{title} — task‑uri filtrate după titlu
```

🔹The REST API will be available at:
[http://localhost:7000]

🔹Swagger:
[http://localhost:7000/swagger.html]

## 🧪 Running the Project

Compile:

```bash
mvn compile
```

Run:

```bash
mvn exec:java
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

Migration to Spring Boot

Hot reload with DevTools

Validation with Jakarta Validation

Automatic mapping with MapStruct

React + TypeScript frontend

Docker support
