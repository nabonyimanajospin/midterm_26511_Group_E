# ERD Description - CARCONNECT

## Purpose
This ERD explains the database structure of my CARCONNECT project and how each table is related. It demonstrates the relationships required in the rubric and shows how the single-table location hierarchy works.

## Tables Represented
- `locations`
- `users`
- `user_profiles`
- `cars`
- `categories`
- `car_categories`
- `inquiries`

## Key Relationships
- **locations -> locations (self-reference):**
  The `parent_id` column creates the hierarchy Province -> District -> Sector -> Cell -> Village.

- **users -> locations (many-to-one):**
  A user belongs to one village, but many users can share the same village.

- **users -> user_profiles (one-to-one):**
  Each user can have one profile. The `user_profiles.user_id` is unique.

- **cars -> users (many-to-one):**
  Each car has one owner, and one user can own many cars.

- **cars <-> categories (many-to-many):**
  Implemented through the `car_categories` join table.

- **inquiries -> cars (many-to-one):**
  Each inquiry is about a specific car.

- **inquiries -> users (many-to-one):**
  Each inquiry is created by a user (buyer).

## Why This Structure
- A single `locations` table makes hierarchy flexible and scalable.
- UUIDs provide strong uniqueness and prevent predictable IDs.
- The join table supports many-to-many without duplication.

## How to Use This ERD
The ERD image generated from `erd-diagram.puml` visually shows these tables and relationships for the exam presentation.
