<div align="center">
 
# 🚗 CARCONNECT
 
### Online Car Marketplace Backend — Rwanda
 
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
 
![Web Technology](https://img.shields.io/badge/Course-Web%20Technology%20%26%20Internet-0078D4?style=for-the-badge&logo=internetexplorer&logoColor=white)
![University](https://img.shields.io/badge/University-AUCA-8B0000?style=for-the-badge&logo=academia&logoColor=white)
![Status](https://img.shields.io/badge/Status-Midterm%20Exam-success?style=for-the-badge)
 
---
 
**Practical Examination · Database & Spring Boot Application Development**
 
| Field | Details |
|---|---|
| Student | Jospin Nabonyimana |
| Student ID | 26511 |
| Lecturer | Mr. Patrick Dushimimana |
| Course | Web Technology and Internet |
| University | Adventist University of Central Africa |
| Date | March 12, 2026 |
 
</div>
 
---
 
## 📋 Table of Contents
 
- [Project Overview](#-project-overview)
- [Rubric Mapping](#-rubric-mapping)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Database Schema](#-database-schema)
- [Location Hierarchy](#-location-hierarchy)
- [API Endpoints](#-api-endpoints)
- [Pagination & Sorting](#-pagination--sorting)
- [Running the Project](#-running-the-project)
- [Postman Data Flow](#-postman-data-flow)
- [Validation & Error Handling](#-validation--error-handling)
- [Evidence & Screenshots](#-evidence--screenshots)
- [Diagrams](#-diagrams)
 
---
 
## 📌 Project Overview
 
**CARCONNECT** is a RESTful backend API built with Spring Boot for an online car marketplace in Rwanda. It focuses on:
 
-  Correct relational database design with JPA/Hibernate
-  A self-referencing location hierarchy (Province → Village)
-  Clean, layered REST API architecture
-  Pagination, sorting, and custom JPQL queries
 
---
 
## 🎯 Rubric Mapping
 
| # | Requirement | Implementation |
|---|---|---|
| 1 | **ERD with at least 5 tables** | 7 tables with clear relationships |
| 2 | **Saving Location** | Single-table hierarchy via `parent_id` |
| 3 | **Sorting and Pagination** | `Pageable` and `Sort` used in multiple endpoints |
| 4 | **Many-to-Many** | `Car` ↔ `Category` via `car_categories` junction table |
| 5 | **One-to-Many** | `User` → `Car`, `User` → `Inquiry`, `Car` → `Inquiry` |
| 6 | **One-to-One** | `User` → `UserProfile` |
| 7 | **`existsBy()`** | Email, location code, and plate number checks |
| 8 | **Users by Province** | JPQL query traversing the full location hierarchy |
 
---
 
## 🛠 Technology Stack
 
| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Core language |
| Spring Boot | 4.0.3 | Application framework |
| PostgreSQL | Latest | Relational database |
| Spring Data JPA / Hibernate | — | ORM & database access |
| Jakarta Validation | — | Input validation |
| Maven | — | Build & dependency management |
 
---
 
## 🏗 Architecture
 
This project follows the standard **Controller → Service → Repository** layered pattern:
 
```
HTTP Request
     │
     ▼
┌─────────────┐
│  Controller  │  ← Handles HTTP requests & responses
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Service    │  ← Business logic, validation & mapping
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Repository  │  ← Spring Data JPA (database access)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Database   │  ← PostgreSQL
└─────────────┘
```
 
**Example flow:**
 
```
POST /api/cars → CarController → CarService.create() → CarRepository.save() → JSON response
```
 
---
 
## 🗄 Database Schema
 
### Tables
 
| Table | Description |
|---|---|
| `locations` | Self-referencing location hierarchy |
| `users` | Registered users |
| `user_profiles` | Extended profile info (One-to-One with `users`) |
| `cars` | Car listings |
| `categories` | Car categories |
| `car_categories` | Many-to-Many junction table (`cars` ↔ `categories`) |
| `inquiries` | Buyer inquiries on car listings |
 
### Key Relationships
 
```
locations.parent_id      → locations.id          (self-referencing)
users.location_id        → locations.id
user_profiles.user_id    → users.id              (one-to-one)
cars.owner_id            → users.id
car_categories.car_id    → cars.id
car_categories.category_id → categories.id
inquiries.car_id         → cars.id
inquiries.inquirer_id    → users.id
```
 
---
 
## 🗺 Location Hierarchy
 
All Rwanda administrative levels are stored in a **single `locations` table** using a `parent_id` self-reference:
 
```
Province
  └── District
        └── Sector
              └── Cell
                    └── Village  ← assigned to Users
```
 
When a user is created, a **Village** is assigned as their location. The full chain (Village → Cell → Sector → District → Province) is derived from the `parent_id` links automatically.
 
---
 
## 📡 API Endpoints
 
### 📍 Locations
 
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/locations` | Create a location |
| `GET` | `/api/locations` | List all locations |
| `GET` | `/api/locations/{id}` | Get location by ID |
| `GET` | `/api/locations/type/{type}` | Filter by type |
| `GET` | `/api/locations/provinces` | List all provinces |
| `GET` | `/api/locations/parent/{parentId}` | Get children of a location |
| `GET` | `/api/locations/exists/{code}` | Check if location code exists |
| `DELETE` | `/api/locations/{id}` | Delete a location |
 
### 👤 Users
 
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users` | Create a user |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Get user by ID |
| `PUT` | `/api/users/{id}` | Full update |
| `PATCH` | `/api/users/{id}` | Partial update |
| `DELETE` | `/api/users/{id}` | Delete user |
| `GET` | `/api/users/exists/email/{email}` | Check if email exists |
| `GET` | `/api/users/by-province?province=P01` | Users by province code |
| `GET` | `/api/users/by-province?province=Kigali%20City` | Users by province name |
| `POST` | `/api/users/{userId}/location/{locationId}` | Assign location to user |
 
### 🏷 Categories
 
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/categories` | Create a category |
| `GET` | `/api/categories` | List all categories |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `PUT` | `/api/categories/{id}` | Update category |
| `DELETE` | `/api/categories/{id}` | Delete category |
 
### 🚗 Cars
 
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/cars` | Create a car listing |
| `GET` | `/api/cars` | List all cars (supports pagination & sorting) |
| `GET` | `/api/cars/{id}` | Get car by ID |
| `GET` | `/api/cars/plate/{plateNumber}` | Find by plate number |
| `GET` | `/api/cars/owner/{ownerId}` | Cars by owner |
| `PUT` | `/api/cars/{id}` | Full update |
| `PATCH` | `/api/cars/{id}` | Partial update |
| `DELETE` | `/api/cars/{id}` | Delete car |
 
### 💬 Inquiries
 
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/inquiries` | Create an inquiry |
| `GET` | `/api/inquiries` | List all inquiries |
| `GET` | `/api/inquiries/{id}` | Get inquiry by ID |
| `PUT` | `/api/inquiries/{id}` | Full update |
| `PATCH` | `/api/inquiries/{id}` | Partial update |
| `DELETE` | `/api/inquiries/{id}` | Delete inquiry |
 
---
 
## 📄 Pagination & Sorting
 
All list endpoints support pagination and sorting via query parameters:
 
```
GET /api/cars?page=0&size=10&sortBy=price&direction=asc
```
 
| Parameter | Description | Example |
|---|---|---|
| `page` | Page number (zero-based) | `0` |
| `size` | Items per page | `10` |
| `sortBy` | Field to sort by | `price`, `createdAt` |
| `direction` | Sort direction | `asc` or `desc` |
 
**Sample paginated response:**
 
```json
{
  "content": [ ... ],
  "totalElements": 42,
  "totalPages": 5,
  "numberOfElements": 10,
  "page": 0,
  "size": 10
}
```
 
---
 
## ▶ Running the Project
 
### 1. Create the Database
 
```sql
CREATE DATABASE carconnect_new_db;
```
 
### 2. Configure `application.properties`
 
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/carconnect_new_db
spring.datasource.username=postgres
spring.datasource.password=jospin123
spring.jpa.hibernate.ddl-auto=update
```
 
### 3. Run
 
```bash
mvn clean spring-boot:run
```
 
---
 
## 🧪 Postman Data Flow
 
Create resources in this exact order, as each step depends on the previous:
 
```
1. POST /api/locations   → Create Provinces   (type=PROVINCE, parentId=null)
2. POST /api/locations   → Create Districts   (parentId = province UUID)
3. POST /api/locations   → Create Sectors     (parentId = district UUID)
4. POST /api/locations   → Create Cells       (parentId = sector UUID)
5. POST /api/locations   → Create Villages    (parentId = cell UUID)
6. POST /api/users       → Create Users       (locationId = village UUID)
7. POST /api/categories  → Create Categories
8. POST /api/cars        → Create Cars        (ownerId + categoryIds)
9. POST /api/inquiries   → Create Inquiries   (carId + inquirerId)
```
 
---
 
## 🛡 Validation & Error Handling
 
- Input validated using **Jakarta Validation** annotations:
  - `@NotBlank`, `@NotNull`, `@Email`, `@Size`, etc.
- Custom exceptions for business logic errors (e.g., duplicate plate number, location not found)
- Centralized error responses via **`GlobalExceptionHandler`** (`@ControllerAdvice`)
 
---
 
## 📸 Evidence & Screenshots
 
> 📌 **Instructions:** For each section below, take a screenshot from Postman or your database tool and replace the placeholder text with:
> `![description](screenshots/filename.png)`
> Make sure to create a `screenshots/` folder in your repo and upload all images there.
 
---
 
### 1️⃣ Location Hierarchy — Provinces to Villages
 
> *Screenshot showing Postman POST requests for creating Provinces, Districts, Sectors, Cells, and Villages. Show the request body and the success response.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![locations-hierarchy](screenshots/locations-hierarchy.png)
```
 
---
 
### 2️⃣ User Response with Full Location Chain
 
> *Screenshot showing a GET /api/users/{id} response where the user's location displays the full chain: Village → Cell → Sector → District → Province.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![user-full-location](screenshots/user-full-location.png)
```
 
---
 
### 3️⃣ Car Response with Owner and Full Location
 
> *Screenshot showing a GET /api/cars/{id} response including the car owner's details and their full location hierarchy.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![car-full-response](screenshots/car-full-response.png)
```
 
---
 
### 4️⃣ Pagination Output
 
> *Screenshot showing GET /api/cars?page=0&size=10&sortBy=price&direction=asc with a paginated JSON response including `totalElements`, `totalPages`, and `numberOfElements`.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![pagination](screenshots/pagination.png)
```
 
---
 
### 5️⃣ `existsBy` Demonstration
 
> *Screenshot showing the existsBy endpoints in action — for example GET /api/users/exists/email/{email} and GET /api/locations/exists/{code} returning `true` or `false`.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![exists-by](screenshots/exists-by.png)
```
 
---
 
### 6️⃣ Users by Province — Code and Name
 
> *Screenshot showing two requests: one using province code (e.g. `?province=P01`) and one using province name (e.g. `?province=Kigali City`), both returning matching users.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![users-by-province](screenshots/users-by-province.png)
```
 
---
 
### 7️⃣ Database Tables (pgAdmin or psql)
 
> *Screenshot from pgAdmin or your PostgreSQL client showing the created tables: `locations`, `users`, `user_profiles`, `cars`, `categories`, `car_categories`, `inquiries`.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![database-tables](screenshots/database-tables.png)
```
 
---
 
### 8️⃣ Many-to-Many — Car with Multiple Categories
 
> *Screenshot showing a POST /api/cars request body with multiple `categoryIds`, and the response showing the car linked to multiple categories.*
 
```
[ 📷 PASTE SCREENSHOT HERE ]
Replace this block with: ![many-to-many](screenshots/many-to-many.png)
```
 
---
 
## 📐 Diagrams
 
PlantUML diagrams are included in the `/diagrams` directory:
 
| File | Description |
|---|---|
| `diagrams/erd-diagram.puml` | Entity Relationship Diagram |
| `diagrams/class-diagram.puml` | Class Diagram |
 
To render them, use the [PlantUML online editor](https://www.plantuml.com/plantuml/uml/) or the PlantUML VS Code extension.
 
---
 
<div align="center">
 
Made with ❤️ by **Jospin Nabonyimana** · Student ID: 26511
 
*Adventist University of Central Africa · Web Technology and Internet*
 
</div>
