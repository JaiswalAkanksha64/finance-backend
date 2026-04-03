# Finance Backend API

## Overview
A backend system for a Finance Dashboard built with
Spring Boot and MySQL. The system supports three user
roles (VIEWER, ANALYST, ADMIN) with different permission
levels. It manages financial records, calculates income
and expense summaries, and exposes secure REST APIs
protected with JWT authentication.

## Tech Stack
- Java 17
- Spring Boot 4.0.5
- MySQL 8
- Spring Security + JWT
- Swagger UI (API Documentation)
- Maven

## Project Structure

src/main/java/com/zorvyn/finance/backend/

├── controller/   → API endpoints    
├── service/      → Business logic   
├── repository/   → Database operations   
├── model/        → Database entities   
├── dto/          → Data transfer objects   
├── security/     → JWT + Spring Security   
├── exception/    → Global error handling   
└── enums/        → Role, TransactionType, Category

## Setup Instructions

### Prerequisites
- Java 17+
- MySQL 8+
- Maven

### Steps

**1. Clone the repository**

git clone https://github.com/JaiswalAkanksha64/finance-backend
cd finance-backend

**2. Create MySQL database**
```sql
CREATE DATABASE finance_db;
```

**3. Configure application properties**
cp src/main/resources/application.properties.example
src/main/resources/application.properties

Edit application.properties with your MySQL credentials.

**4. Run the application**

./mvnw spring-boot:run

**5. Access Swagger UI**

http://localhost:8080/swagger-ui/index.html

## Roles and Permissions

| Action | VIEWER | ANALYST | ADMIN |
|--------|--------|---------|-------|
| View records | ✅ | ✅ | ✅ |
| Create records | ❌ | ✅ | ✅ |
| Update records | ❌ | ✅ | ✅ |
| Delete records | ❌ | ❌ | ✅ |
| View dashboard | ✅ | ✅ | ✅ |
| Manage users | ❌ | ❌ | ✅ |

## API Endpoints

### Authentication
| Method | URL | Access |
|--------|-----|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |

### Financial Records
| Method | URL | Access |
|--------|-----|--------|
| POST | /api/records | ANALYST, ADMIN |
| GET | /api/records | ALL |
| GET | /api/records/{id} | ALL |
| PUT | /api/records/{id} | ANALYST, ADMIN |
| DELETE | /api/records/{id} | ADMIN |

### Users
| Method | URL | Access |
|--------|-----|--------|
| GET | /api/users | ADMIN |
| GET | /api/users/{id} | ADMIN |
| PUT | /api/users/{id} | ADMIN |
| DELETE | /api/users/{id} | ADMIN |

### Dashboard
| Method | URL | Access |
|--------|-----|--------|
| GET | /api/dashboard/summary | ALL |
| GET | /api/dashboard/category-totals | ALL |
| GET | /api/dashboard/monthly-trends | ALL |
| GET | /api/dashboard/recent-activity | ALL |

## Filtering and Pagination

GET /api/records?type=EXPENSE

GET /api/records?category=FOOD

GET /api/records?startDate=2026-01-01&endDate=2026-03-31

GET /api/records?page=0&size=10

## Design Decisions

**1. JWT over Sessions**
JWT is stateless meaning the server stores nothing.
Every request carries the token which contains the
user's email and role. This scales better and suits
REST APIs perfectly.

**2. Enum for Category and Type**
Categories and types are fixed enums instead of free
text. This ensures data consistency across all records
and prevents issues like "food" vs "Food" vs "groceries"
which would break dashboard aggregations.

**3. BigDecimal for Amount**
Financial calculations require exact precision.
Double and float have floating point errors which
are unacceptable in any financial system.

**4. Two Tables Only**
The system requirements are cleanly served by users
and financial_records tables with a foreign key
relationship. Simple and sufficient for the use case.

**5. Layered Architecture**
Controller → Service → Repository pattern ensures
clean separation of concerns. Each layer has one
responsibility making code readable and maintainable.

## Assumptions Made
- A user can only have one role at a time
- Categories are predefined and not user defined
- All monetary values are in INR
- Admin users are created via register API

## Known Limitations and Future Improvements
- Refresh token implementation for better security
- Soft delete functionality for records
- Combined filtering (type AND category together)
- Rate limiting on authentication endpoints
- More granular permissions per user

## Error Responses
| Status Code | Meaning |
|-------------|---------|
| 200 | Success |
| 201 | Created successfully |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |



