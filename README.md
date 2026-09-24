# ProjectSaloon

A full-stack **multi-tenant salon booking and queue management SaaS** built with **Spring Boot and React**.

ProjectSaloon allows multiple salons to operate independently on the same platform. Each salon acts as a tenant with its own users, treatments, stylists, and bookings.

The project is designed as a production-oriented full-stack application with a focus on:

* Multi-tenant architecture
* JWT authentication
* Role-based authorization
* Salon and treatment management
* Stylist management
* Customer bookings
* Booking conflict detection
* Working-hour validation
* Redis integration
* Dockerized backend infrastructure
* React frontend integration

---

## Project Overview

ProjectSaloon follows a client-server architecture:

```text
                    ┌──────────────────────┐
                    │      React App       │
                    │   React 19 + Vite    │
                    └──────────┬───────────┘
                               │
                               │ HTTP / REST API
                               ▼
                    ┌──────────────────────┐
                    │   Spring Boot API    │
                    │   JWT + Spring Sec.   │
                    └──────────┬───────────┘
                               │
                 ┌─────────────┴─────────────┐
                 │                           │
                 ▼                           ▼
        ┌─────────────────┐         ┌─────────────────┐
        │     MySQL       │         │      Redis      │
        │   Persistent DB  │         │      Cache      │
        └─────────────────┘         └─────────────────┘
```

---

# Features

## Authentication & Authorization

* Owner registration
* Customer registration
* Login using email/password
* JWT-based authentication
* Stateless Spring Security configuration
* BCrypt password hashing
* Role-based authorization
* Protected API endpoints
* Authentication filter for JWT validation

### Roles

```text
OWNER
CUSTOMER
STYLIST
```

---

## Multi-Tenant Architecture

Each salon is represented as a `Tenant`.

A tenant owns its:

* Users
* Treatments
* Stylists
* Bookings

Tenant-aware queries prevent users from accessing resources belonging to another salon.

Example:

```text
Tenant A
 ├── Users
 ├── Treatments
 ├── Stylists
 └── Bookings

Tenant B
 ├── Users
 ├── Treatments
 ├── Stylists
 └── Bookings
```

The backend uses the authenticated user's tenant to scope protected operations.

---

# Booking System

Customers can book treatments with stylists.

The booking system includes:

* Treatment selection
* Stylist selection
* Start time
* Automatic end-time calculation
* Booking status
* Working-hours validation
* Booking conflict detection
* Customer/owner authorization

### Booking statuses

```text
PENDING
CONFIRMED
CANCELLED
```

The backend checks for conflicting bookings before creating a new booking.

---

# Salon Management

Salon owners can manage their salon's:

* Treatments
* Stylists
* Bookings

Treatments contain information such as:

* Name
* Description
* Duration
* Price

Stylists can have:

* Work start time
* Work end time
* Skills
* Associated user account
* Associated tenant
* Bookings

---

# Frontend

The frontend is built using:

* React
* Vite
* React Router
* Axios
* JavaScript

The frontend communicates with the Spring Boot backend through REST APIs.

### Current frontend flow

```text
HomePage
   │
   ├── GET /api/tenants
   │
   ▼
Salon cards
   │
   │ click salon
   ▼
/salon/:id
   │
   ├── GET salon details
   │
   └── GET treatments
             │
             ▼
       Service cards
             │
             ▼
       Booking form
```

---

# Frontend Pages

## HomePage

Route:

```text
/
```

Responsibilities:

* Display available salons
* Fetch salons from backend
* Search salons
* Display loading state
* Display error state
* Display empty search results

The frontend no longer relies on the original mock salon data for the salon listing.

---

## Salon Details

Route:

```text
/salon/:id
```

Responsibilities:

* Fetch selected salon
* Display salon information
* Fetch treatments
* Display available treatments
* Allow treatment selection
* Open booking form

---

# Frontend Components

```text
src/
├── components/
│   ├── BookingForm.jsx
│   ├── Login.jsx
│   ├── Navbar.jsx
│   ├── SalonCard.jsx
│   └── ServiceCard.jsx
│
├── pages/
│   ├── HomePage.jsx
│   └── SalonDetails.jsx
│
├── services/
│   └── api.js
│
├── css/
│   └── index.css
│
├── App.jsx
└── main.jsx
```

---

# Backend Architecture

The backend follows a layered Spring Boot architecture.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Security is handled separately through Spring Security and JWT components.

---

# Backend Structure

```text
SaloonBackend/
│
├── src/
│   ├── main/
│   │   ├── java/com/parth/saloonmanagement/
│   │   │
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── CorsConfig.java
│   │   │   └── RedisConfig.java
│   │   │
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── BookingController.java
│   │   │   ├── StylistController.java
│   │   │   ├── TenantController.java
│   │   │   └── TreatmentController.java
│   │   │
│   │   ├── dto/
│   │   │
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   ├── Tenant.java
│   │   │   ├── Treatment.java
│   │   │   ├── Stylist.java
│   │   │   ├── Booking.java
│   │   │   ├── Role.java
│   │   │   └── BookingStatus.java
│   │   │
│   │   ├── exception/
│   │   │
│   │   ├── repository/
│   │   │
│   │   ├── security/
│   │   │   ├── JwtService.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── UserDetailService.java
│   │   │
│   │   └── service/
│   │
│   └── test/
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── application.properties
```

---

# Database Model

The main entities are:

```text
Tenant
  │
  ├───────────────┐
  │               │
  ▼               ▼
User           Treatment
  │
  │
  ▼
Booking
  │
  ├──────────────► Stylist
  │
  └──────────────► Treatment
```

### Tenant

Represents a salon/business.

### User

Represents an authenticated user.

A user belongs to a tenant and has a role.

### Treatment

Represents a salon service.

Examples:

```text
Haircut
Hair Coloring
Beard Styling
Facial
Massage
```

### Stylist

Represents a salon stylist and their working schedule.

### Booking

Connects:

```text
Customer
   +
Treatment
   +
Stylist
   +
Tenant
   +
Time
   +
Status
```

---

# REST API

Base URL during local development:

```text
http://localhost:8000
```

---

## Authentication

### Owner Signup

```http
POST /api/auth/signup
```

Creates a new tenant and owner account.

---

### Login

```http
POST /api/auth/login
```

Returns a JWT authentication token.

---

### Customer Signup

```http
POST /api/auth/customer-signup
```

Registers a customer for a tenant.

---

# Tenant APIs

### Get All Salons

```http
GET /api/tenants
```

Returns salon summaries.

Example response structure:

```json
[
  {
    "id": 1,
    "name": "Example Salon",
    "address": "Chandigarh"
  }
]
```

---

### Get Salon Details

```http
GET /api/tenants/{tenantId}
```

Returns information about a specific salon.

---

### Get Salon Treatments

```http
GET /api/tenants/{tenantId}/treatments
```

Returns treatments belonging to the selected salon.

---

# Treatment APIs

### Get Treatments

```http
GET /api/treatments
```

### Create Treatment

```http
POST /api/treatments/create
```

### Update Treatment

```http
PUT /api/treatments/{id}
```

### Delete Treatment

```http
DELETE /api/treatments/{id}
```

Treatment management is protected and intended for authorized salon owners.

---

# Stylist APIs

Stylist management supports CRUD operations for salon owners.

Typical operations include:

```text
GET
POST
PUT
DELETE
```

Stylists are associated with a tenant and can contain working hours and skill information.

---

# Booking APIs

Booking operations include:

```text
GET    /api/bookings
POST   /api/bookings
PUT    /api/bookings/{id}/status
```

Bookings are subject to authorization, tenant isolation, working-hour validation, and conflict detection.

---

# Security Architecture

ProjectSaloon uses:

```text
Client
  │
  │ Authorization: Bearer <JWT>
  ▼
JwtAuthenticationFilter
  │
  ▼
JwtService
  │
  ▼
UserDetailsService
  │
  ▼
SecurityContext
  │
  ▼
Controller
  │
  ▼
Service
```

The application uses stateless authentication.

JWT tokens currently have a 24-hour expiration period.

---

# Tenant Isolation

Tenant isolation is a core part of the backend.

For authenticated operations, the backend obtains the current user from the Spring Security context.

The user's tenant is then used to scope database operations.

For example:

```java
User user = userRepository.findByEmail(email)
        .orElseThrow(...);

Tenant tenant = user.getTenant();
```

The repository can then query using the tenant:

```java
findByTenant(tenant)
```

or:

```java
findByIdAndTenant(id, tenant)
```

This prevents a tenant from directly accessing another tenant's resources.

---

# Technology Stack

## Backend

| Technology      | Purpose                      |
| --------------- | ---------------------------- |
| Java            | Backend language             |
| Spring Boot     | Application framework        |
| Spring Security | Authentication/authorization |
| JWT             | Stateless authentication     |
| Spring Data JPA | Database access              |
| Hibernate       | ORM                          |
| MySQL           | Relational database          |
| Redis           | Caching                      |
| Maven           | Build system                 |
| JUnit           | Testing                      |
| Mockito         | Mock-based testing           |
| Docker          | Containerization             |

---

## Frontend

| Technology   | Purpose                   |
| ------------ | ------------------------- |
| React        | UI                        |
| Vite         | Development/build tooling |
| React Router | Routing                   |
| Axios        | HTTP requests             |
| JavaScript   | Frontend language         |
| CSS          | Styling                   |

---

# Docker

The backend includes Docker support for:

```text
Spring Boot
MySQL
Redis
```

Docker Compose manages the services.

Typical architecture:

```text
docker-compose
      │
      ├── mysql
      │
      ├── redis
      │
      └── app
```

The Spring Boot application runs internally on:

```text
8080
```

and is exposed on the host through:

```text
8000
```

---

# Docker Commands

From the backend directory:

```bash
docker compose up --build
```

Run in detached mode:

```bash
docker compose up -d
```

Check services:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs app
```

View all logs:

```bash
docker compose logs
```

Stop services:

```bash
docker compose down
```

Stop and remove volumes:

```bash
docker compose down -v
```

Use the last command carefully because it removes persistent Docker volumes such as the MySQL data volume.

---

# Environment Variables

The application is designed to use environment variables instead of committing credentials.

Expected variables include:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
DB_HOST
DB_PORT
DB_NAME
```

Typical Docker defaults:

```text
DB_HOST=mysql
DB_PORT=3306
DB_NAME=saloonmanagement
```

Secrets such as database passwords and JWT secrets should **never be committed to Git**.

---

# Local Development

## Prerequisites

Install:

* Java
* Maven or Maven Wrapper
* Node.js
* npm
* MySQL
* Redis
* Docker Desktop
* Git

---

# Running the Backend

Navigate to:

```bash
cd SaloonBackend
```

Run tests:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

Start Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend is available at:

```text
http://localhost:8080
```

When using the Docker Compose configuration, the backend is exposed through:

```text
http://localhost:8000
```

---

# Running the Frontend

Navigate to:

```bash
cd SaloonFrontend
```

Install dependencies:

```bash
npm install
```

Start development server:

```bash
npm run dev
```

The Vite development server normally runs at:

```text
http://localhost:5173
```

---

# Frontend API Configuration

The Axios client is located at:

```text
SaloonFrontend/src/services/api.js
```

The frontend currently communicates with the backend using the configured API base URL.

Example:

```javascript
const api = axios.create({
    baseURL: "http://localhost:8000"
});
```

API functions are kept inside the service layer rather than making Axios requests directly throughout the UI.

---

# Error & Loading States

The frontend handles common asynchronous states.

### Loading

Example:

```text
Loading salons...
```

### Error

Example:

```text
Failed to load salons.
```

### Empty Results

The UI handles situations where:

* no salons match the search
* a salon has no treatments
* a requested salon does not exist

This prevents API failures from resulting in a completely broken UI.

---

# Testing

Backend tests can be executed with:

```powershell
cd SaloonBackend
.\mvnw.cmd test
```

Frontend production build:

```bash
cd SaloonFrontend
npm run build
```

---

# Manual Testing Checklist

## Salon Listing

* [ ] Home page loads
* [ ] Real salon data appears
* [ ] Loading state appears
* [ ] Search works
* [ ] Search by address works
* [ ] Empty search results are handled
* [ ] Backend failure displays an error

## Salon Details

* [ ] Clicking a salon opens `/salon/:id`
* [ ] Salon information loads
* [ ] Treatments load
* [ ] Treatment cards display correctly
* [ ] Treatment selection works
* [ ] Booking form opens

## Authentication

* [ ] Owner signup works
* [ ] Customer signup works
* [ ] Login works
* [ ] JWT is returned
* [ ] Protected endpoints reject unauthenticated users
* [ ] Role-based authorization works

## Booking

* [ ] Customer can create booking
* [ ] Invalid booking is rejected
* [ ] Overlapping booking is rejected
* [ ] Working hours are validated
* [ ] Booking status can be updated

## Security

* [ ] Customer cannot access owner-only resources
* [ ] Tenant isolation works
* [ ] Invalid JWT is rejected
* [ ] Expired JWT is rejected

---

# Project Status

## Backend

* [x] Spring Boot application
* [x] MySQL integration
* [x] Redis configuration
* [x] JWT authentication
* [x] Role-based authorization
* [x] Multi-tenant structure
* [x] Tenant management
* [x] Treatment CRUD
* [x] Stylist CRUD
* [x] Booking functionality
* [x] Booking conflict detection
* [x] Working-hours validation
* [x] Exception handling
* [x] Docker configuration
* [x] Backend tests

## Frontend

* [x] React application
* [x] React Router
* [x] Salon listing
* [x] Salon search
* [x] Salon details
* [x] Treatment display
* [x] Booking form UI
* [x] Responsive styling
* [x] Backend API integration
* [x] Loading states
* [x] Error states
* [x] Empty states

## Upcoming

* [ ] Frontend authentication integration
* [ ] JWT storage/handling
* [ ] Authentication state management
* [ ] Protected frontend routes
* [ ] Complete frontend booking integration
* [ ] Customer dashboard
* [ ] Owner dashboard
* [ ] Stylist dashboard
* [ ] Improved Redis usage
* [ ] Production deployment
* [ ] Production database configuration
* [ ] CI/CD

---

# Development Roadmap

### Phase 1 — Backend Foundation

```text
Spring Boot
    ↓
MySQL
    ↓
JPA
    ↓
REST APIs
```

### Phase 2 — Security

```text
Spring Security
    ↓
JWT
    ↓
Roles
    ↓
Tenant Isolation
```

### Phase 3 — Core Business Logic

```text
Tenants
    ↓
Treatments
    ↓
Stylists
    ↓
Bookings
```

### Phase 4 — Frontend

```text
React
    ↓
React Router
    ↓
Axios
    ↓
Backend Integration
```

### Phase 5 — Full Application

```text
Authentication
    ↓
Dashboards
    ↓
Booking Flow
    ↓
Caching
    ↓
Docker
    ↓
Deployment
```

---

# Project Structure

```text
ProjectSaloon/
│
├── SaloonBackend/
│   ├── src/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── pom.xml
│   └── README.md
│
├── SaloonFrontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
└── .gitignore
```

---

# Git Workflow

The repository is hosted on GitHub.

Recommended workflow:

```bash
git status

git add .

git commit -m "feat: describe the change"

git push
```

Use commits that represent actual changes.

Examples:

```text
feat: integrate frontend authentication
feat: implement customer booking flow
feat: add owner dashboard
fix: prevent duplicate bookings
fix: handle expired JWT
refactor: simplify treatment service
test: add booking conflict tests
chore: update Docker configuration
```

---

# Design Goals

ProjectSaloon is being developed with the following principles:

### Separation of Concerns

Controllers handle HTTP requests.

Services handle business logic.

Repositories handle persistence.

Security components handle authentication.

React components handle UI.

API services handle HTTP communication.

---

### Security First

The application avoids putting authentication and authorization logic directly into UI components or controllers whenever possible.

JWT authentication and tenant-aware database queries form the security foundation.

---

### Multi-Tenant by Design

Tenant isolation is not treated as an optional feature.

Business resources are associated with a tenant and protected operations use the authenticated user's tenant.

---

### Production-Oriented Development

The project is being built with real-world concepts such as:

* Docker
* Environment variables
* JWT
* Redis
* Database persistence
* Exception handling
* Testing
* API separation
* Layered architecture
* Git version control

---

# Future Improvements

Potential future improvements include:

* Refresh tokens
* More granular permissions
* Better tenant identification
* Salon slugs
* Public salon profiles
* Appointment notifications
* Email notifications
* Payment integration
* Calendar integration
* Advanced queue management
* Redis caching implementation
* Rate limiting
* API documentation with OpenAPI/Swagger
* CI/CD pipeline
* Cloud deployment
* Production monitoring
* Centralized logging

---

# Author

**Parth Sharma**

B.Tech Computer Science Engineering

Project: **ProjectSaloon**

---

# License

This project is currently intended as a personal/educational development project.

A formal open-source license can be added when the project is prepared for public distribution.
