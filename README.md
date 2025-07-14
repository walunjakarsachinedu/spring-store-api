# Spring Store API

A Spring Boot REST API built while following the *Spring Boot: Mastering REST API Development* course by Mosh Hamedani.

## Features

- RESTful API design with Spring Boot
- CRUD operations for Products, Categories, Users
- Data validation using Spring Validation
- Global exception handling
- DTO mapping with MapStruct
- Persistence using Spring Data JPA
- Custom middleware for logging
- Secure endpoints with Spring Security & JWT
- Swagger (OpenAPI) documentation
- Role-based access control (Admin/User)


## Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security, JPA, Validation
- MapStruct
- MySQL
- JWT
- Swagger

## Running the App

```bash
./mvnw spring-boot:run
```

Or use your IDE to run SpringStoreApiApplication.

## Code Organisation

This project uses a feature-first approach to package organization:

> Organize by feature first, then by layer—only when a layer has multiple elements.
> 
> e.g., For example, in the auth feature, multiple DTOs (like LoginRequest, JwtResponse) are grouped under:
> ```java
> package com.codewithmosh.store.auth.dtos;
> ```


## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

## Notes

For detailed implementation notes and explanations, refer to my course notes [here](https://lumbar-clownfish-4a1.notion.site/springboot-9b78b306ca18426182acbf310dc04d27?source=copy_link).

