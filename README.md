# CARCONNECT - Midterm Practical Exam Backend (Spring Boot)

**Practical Examination:** Database & Spring Boot Application Development
**Course:** Web Technology and Internet
**University:** Adventist University of Central Africa
**Lecturer:** Mr. Patrick Dushimimana
**Student Name:** Jospin Nabonyimana
**Student ID:** 26511
**Date:** February 20, 2026

---

## Project Overview
I built CARCONNECT as a Spring Boot backend for an online car marketplace in Rwanda. The project focuses on correct database relationships, a self-referencing location hierarchy, clean REST APIs, and clear evidence of pagination, sorting, and query logic required by the exam rubric.

---

## What This Project Demonstrates (Rubric Mapping)
1. **ERD with at least 5 tables**: I implemented 7 tables with clear relationships.
2. **Saving Location**: One-table location hierarchy using `parent_id`.
3. **Sorting and Pagination**: `Pageable` and `Sort` used in multiple endpoints.
4. **Many-to-Many**: `Car` <-> `Category` via `car_categories`.
5. **One-to-Many**: `User` -> `Car`, `User` -> `Inquiry`, `Car` -> `Inquiry`.
6. **One-to-One**: `User` -> `UserProfile`.
7. **existsBy()**: Exists checks for email, location code, plate number.
8. **Users by Province Code or Name**: JPQL query traverses location hierarchy.

---

## Technology Stack
- **Java:** 21
- **Spring Boot:** 4.0.3
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven

---

## Architecture and Flow (Controller -> Service -> Repository)
- **Controller:** Handles HTTP requests and responses.
- **Service:** Business logic, validation, and mapping.
- **Repository:** Database access using Spring Data JPA.
- **Model:** Entity classes that map to tables.

**Example flow:**
`POST /api/cars` -> `CarController` -> `CarService.create()` -> `CarRepository.save()` -> JSON response.

---

## Database Schema Summary
**Tables:**
- `locations` (self-referencing hierarchy)
- `users`
- `user_profiles`
- `cars`
- `categories`
- `car_categories` (junction table)
- `inquiries`

**Key Relationships:**
- `locations.parent_id` -> `locations.id` (self-referencing)
- `users.location_id` -> `locations.id`
- `user_profiles.user_id` -> `users.id` (one-to-one)
- `cars.owner_id` -> `users.id`
- `car_categories.car_id` -> `cars.id`
- `car_categories.category_id` -> `categories.id`
- `inquiries.car_id` -> `cars.id`
- `inquiries.inquirer_id` -> `users.id`

---

## Location Hierarchy (Single Table)
I store all Rwanda administrative levels in one `locations` table using a parent chain:
Province -> District -> Sector -> Cell -> Village

When I create a user, I assign a **Village** as the location. The system derives the full chain from the parent links.

---

## Running the Project
1. Create a database in PostgreSQL:
   `CREATE DATABASE carconnect_final_db;`
2. Update `src/main/resources/application.properties`:
   - `spring.datasource.url`
   - `spring.datasource.username`
   - `spring.datasource.password`
3. Run:
   - `mvn clean spring-boot:run`

---

## Postman Data Flow (Required Order)
1. Create Provinces (type=PROVINCE, parentId=null)
2. Create Districts (parentId = province UUID)
3. Create Sectors (parentId = district UUID)
4. Create Cells (parentId = sector UUID)
5. Create Villages (parentId = cell UUID)
6. Create Users (locationId = village UUID)
7. Create Categories
8. Create Cars (ownerId + categoryIds)
9. Create Inquiries (carId + inquirerId)

---

## API Endpoints
**Locations**
- `POST /api/locations`
- `GET /api/locations`
- `GET /api/locations/{id}`
- `GET /api/locations/type/{type}`
- `GET /api/locations/provinces`
- `GET /api/locations/parent/{parentId}`
- `GET /api/locations/exists/{code}`
- `DELETE /api/locations/{id}`

**Users**
- `POST /api/users`
- `GET /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `PATCH /api/users/{id}`
- `DELETE /api/users/{id}`
- `GET /api/users/exists/email/{email}`
- `GET /api/users/by-province?province=P01`
- `GET /api/users/by-province?province=Kigali%20City`
- `POST /api/users/{userId}/location/{locationId}`

**Categories**
- `POST /api/categories`
- `GET /api/categories`
- `GET /api/categories/{id}`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

**Cars**
- `POST /api/cars`
- `GET /api/cars`
- `GET /api/cars/{id}`
- `GET /api/cars/plate/{plateNumber}`
- `GET /api/cars/owner/{ownerId}`
- `PUT /api/cars/{id}`
- `PATCH /api/cars/{id}`
- `DELETE /api/cars/{id}`

**Inquiries**
- `POST /api/inquiries`
- `GET /api/inquiries`
- `GET /api/inquiries/{id}`
- `PUT /api/inquiries/{id}`
- `PATCH /api/inquiries/{id}`
- `DELETE /api/inquiries/{id}`

---

## Pagination and Sorting
Example request:
`GET /api/cars?page=0&size=10&sortBy=price&direction=asc`

This uses Spring Data `PageRequest` and `Sort` to return results with metadata such as `totalElements`, `totalPages`, and `numberOfElements`.

---

## Evidence and Screenshots
Replace the placeholders below with your screenshots.

- **Locations hierarchy posted (Provinces -> Villages)**
  [Screenshot: locations-hierarchy.png]

- **User response with full location chain**
  [Screenshot: user-full-location.png]

- **Car response with owner + full location**
  [Screenshot: car-full-response.png]

- **Pagination output**
  [Screenshot: pagination.png]

- **existsBy demonstration**
  [Screenshot: exists-by.png]

- **Users by province (code and name)**
  [Screenshot: users-by-province.png]

---

## Diagrams (PlantUML)
I generated PlantUML files for ERD and Class Diagram:
- `erd-diagram.puml`
- `class-diagram.puml`

---

## Validation and Error Handling
- Validation uses Jakarta Validation annotations (e.g., `@NotBlank`, `@NotNull`).
- Errors are handled through custom exceptions and `GlobalExceptionHandler`.

- Project runs with `mvn clean spring-boot:run`
---
