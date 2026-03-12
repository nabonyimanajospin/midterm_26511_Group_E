# Class Diagram Description - CARCONNECT

## Purpose
This class diagram explains the object-oriented structure of my CARCONNECT project. It shows the Java classes, enums, and relationships that map directly to the database and API behavior.

## Entities and Enums Shown
**Entities:**
- User
- UserProfile
- Location
- Car
- Category
- Inquiry

**Enums:**
- UserType (BUYER, SELLER, BOTH)
- LocationType (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)
- CarStatus (AVAILABLE, SOLD, RESERVED)
- InquiryStatus (PENDING, RESPONDED, CLOSED)

## Relationships Illustrated
- **User -> Location (many-to-one):**
  Each user is linked to one village. Many users can share the same village.

- **Location -> Location (self-reference):**
  The hierarchy is built using a parent-child link inside the same class.

- **User -> UserProfile (one-to-one):**
  A user can have one profile with extra details.

- **User -> Car (one-to-many):**
  A seller can own multiple cars.

- **Car <-> Category (many-to-many):**
  A car can have multiple categories, and a category can belong to multiple cars.

- **Car -> Inquiry (one-to-many):**
  A car can receive multiple inquiries.

- **User -> Inquiry (one-to-many):**
  A user can create multiple inquiries.

## Why This Diagram Matters
The class diagram shows how my Java classes map to the ERD and how the relationships are implemented using JPA annotations such as `@ManyToOne`, `@OneToMany`, `@ManyToMany`, and `@OneToOne`.

## How to Use This Diagram
The diagram generated from `class-diagram.puml` is included for exam presentation and GitHub submission.
