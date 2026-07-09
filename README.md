# 📝 Spring Boot Blog CMS REST API

<div align="center">

*A RESTful Blog Content Management System (CMS) built with Spring Boot, following layered architecture, clean code principles, and modern backend development practices.*

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge\&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge\&logo=postgresql)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge\&logo=hibernate)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge\&logo=apachemaven)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge\&logo=swagger)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge\&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge)

</div>

---

# 📌 Project Overview

Spring Boot Blog CMS is a RESTful backend application developed to manage blog content efficiently.

The project follows a layered architecture and demonstrates modern backend development concepts such as DTO mapping, validation, exception handling, pagination, specifications, unit testing, and API documentation.

The application exposes REST APIs for managing:

* Blog posts
* Categories
* Comments
* Tags
* Search operations
* Statistics

The project also includes comprehensive unit tests and interactive API documentation using Swagger UI.

---

# 🚀 Features

## 📝 Post Management

* Create a new post
* Update existing posts
* Delete posts
* Retrieve all posts
* Retrieve a post by ID
* Publish posts
* Save posts as draft
* Retrieve latest posts

---

## 📂 Category Management

* Create category
* Update category
* Delete category
* Retrieve all categories
* Retrieve category by ID

---

## 💬 Comment Management

* Add comments to posts
* Retrieve all comments belonging to a post

---

## 🏷️ Tag Management

* Add tags to posts
* Remove tags
* Prevent duplicate tags

---

## 🔍 Search

Dynamic searching using Spring Data JPA Specifications.

Users can search posts based on multiple criteria.

---

## 📄 Pagination

Retrieve posts with paging support.

---

## 📊 Statistics

The API provides statistical endpoints for:

* Categories
* Authors
* Post Status

---

# 📑 API Documentation

Swagger UI is integrated into the project for interactive API documentation.

After running the application, documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```

Users can:

* View all available endpoints
* Inspect request/response models
* Execute API requests directly from the browser
* Test endpoints without using Postman

---

# 📷 Swagger Preview

### Swagger Endpoint List

<img src="images/swagger-home.png" width="400"/>

---

# 🛠 Technology Stack

## Backend

```text
Java 17
Spring Boot
Spring MVC
Spring Data JPA
Hibernate
RESTful API
Jakarta Bean Validation
```

## Database

```text
PostgreSQL
pgAdmin
```

## Documentation

```text
Swagger / OpenAPI
```

## Testing

```text
JUnit 5
Mockito
JaCoCo
```

## Utilities

```text
MapStruct
Lombok
Maven
```

---

# 🏗 Project Architecture

```
src
└── main
    ├── java
    │   └── com.oyku.blog
    │       ├── config
    │       ├── controller
    │       ├── dto
    │       │     ├── request
    │       │     └── response
    │       ├── entity
    │       ├── enums
    │       ├── exception
    │       ├── mapper
    │       ├── model
    │       ├── repository
    │       ├── service
    │       │      └── impl
    │       ├── specification
    │       └── BlogApplication.java
    │
    └── resources
          └── application.properties
```

---

# 🔄 Request Flow

```
Client

↓

Controller

↓

Service

↓

Repository

↓

PostgreSQL Database

↓

Response DTO
```

---

# 📦 API Endpoints

## Posts

| Method | Endpoint             | Description     |
| ------ | -------------------- | --------------- |
| POST   | /posts               | Create Post     |
| GET    | /posts               | Get All Posts   |
| GET    | /posts/{id}          | Get Post By Id  |
| PUT    | /posts/{id}          | Update Post     |
| DELETE | /posts/{id}          | Delete Post     |
| PATCH  | /posts/{id}/publish  | Publish Post    |
| PATCH  | /posts/{id}/draft    | Draft Post      |
| PATCH  | /posts/{id}/tags     | Add Tags        |
| DELETE | /posts/{id}/tags     | Remove Tags     |
| POST   | /posts/{id}/comments | Add Comment     |
| GET    | /posts/{id}/comments | Get Comments    |
| POST   | /posts/search        | Search Posts    |
| GET    | /posts/latest        | Latest Posts    |
| GET    | /posts/page          | Paginated Posts |

---

## Categories

| Method | Endpoint         | Description             |
| ------ | ---------------- | -----------             |
| POST   | /categories      | Create Category         |
| GET    | /categories      | Retrieve All Categories |
| GET    | /categories/{id} | Retrieve Category by ID |
| PUT    | /categories/{id} | Update Category         |
| DELETE | /categories/{id} | Delete Category         |

---

## Statistics

| Method | Endpoint               | Description                     |
| ------ | ---------------------- | -------------------------------
| GET    | /statistics/status     | Retrieve post status statistics |
| GET    | /statistics/authors    | Retrieve author statistics      |
| GET    | /statistics/categories | Retrieve category statistics    |

---

# 🧪 Unit Testing

The project includes service layer unit tests using:

* JUnit 5
* Mockito

Test coverage includes:

* CRUD operations
* Success scenarios
* Exception scenarios
* Search functionality
* Tag management
* Comment management
* Slug generation
* Edge cases
* Validation logic

Coverage reports are generated with JaCoCo.

---

# ⚙️ Installation

## Prerequisites

* Java 17
* Maven
* PostgreSQL
* pgAdmin (Optional)
* Eclipse IDE or IntelliJ IDEA

---

## Clone Repository

```bash
git clone https://github.com/OykuEyuboglu/spring-boot-blog-cms.git
```

---

## Navigate to Project

```bash
cd spring-boot-blog-cms
```

---

## Configure Database

Update your database configuration inside:

```
src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

---

## Install Dependencies

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

Application starts at:

```
http://localhost:8080
```

---

## Open Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

# 📚 Key Concepts Covered

This project demonstrates practical experience with:

* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* REST API Design
* Layered Architecture
* DTO Pattern
* MapStruct
* Exception Handling
* Validation
* PostgreSQL
* Swagger / OpenAPI
* Pagination
* Dynamic Search using Specifications
* Unit Testing with Mockito
* JaCoCo Code Coverage
* Clean Code Principles

---

# 👩‍💻 Author

**Dila Öykü Eyüboğlu**

Java & Spring Boot
