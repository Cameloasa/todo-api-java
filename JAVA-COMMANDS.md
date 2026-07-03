# Overview

This document contains all essential Maven commands and project workflow steps used across all Java projects in this organization.
It applies to both new projects and legacy projects being upgraded.

## 🧩 Maven Version & Validation

Check Maven version. Confirms Maven is installed and shows the Java version Maven uses.

```bash
mvn -version
```

Validate project structure. Ensures the project and pom.xml are correctly configured.

```bash
mvn validate
```

## 🔧 Build & Compile

Compile the project. Clean and rebuild everything

```bash
mvn compile
```

```bash
mvn clean install
```

Removes old build files and installs dependencies again.
Use this after updating pom.xml, Java version, or project structure.

## 🧪 Testing (JUnit 5)

Run all tests

```bash
mvn test
```

Run tests after cleaning

```bash
mvn clean test
```

## 🎨 Code Formatting (Spotless)

```bash
mvn spotless:apply
```

## 🛡️ Code Quality & Linting (Checkstyle)

```bash
mvn checkstyle:check
```

## ▶️ Run Application (Exec Plugin)

Requires the exec-maven-plugin configured in pom.xml.Run the main application

```bash
mvn exec:java
```
