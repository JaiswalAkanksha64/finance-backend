# Finance Dashboard Backend

A secure REST API backend for a Finance Dashboard application built with Java Spring Boot, featuring JWT authentication and role-based access control.

## Tech Stack

**Backend:** Java, Spring Boot, Spring Security, JWT, MySQL, Maven  
**Tools:** Postman, Git

## Features

- JWT-based stateless authentication
- Role-based access control (Admin, User)
- Layered REST API architecture for financial data management
- Multi-role financial operations support
- BCrypt password encryption
- MySQL database integration with Spring Data JPA

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | No | Register user |
| POST | `/api/auth/login` | No | Login & get token |
| GET | `/api/dashboard` | Yes | Get dashboard data |
| GET | `/api/transactions` | Yes | Get transactions |

## Setup

1. Create MySQL database
2. Update `application.properties` with DB credentials
3. Run: `mvn spring-boot:run`
4. API runs on `http://localhost:8080`

## Security
- JWT stateless authentication
- BCrypt password encryption  
- Role-based access control
