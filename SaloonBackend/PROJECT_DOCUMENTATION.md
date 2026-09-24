# Multi-Saloon Management System - Complete Project Documentation

## Project Overview
**Project Name**: SaloonManagement  
**Type**: Multi-tenant SaaS Application  
**Tech Stack**: Spring Boot 4.1.1, Java 17, MySQL, JWT, Spring Security  
**Architecture**: RESTful API with Multi-tenant Database Isolation  
**Purpose**: Enable multiple salon owners to manage their bookings, stylists, treatments, and customers in isolated environments

---

## Project Structure

```
SaloonManagement/
├── src/
│   ├── main/
│   │   ├── java/com/parth/saloonmanagement/
│   │   │   ├── SaloonManagementApplication.java          # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java                    # Spring Security + JWT configuration
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java                   # Authentication endpoints
│   │   │   │   ├── BookingController.java                 # Booking CRUD operations
│   │   │   │   ├── StylistController.java                # Stylist management
│   │   │   │   └── TreatmentController.java              # Treatment/service management
│   │   │   ├── dto/
│   │   │   │   ├── AuthResponse.java                     # JWT token response
│   │   │   │   ├── BookingRequest.java                   # Create booking request
│   │   │   │   ├── BookingResponse.java                  # Booking details response
│   │   │   │   ├── BookingStatusRequest.java             # Status update request
│   │   │   │   ├── LoginRequest.java                      # Login credentials
│   │   │   │   ├── SignUpRequest.java                    # Owner registration
│   │   │   │   ├── StylistRequest.java                   # Create/update stylist
│   │   │   │   ├── StylistResponse.java                  # Stylist details
│   │   │   │   ├── TreatmentRequest.java                 # Create/update treatment
│   │   │   │   └── TreatmentResponse.java                # Treatment details
│   │   │   ├── entity/
│   │   │   │   ├── Booking.java                          # Booking entity with relationships
│   │   │   │   ├── BookingStatus.java                    # Enum: PENDING, CONFIRMED, CANCELLED
│   │   │   │   ├── Role.java                             # Enum: ROLE_CUSTOMER, ROLE_STYLIST, ROLE_OWNER
│   │   │   │   ├── Stylist.java                          # Stylist entity
│   │   │   │   ├── Tenant.java                           # Multi-tenant isolation entity
│   │   │   │   ├── Treatment.java                        # Service/treatment entity
│   │   │   │   └── User.java                             # User entity implementing UserDetails
│   │   │   ├── exception/
│   │   │   │   ├── AccessDeniedException.java            # Custom access denied exception
│   │   │   │   ├── BookingConflictException.java         # Booking conflict exception
│   │   │   │   ├── GlobalExceptionHandler.java           # Centralized exception handling
│   │   │   │   ├── InvalidCredentialsException.java      # Invalid login credentials
│   │   │   │   ├── ResourceNotFoundException.java        # Resource not found exception
│   │   │   │   └── UserAlreadyExistsException.java       # Duplicate user exception
│   │   │   ├── repository/
│   │   │   │   ├── BookingRepository.java                # Booking data access
│   │   │   │   ├── StylistRepository.java                # Stylist data access
│   │   │   │   ├── TenantRepository.java                 # Tenant data access
│   │   │   │   ├── TreatmentRepository.java              # Treatment data access
│   │   │   │   └── UserRepository.java                   # User data access
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java          # JWT filter for request interception
│   │   │   │   ├── JwtService.java                        # JWT token generation/validation
│   │   │   │   └── UserDetailService.java                 # UserDetailsService implementation
│   │   │   └── service/
│   │   &│   │   ├── AuthService.java                     # Authentication business logic
│   │   │   │   ├── BookingService.java                   # Booking business logic
│   │   │   │   ├── StylistService.java                  # Stylist business logic
│   │   │   │   └── TreatmentService.java                 # Treatment business logic
│   │   └── resources/
│   │       ├── application.properties                    # Application configuration
│   │       ├── static/                                    # Static resources (empty)
│   │       └── templates/                                 # Template resources (empty)
│   └── test/
│       └── java/com/parth/saloonmanagement/
│           └── SaloonManagementApplicationTests.java      # Basic context load test
├── pom.xml                                                # Maven dependencies
├── mvw, mvw.cmd                                          # Maven wrapper scripts
├── .gitignore                                            # Git ignore rules
└── HELP.md                                               # Spring Boot help documentation
```

---

## Dependencies (pom.xml)

### Core Spring Dependencies
- **spring-boot-starter-data-jpa**: JPA/Hibernate for database operations
- **spring-boot-starter-security**: Spring Security for authentication/authorization
- **spring-boot-starter-validation**: Bean validation (JSR-380)
- **spring-boot-starter-webmvc**: Spring MVC for REST APIs

### JWT Dependencies
- **jjwt-api (0.13.0)**: JWT API
- **jjwt-impl (0.13.0)**: JWT implementation
- **jjwt-jackson (0.13.0)**: Jackson JSON support for JWT

### Database
- **mysql-connector-j**: MySQL JDBC driver

### Development Tools
- **spring-boot-devtools**: Hot reload during development
- **lombok**: Reduce boilerplate code

### Testing Dependencies
- **spring-boot-starter-data-jpa-test**: JPA testing
- **spring-boot-starter-security-test**: Security testing
- **spring-boot-starter-validation-test**: Validation testing
- **spring-boot-starter-webmvc-test**: Web MVC testing

### Build Configuration
- **Java Version**: 17
- **Spring Boot Version**: 4.1.1
- **Maven Compiler Plugin**: Configured with Lombok annotation processor

---

## Database Configuration (application.properties)

```properties
spring.application.name=SaloonManagement

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/saloonmanagement
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate DDL Auto
spring.jpa.hibernate.ddl-auto=update

# JWT Secret Key
jwt.secret-key=${JWT_SECRET}

**Database**: MySQL running on localhost:3306  
**Schema Name**: saloonmanagement  
**DDL Strategy**: update (automatically updates schema on startup)  
**JWT Secret**: Hardcoded (should be externalized for production)

---

## Entity Models

### 1. User Entity
**File**: `User.java`  
**Purpose**: Represents application users with authentication capabilities

**Fields**:
- `id` (Long, Primary Key, Auto-generated)
- `email` (String, Email validation, unique, used as username)
- `name` (String, required)
- `password` (String, required, BCrypt encrypted)
- `contact` (String, 10 digits validation)
- `role` (Role enum: ROLE_CUSTOMER, ROLE_STYLIST, ROLE_OWNER)
- `tenant` (ManyToOne relationship with Tenant)

**Implements**: `UserDetails` (Spring Security interface)

**UserDetails Methods**:
- `getAuthorities()`: Returns role as GrantedAuthority
- `getUsername()`: Returns email
- `isAccountNonExpired()`: Always true
- `isAccountNonLocked()`: Always true
- `isCredentialsNonExpired()`: Always true
- `isEnabled()`: Always true

**Relationships**:
- Many Users belong to one Tenant
- One User can be linked to one Stylist (via Stylist entity)

---

### 2. Tenant Entity
**File**: `Tenant.java`  
**Purpose**: Multi-tenant isolation - each salon is a separate tenant

**Fields**:
- `id` (Long, Primary Key, Auto-generated)
- `name` (String, required - salon name)
- `email` (String, Email validation, required)
- `contact` (String, 10 digits validation)
- `address` (String, required)

**Relationships**:
- One Tenant has many Users
- One Tenant has many Stylists
- One Tenant has many Treatments
- One Tenant has many Bookings

**Isolation Strategy**: All queries filter by tenant to ensure data isolation

---

### 3. Stylist Entity
**File**: `Stylist.java`  
**Purpose**: Represents salon staff/stylists

**Fields**:
- `id` (Long, Primary Key, Auto-generated)
- `name` (String, required)
- `contact` (String, 10 digits validation)
- `skills` (List<String>, @ElementCollection - stored in separate table)
- `user` (OneToOne relationship with User - optional)
- `tenant` (ManyToOne relationship with Tenant)
- `workStartTime` (LocalTime - stylist shift start)
- `workEndTime` (LocalTime - stylist shift end)

**Relationships**:
- Many Stylists belong to one Tenant
- One Stylist can be linked to one User (for login)
- One Stylist has many Bookings

**Validation**: Contact must be exactly 10 digits

---

### 4. Treatment Entity
**File**: `Treatment.java`  
**Purpose**: Represents services offered by the salon

**Fields**:
- `id` (Long, Primary Key, Auto-generated)
- `name` (String, required)
- `durationMinutes` (Integer, required, must be positive)
- `price` (BigDecimal, required, must be positive)
- `description` (String, optional)
- `tenant` (ManyToOne relationship with Tenant)

**Relationships**:
- Many Treatments belong to one Tenant
- One Treatment has many Bookings

**Validation**: Duration and price must be positive values

---

### 5. Booking Entity
**File**: `Booking.java`  
**Purpose**: Represents customer appointments

**Fields**:
- `id` (Long, Primary Key, Auto-generated)
- `user` (ManyToOne relationship with User - customer)
- `treatment` (ManyToOne relationship with Treatment)
- `stylist` (ManyToOne relationship with Stylist)
- `tenant` (ManyToOne relationship with Tenant)
- `status` (BookingStatus enum: PENDING, CONFIRMED, CANCELLED)
- `startTime` (LocalDateTime, required)
- `endTime` (LocalDateTime, required)

**Relationships**:
- Many Bookings belong to one User
- Many Bookings belong to one Treatment
- Many Bookings belong to one Stylist
- Many Bookings belong to one Tenant

**Business Logic**:
- endTime is calculated as startTime + treatment.durationMinutes
- Status transitions: PENDING → CONFIRMED or CANCELLED

---

### 6. Role Enum
**File**: `Role.java`  
**Purpose**: Defines user roles for authorization

**Values**:
- `ROLE_CUSTOMER`: Can book appointments, view/cancel own bookings
- `ROLE_STYLIST`: (Not fully implemented yet)
- `ROLE_OWNER`: Full access to all tenant data, manage stylists/treatments/bookings

---

### 7. BookingStatus Enum
**File**: `BookingStatus.java`  
**Purpose**: Defines booking lifecycle states

**Values**:
- `PENDING`: Initial state when booking is created
- `CONFIRMED`: Booking confirmed by owner
- `CANCELLED`: Booking cancelled by customer or owner

---

## Repository Layer

### 1. BookingRepository
**File**: `BookingRepository.java`  
**Extends**: `JpaRepository<Booking, Long>`

**Custom Methods**:
- `findByStylistAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn()`: Finds stylist conflicts
  - Parameters: Stylist, endTime, startTime, List<BookingStatus>
  - Used for: Checking stylist availability during booking
- `findByUserAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn()`: Finds user conflicts
  - Parameters: User, endTime, startTime, List<BookingStatus>
  - Used for: Checking customer double-booking
- `findByTenant(Tenant)`: Gets all bookings for a tenant
- `findByUser(User)`: Gets all bookings for a user
- `findByIdAndTenant(Long, Tenant)`: Gets booking by ID within tenant scope
- `findByIdAndUser(Long, User)`: Gets booking by ID within user scope

**Conflict Detection Logic**: Uses overlapping time range check (startTime < existingEndTime AND endTime > existingStartTime)

---

### 2. StylistRepository
**File**: `StylistRepository.java`  
**Extends**: `JpaRepository<Stylist, Long>`

**Custom Methods**:
- `findByIdAndTenant(Long, Tenant)`: Gets stylist by ID within tenant scope
- `findByTenant(Tenant)`: Gets all stylists for a tenant

**Purpose**: Ensures tenant isolation for stylist operations

---

### 3. TreatmentRepository
**File**: `TreatmentRepository.java`  
**Extends**: `JpaRepository<Treatment, Long>`

**Custom Methods**:
- `findByTenant(Tenant)`: Gets all treatments for a tenant
- `findByIdAndTenant(Long, Tenant)`: Gets treatment by ID within tenant scope

**Purpose**: Ensures tenant isolation for treatment operations

---

### 4. UserRepository
**File**: `UserRepository.java`  
**Extends**: `JpaRepository<User, Long>`

**Custom Methods**:
- `findByEmail(String)`: Finds user by email (used for authentication)

**Purpose**: User lookup for authentication and authorization

---

### 5. TenantRepository
**File**: `TenantRepository.java`  
**Extends**: `JpaRepository<Tenant, Long>`

**Custom Methods**: None (uses standard JPA methods)

**Purpose**: Basic CRUD operations for tenants

---

## Service Layer

### 1. AuthService
**File**: `AuthService.java`  
**Purpose**: Handles user authentication and registration

**Dependencies**:
- UserRepository
- TenantRepository
- PasswordEncoder
- JwtService

**Methods**:

#### login(LoginRequest)
**Purpose**: Authenticate user and generate JWT token  
**Flow**:
1. Find user by email from request
2. Throw ResourceNotFoundException if user not found
3. Verify password using PasswordEncoder.matches()
4. Throw InvalidCredentialsException if password mismatch
5. Generate JWT token using JwtService
6. Return AuthResponse with token, email, role, tenantId

**Returns**: AuthResponse (token, email, role, tenantId)

---

#### signUp(SignUpRequest)
**Purpose**: Register new salon owner and create tenant  
**Flow**:
1. Check if user already exists by email
2. Throw ResourceNotFoundException if user exists (should be UserAlreadyExistsException)
3. Create new Tenant with name, address, contact, email from request
4. Save tenant to database
5. Create new User with name, email, contact, password from request
6. Set user role to ROLE_OWNER
7. Encrypt password using PasswordEncoder
8. Link user to saved tenant
9. Save user to database
10. Generate JWT token for new user
11. Return AuthResponse with token, email, role, tenantId

**Returns**: AuthResponse (token, email, role, tenantId)

**Note**: Only owner signup is implemented. Customer and stylist signup not available.

---

### 2. BookingService
**File**: `BookingService.java`  
**Purpose**: Manage booking lifecycle with conflict detection

**Dependencies**:
- BookingRepository
- UserRepository
- TreatmentRepository
- StylistRepository

**Methods**:

#### createBooking(BookingRequest)
**Purpose**: Create new booking with conflict detection  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Find treatment by ID and tenant (throw ResourceNotFoundException if not found)
4. Find stylist by ID and tenant (throw ResourceNotFoundException if not found)
5. Calculate endTime = startTime + treatment.durationMinutes
6. Check for stylist conflicts:
   - Query bookings overlapping with new time range
   - Filter by PENDING and CONFIRMED status
   - Throw BookingConflictException if conflicts exist
7. Check for user conflicts:
   - Query user bookings overlapping with new time range
   - Filter by PENDING and CONFIRMED status
   - Throw BookingConflictException if conflicts exist
8. Validate working hours:
   - Check if startTime < stylist.workStartTime OR endTime > stylist.workEndTime
   - Throw BookingConflictException if outside working hours
9. Create new Booking entity
10. Set user, stylist, treatment, tenant, startTime, endTime
11. Set status to PENDING
12. Save booking to database
13. Return BookingResponse with booking details

**Returns**: BookingResponse (id, status, startTime, endTime, treatmentName, stylistName)

**Validation Rules**:
- Stylist must not have overlapping bookings
- User must not have overlapping bookings
- Booking must be within stylist working hours
- Treatment and stylist must belong to same tenant as user

---

#### deleteBooking(Long id)
**Purpose**: Delete booking with role-based authorization  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant and role
4. If role is ROLE_OWNER:
   - Find booking by ID and tenant
   - Throw ResourceNotFoundException if not found
5. If role is ROLE_CUSTOMER:
   - Find booking by ID and user
   - Throw ResourceNotFoundException if not found
6. If role is ROLE_STYLIST:
   - Throw AccessDeniedException (stylists cannot delete)
7. Build BookingResponse from booking
8. Delete booking from database
9. Return BookingResponse

**Returns**: BookingResponse (deleted booking details)

**Authorization**:
- Owners can delete any booking in their tenant
- Customers can only delete their own bookings
- Stylists cannot delete bookings

---

#### updateBookingStatus(Long id, BookingStatusRequest)
**Purpose**: Update booking status with role-based restrictions  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant and role
4. If role is ROLE_OWNER:
   - Find booking by ID and tenant
   - Throw ResourceNotFoundException if not found
5. If role is ROLE_CUSTOMER:
   - Find booking by ID and user
   - Throw ResourceNotFoundException if not found
   - Check if requested status is CANCELLED
   - Throw AccessDeniedException if not CANCELLED (customers can only cancel)
6. If role is ROLE_STYLIST:
   - Throw AccessDeniedException (stylists cannot update status)
7. Update booking status
8. Save booking to database
9. Return BookingResponse

**Returns**: BookingResponse (updated booking details)

**Authorization**:
- Owners can set any status (PENDING, CONFIRMED, CANCELLED)
- Customers can only set CANCELLED
- Stylists cannot update status

---

#### getBookingById(Long id)
**Purpose**: Get booking by ID with role-based access  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant and role
4. If role is ROLE_OWNER:
   - Find booking by ID and tenant
   - Throw ResourceNotFoundException if not found
5. If role is ROLE_CUSTOMER:
   - Find booking by ID and user
   - Throw ResourceNotFoundException if not found
6. If role is ROLE_STYLIST:
   - Throw ResourceNotFoundException (stylists cannot view)
7. Return BookingResponse

**Returns**: BookingResponse (booking details)

**Authorization**:
- Owners can view any booking in their tenant
- Customers can only view their own bookings
- Stylists cannot view bookings

---

#### getAllBookings()
**Purpose**: Get all bookings based on user role  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant and role
4. If role is ROLE_OWNER:
   - Get all bookings by tenant
5. If role is ROLE_CUSTOMER:
   - Get all bookings by user
6. If role is ROLE_STYLIST:
   - Get all bookings by tenant (fallback, not implemented properly)
7. Convert each Booking to BookingResponse
8. Return list of BookingResponse

**Returns**: List<BookingResponse>

**Authorization**:
- Owners see all bookings for their tenant
- Customers see only their own bookings
- Stylists see all bookings for their tenant (incomplete implementation)

---

### 3. StylistService
**File**: `StylistService.java`  
**Purpose**: Manage stylist profiles

**Dependencies**:
- StylistRepository
- UserRepository

**Methods**:

#### createStylist(StylistRequest)
**Purpose**: Create new stylist  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Create new Stylist entity
5. Set name, contact, skills from request
6. Link to tenant
7. Save stylist to database
8. Return StylistResponse

**Returns**: StylistResponse (id, name, contact, skills)

**Authorization**: Only authenticated users can create stylists (should be restricted to owners)

---

#### getStylists()
**Purpose**: Get all stylists for current tenant  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Find all stylists by tenant
5. Convert each Stylist to StylistResponse
6. Return list of StylistResponse

**Returns**: List<StylistResponse>

**Authorization**: Returns stylists for user's tenant only

---

#### updateStylist(Long id, StylistRequest)
**Purpose**: Update existing stylist  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Find stylist by ID and tenant (throw ResourceNotFoundException if not found)
5. Update name, contact, skills from request
6. Save stylist to database
7. Return StylistResponse

**Returns**: StylistResponse (updated stylist details)

**Authorization**: Only users within the same tenant can update

---

#### deleteStylist(Long id)
**Purpose**: Delete stylist  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Find stylist by ID and tenant (throw ResourceNotFoundException if not found)
5. Delete stylist from database

**Returns**: void

**Authorization**: Only users within the same tenant can delete

---

### 4. TreatmentService
**File**: `TreatmentService.java`  
**Purpose**: Manage salon services/treatments

**Dependencies**:
- TreatmentRepository
- UserRepository

**Methods**:

#### createTreatment(TreatmentRequest)
**Purpose**: Create new treatment/service  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Create new Treatment entity
5. Set name, durationMinutes, price, description from request
6. Link to tenant
7. Save treatment to database
8. Return TreatmentResponse

**Returns**: TreatmentResponse (id, name, durationMinutes, price, description)

**Authorization**: Only authenticated users can create treatments (should be restricted to owners)

---

#### getTreatments()
**Purpose**: Get all treatments for current tenant  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Find all treatments by tenant
5. Convert each Treatment to TreatmentResponse
6. Return list of TreatmentResponse

**Returns**: List<TreatmentResponse>

**Authorization**: Returns treatments for user's tenant only

---

#### deleteTreatment(Long id)
**Purpose**: Delete treatment  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Find treatment by ID and tenant (throw ResourceNotFoundException if not found)
5. Delete treatment from database

**Returns**: void

**Authorization**: Only users within the same tenant can delete

---

#### updateTreatment(Long id, TreatmentRequest)
**Purpose**: Update existing treatment  
**Flow**:
1. Get current user email from SecurityContext
2. Find user by email (throw ResourceNotFoundException if not found)
3. Get user's tenant
4. Find treatment by ID and tenant (throw ResourceNotFoundException if not found)
5. Update name, durationMinutes, price, description from request
6. Save treatment to database
7. Return TreatmentResponse

**Returns**: TreatmentResponse (updated treatment details)

**Authorization**: Only users within the same tenant can update

---

## Controller Layer

### 1. AuthController
**File**: `AuthController.java`  
**Base Path**: `/api/auth`  
**Purpose**: Authentication endpoints

**Endpoints**:

#### POST /api/auth/signup
**Purpose**: Register new salon owner  
**Request Body**: SignUpRequest (email, password, name, tenantName, tenantAddress, contact)  
**Validation**: @Valid annotation triggers bean validation  
**Response**: AuthResponse (token, email, role, tenantId)  
**Status**: 200 OK  
**Security**: Public (no authentication required)

---

#### POST /api/auth/login
**Purpose**: Authenticate existing user  
**Request Body**: LoginRequest (email, password)  
**Validation**: @Valid annotation triggers bean validation  
**Response**: AuthResponse (token, email, role, tenantId)  
**Status**: 200 OK  
**Security**: Public (no authentication required)

---

### 2. BookingController
**File**: `BookingController.java`  
**Base Path**: `/api/bookings`  
**Purpose**: Booking management endpoints

**Endpoints**:

#### POST /api/bookings
**Purpose**: Create new booking  
**Request Body**: BookingRequest (stylistId, treatmentId, startTime)  
**Validation**: @Valid, @Future on startTime  
**Response**: BookingResponse  
**Status**: 201 CREATED  
**Security**: Requires ROLE_OWNER or ROLE_CUSTOMER

---

#### GET /api/bookings
**Purpose**: Get all bookings (based on role)  
**Response**: List<BookingResponse>  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER or ROLE_CUSTOMER

---

#### GET /api/bookings/{id}
**Purpose**: Get booking by ID  
**Path Variable**: id (Long)  
**Response**: BookingResponse  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER or ROLE_CUSTOMER

---

#### PUT /api/bookings/{id}/status
**Purpose**: Update booking status  
**Path Variable**: id (Long)  
**Request Body**: BookingStatusRequest (status)  
**Response**: BookingResponse  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER or ROLE_CUSTOMER

---

#### DELETE /api/bookings/{id}
**Purpose**: Delete booking  
**Path Variable**: id (Long)  
**Response**: BookingResponse (deleted booking)  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER or ROLE_CUSTOMER

---

### 3. StylistController
**File**: `StylistController.java`  
**Base Path**: `/api/stylists`  
**Purpose**: Stylist management endpoints

**Endpoints**:

#### GET /api/stylists
**Purpose**: Get all stylists for tenant  
**Response**: List<StylistResponse>  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER

---

#### POST /api/stylists
**Purpose**: Create new stylist  
**Request Body**: StylistRequest (name, contact, skills)  
**Validation**: @Valid  
**Response**: StylistResponse  
**Status**: 201 CREATED  
**Security**: Requires ROLE_OWNER

---

#### PUT /api/stylists/{id}
**Purpose**: Update stylist  
**Path Variable**: id (Long)  
**Request Body**: StylistRequest (name, contact, skills)  
**Validation**: @Valid  
**Response**: StylistResponse  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER

---

#### DELETE /api/stylists/{id}
**Purpose**: Delete stylist  
**Path Variable**: id (Long)  
**Response**: void  
**Status**: 204 NO CONTENT  
**Security**: Requires ROLE_OWNER

---

### 4. TreatmentController
**File**: `TreatmentController.java`  
**Base Path**: `/api/treatments`  
**Purpose**: Treatment/service management endpoints

**Endpoints**:

#### POST /api/treatments/create
**Purpose**: Create new treatment  
**Request Body**: TreatmentRequest (name, durationMinutes, price, description)  
**Validation**: @Valid  
**Response**: TreatmentResponse  
**Status**: 201 CREATED  
**Security**: Requires ROLE_OWNER

---

#### GET /api/treatments
**Purpose**: Get all treatments for tenant  
**Response**: List<TreatmentResponse>  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER

---

#### PUT /api/treatments/{id}
**Purpose**: Update treatment  
**Path Variable**: id (Long)  
**Request Body**: TreatmentRequest (name, durationMinutes, price, description)  
**Validation**: @Valid  
**Response**: TreatmentResponse  
**Status**: 200 OK  
**Security**: Requires ROLE_OWNER

---

#### DELETE /api/treatments/{id}
**Purpose**: Delete treatment  
**Path Variable**: id (Long)  
**Response**: void  
**Status**: 204 NO CONTENT  
**Security**: Requires ROLE_OWNER

---

## Security Configuration

### SecurityConfig
**File**: `SecurityConfig.java`  
**Purpose**: Configure Spring Security and JWT filter chain

**Beans**:

#### passwordEncoder()
**Returns**: BCryptPasswordEncoder  
**Purpose**: Encrypt passwords before storing

---

#### securityFilterChain(HttpSecurity, JwtService, UserDetailService)
**Purpose**: Configure HTTP security and add JWT filter  
**Configuration**:
- CSRF disabled (stateless API)
- Session management: STATELESS (no sessions)
- Authorization rules:
  - `/api/auth/**` - Permit all (public endpoints)
  - `/api/treatments/**` - Requires ROLE_OWNER
  - `/api/stylists/**` - Requires ROLE_OWNER
  - `/api/bookings/**` - Requires ROLE_OWNER or ROLE_CUSTOMER
  - Any other request - Requires authentication
- JWT filter added before UsernamePasswordAuthenticationFilter

**Security Flow**:
1. Request comes in
2. JwtAuthenticationFilter intercepts
3. Extracts JWT from Authorization header (Bearer token)
4. Validates token and extracts username
5. Loads user details and sets authentication in SecurityContext
6. Request proceeds to controller with authenticated context

---

## JWT Implementation

### JwtService
**File**: `JwtService.java`  
**Purpose**: Generate and validate JWT tokens

**Configuration**:
- Secret key from application.properties
- Expiration time: 24 hours (1000L * 60 * 60 * 24 ms)
- Signing algorithm: HMAC-SHA

**Methods**:

#### extractUsername(String token)
**Purpose**: Extract subject (email) from JWT  
**Returns**: String (username/email)

---

#### extractExpiration(String token)
**Purpose**: Extract expiration date from JWT  
**Returns**: Date (expiration timestamp)

---

#### isTokenExpired(String token)
**Purpose**: Check if token is expired  
**Returns**: boolean (true if expired)

---

#### generateToken(UserDetails)
**Purpose**: Generate new JWT token  
**Parameters**: UserDetails (user information)  
**Token Claims**:
- subject: username (email)
- issuedAt: current date
- expiration: current time + 24 hours
- signed with: secret key

**Returns**: String (JWT token)

---

#### isTokenValid(String token, UserDetails)
**Purpose**: Validate token against user details  
**Checks**:
- Username matches
- Token not expired

**Returns**: boolean (true if valid)

---

### JwtAuthenticationFilter
**File**: `JwtAuthenticationFilter.java`  
**Purpose**: Intercept requests and validate JWT tokens

**Extends**: OncePerRequestFilter (ensures filter runs once per request)

**Flow**:
1. Extract Authorization header
2. Check if header is null or doesn't start with "Bearer "
3. If invalid, skip filter and continue chain
4. Extract JWT token (remove "Bearer " prefix)
5. Extract username from token
6. Load user details from UserDetailsService
7. Validate token against user details
8. If valid, create UsernamePasswordAuthenticationToken
9. Set authentication in SecurityContextHolder
10. Continue filter chain

**Dependencies**: JwtService, UserDetailsService

---

### UserDetailService
**File**: `UserDetailService.java`  
**Purpose**: Load user details for authentication

**Implements**: UserDetailsService (Spring Security interface)

**Methods**:

#### loadUserByUsername(String username)
**Purpose**: Load user by email (username)  
**Flow**:
1. Find user by email from UserRepository
2. Throw RuntimeException if not found
3. Return User entity (implements UserDetails)

**Returns**: UserDetails (User entity)

**Note**: Uses email as username for authentication

---

## Exception Handling

### GlobalExceptionHandler
**File**: `GlobalExceptionHandler.java`  
**Purpose**: Centralized exception handling with @RestControllerAdvice

**Handled Exceptions**:

#### ResourceNotFoundException
**HTTP Status**: 404 NOT_FOUND  
**Response**: Exception message  
**Triggered When**: Resource not found in database

---

#### BookingConflictException
**HTTP Status**: 409 CONFLICT  
**Response**: Exception message  
**Triggered When**: Booking conflict detected (stylist/user double-booking, outside working hours)

---

#### InvalidCredentialsException
**HTTP Status**: 401 UNAUTHORIZED  
**Response**: Exception message  
**Triggered When**: Login password mismatch

---

#### UserAlreadyExistsException
**HTTP Status**: 409 CONFLICT  
**Response**: Exception message  
**Triggered When**: Attempting to create duplicate user

---

#### AccessDeniedException
**HTTP Status**: 403 FORBIDDEN  
**Response**: Exception message  
**Triggered When**: User lacks permission for operation

---

### Custom Exceptions

#### AccessDeniedException
**File**: `AccessDeniedException.java`  
**Purpose**: Custom access denied exception (note: conflicts with java.nio.file.AccessDeniedException)

---

#### BookingConflictException
**File**: `BookingConflictException.java`  
**Purpose**: Thrown when booking conflicts are detected

---

#### InvalidCredentialsException
**File**: `InvalidCredentialsException.java`  
**Purpose**: Thrown when login credentials are invalid

---

#### ResourceNotFoundException
**File**: `ResourceNotFoundException.java`  
**Purpose**: Thrown when requested resource is not found

---

#### UserAlreadyExistsException
**File**: `UserAlreadyExistsException.java`  
**Purpose**: Thrown when attempting to create duplicate user

---

## DTOs (Data Transfer Objects)

### Request DTOs

#### LoginRequest
**Fields**:
- email (String, @Email, @NotBlank)
- password (String, @NotBlank)

---

#### SignUpRequest
**Fields**:
- email (String, @Email, @NotBlank)
- password (String, @NotBlank)
- name (String, @NotBlank)
- tenantName (String, @NotBlank)
- tenantAddress (String, @NotBlank)
- contact (String, @Size(min=10, max=10))

---

#### BookingRequest
**Fields**:
- stylistId (Long, @NotNull)
- treatmentId (Long, @NotNull)
- startTime (LocalDateTime, @NotNull, @Future)

---

#### BookingStatusRequest
**Fields**:
- status (BookingStatus)

---

#### StylistRequest
**Fields**:
- name (String, @NotBlank)
- contact (String, @Size(min=10, max=10))
- skills (List<String>, @ElementCollection)

---

#### TreatmentRequest
**Fields**:
- name (String, @NotBlank)
- durationMinutes (Integer, @NotNull, @Positive)
- price (BigDecimal, @NotNull, @Positive)
- description (String, optional)

---

### Response DTOs

#### AuthResponse
**Fields**:
- token (String)
- email (String)
- role (Role)
- tenantId (Long)

---

#### BookingResponse
**Fields**:
- id (Long)
- status (BookingStatus)
- startTime (LocalDateTime)
- endTime (LocalDateTime)
- treatmentName (String)
- stylistName (String)

---

#### StylistResponse
**Fields**:
- id (Long)
- name (String)
- contact (String)
- skills (List<String>)

---

#### TreatmentResponse
**Fields**:
- id (Long)
- name (String)
- durationMinutes (Integer)
- price (BigDecimal)
- description (String)

---

## Validation Rules

### Entity-Level Validation
- **User.email**: Must be valid email format, required
- **User.name**: Required
- **User.password**: Required
- **User.contact**: Exactly 10 digits
- **Tenant.name**: Required
- **Tenant.email**: Valid email format, required
- **Tenant.contact**: Exactly 10 digits
- **Tenant.address**: Required
- **Stylist.name**: Required
- **Stylist.contact**: Exactly 10 digits
- **Treatment.name**: Required
- **Treatment.durationMinutes**: Required, must be positive
- **Treatment.price**: Required, must be positive
- **Booking.startTime**: Required
- **Booking.endTime**: Required

### DTO-Level Validation
- **LoginRequest**: Email format, required fields
- **SignUpRequest**: Email format, 10-digit contact, all fields required
- **BookingRequest**: Required fields, startTime must be in future
- **StylistRequest**: Required name, 10-digit contact
- **TreatmentRequest**: Required fields, positive duration and price

---

## Business Logic Flows

### 1. Owner Registration Flow
```
Client → POST /api/auth/signup
  ↓
AuthController.signUp()
  ↓
AuthService.signUp()
  ↓
1. Check if user exists by email
2. Create Tenant entity
3. Save Tenant
4. Create User entity with ROLE_OWNER
5. Encrypt password
6. Link User to Tenant
7. Save User
8. Generate JWT token
9. Return AuthResponse
  ↓
Client receives JWT token
```

---

### 2. Login Flow
```
Client → POST /api/auth/login
  ↓
AuthController.login()
  ↓
AuthService.login()
  ↓
1. Find user by email
2. Verify password with BCrypt
3. Generate JWT token
4. Return AuthResponse
  ↓
Client receives JWT token
  ↓
Client includes token in Authorization header for subsequent requests
```

---

### 3. Booking Creation Flow
```
Client → POST /api/bookings (with JWT token)
  ↓
JwtAuthenticationFilter validates token
  ↓
SecurityContext set with authenticated user
  ↓
BookingController.createBooking()
  ↓
BookingService.createBooking()
  ↓
1. Get current user from SecurityContext
2. Find User, Treatment, Stylist (tenant-scoped)
3. Calculate endTime = startTime + duration
4. Check stylist conflicts (overlapping bookings)
5. Check user conflicts (double-booking)
6. Validate working hours
7. Create Booking with PENDING status
8. Save Booking
9. Return BookingResponse
  ↓
Client receives booking confirmation
```

---

### 4. Multi-Tenant Data Isolation
```
Every query includes tenant filtering:
- findByTenant(Tenant) - returns only tenant's data
- findByIdAndTenant(Long, Tenant) - ensures ID belongs to tenant
- findByIdAndUser(Long, User) - ensures user owns the resource

SecurityContext provides current user
User provides Tenant
All repository queries filter by Tenant
```

---

## Current Limitations & Known Issues

### 1. Role Implementation
- **ROLE_STYLIST**: Not fully implemented
  - Stylists cannot view bookings assigned to them
  - Stylists cannot update their availability
  - Stylist-specific endpoints missing

### 2. User Management
- Only owner signup exists
- No customer registration endpoint
- No staff/stylist user creation
- No user profile management
- No password change functionality
- No user activation/deactivation

### 3. Booking System
- Cannot modify booking details (only status)
- No search/filtering capabilities
- No pagination on list endpoints
- No recurring bookings
- No waitlist functionality
- No booking reminders
- No calendar view API

### 4. Stylist Management
- Working hours not validated in requests
- No availability management per day
- No leave/time-off management
- No skill-based recommendations
- No performance metrics

### 5. Business Features
- No payment processing
- No invoicing
- No discount/coupon system
- No review/rating system
- No loyalty program
- No service packages

### 6. Analytics & Reporting
- No revenue reports
- No booking statistics
- No performance analytics
- No export functionality
- No dashboard

### 7. Testing
- Only basic contextLoads test exists
- No unit tests for services
- No integration tests for controllers
- No repository tests
- No security tests

### 8. Documentation
- No API documentation (Swagger/OpenAPI)
- No Javadoc comments
- No README with setup instructions

### 9. Configuration
- Hardcoded database credentials
- Hardcoded JWT secret
- No environment profiles (dev/test/prod)
- No externalized configuration

### 10. Logging
- No application logging
- No audit trail
- No request/response logging

### 11. DevOps
- No Docker configuration
- No CI/CD pipeline
- No database migrations
- No health checks
- No monitoring

### 12. Security
- No rate limiting
- No OWASP security headers
- No data encryption at rest
- No GDPR compliance features
- Custom AccessDeniedException conflicts with java.nio.file.AccessDeniedException

### 13. Frontend
- No UI/UX
- Backend-only API
- No mobile responsiveness
- No PWA support

---

## Database Schema

### Tables (Generated by Hibernate)

#### users
```sql
id (BIGINT, PK, AUTO_INCREMENT)
email (VARCHAR, UNIQUE, NOT NULL)
name (VARCHAR, NOT NULL)
password (VARCHAR, NOT NULL)
contact (VARCHAR(10))
role (VARCHAR, NOT NULL)
tenant_id (BIGINT, FK)
```

#### tenants
```sql
id (BIGINT, PK, AUTO_INCREMENT)
name (VARCHAR, NOT NULL)
email (VARCHAR, NOT NULL)
contact (VARCHAR(10))
address (VARCHAR, NOT NULL)
```

#### stylists
```sql
id (BIGINT, PK, AUTO_INCREMENT)
name (VARCHAR, NOT NULL)
contact (VARCHAR(10))
tenant_id (BIGINT, FK)
user_id (BIGINT, FK)
work_start_time (TIME)
work_end_time (TIME)
```

#### stylist_skills
```sql
stylist_id (BIGINT, FK)
skills (VARCHAR)
```

#### treatments
```sql
id (BIGINT, PK, AUTO_INCREMENT)
name (VARCHAR, NOT NULL)
duration_minutes (INT, NOT NULL)
price (DECIMAL, NOT NULL)
description (VARCHAR)
tenant_id (BIGINT, FK)
```

#### bookings
```sql
id (BIGINT, PK, AUTO_INCREMENT)
status (VARCHAR, NOT NULL)
start_time (DATETIME, NOT NULL)
end_time (DATETIME, NOT NULL)
user_id (BIGINT, FK)
treatment_id (BIGINT, FK)
stylist_id (BIGINT, FK)
tenant_id (BIGINT, FK)
```

---

## API Endpoint Summary

### Public Endpoints (No Authentication)
- `POST /api/auth/signup` - Register owner
- `POST /api/auth/login` - Login

### Owner Endpoints (ROLE_OWNER)
- `GET /api/treatments` - Get all treatments
- `POST /api/treatments/create` - Create treatment
- `PUT /api/treatments/{id}` - Update treatment
- `DELETE /api/treatments/{id}` - Delete treatment
- `GET /api/stylists` - Get all stylists
- `POST /api/stylists` - Create stylist
- `PUT /api/stylists/{id}` - Update stylist
- `DELETE /api/stylists/{id}` - Delete stylist
- `GET /api/bookings` - Get all bookings (tenant-wide)
- `POST /api/bookings` - Create booking
- `GET /api/bookings/{id}` - Get booking by ID
- `PUT /api/bookings/{id}/status` - Update booking status (any status)
- `DELETE /api/bookings/{id}` - Delete booking

### Customer Endpoints (ROLE_CUSTOMER)
- `GET /api/bookings` - Get my bookings
- `POST /api/bookings` - Create booking
- `GET /api/bookings/{id}` - Get my booking by ID
- `PUT /api/bookings/{id}/status` - Cancel booking (only CANCELLED status)
- `DELETE /api/bookings/{id}` - Delete my booking

### Stylist Endpoints (ROLE_STYLIST)
- **Not implemented yet**

---

## Technology Stack Details

### Backend Framework
- **Spring Boot**: 4.1.1
- **Spring Security**: For authentication/authorization
- **Spring Data JPA**: For database operations
- **Hibernate**: JPA implementation

### Database
- **MySQL**: Relational database
- **JDBC Driver**: mysql-connector-j

### Security
- **JWT**: JSON Web Tokens for stateless authentication
- **BCrypt**: Password encryption
- **Spring Security**: Security framework

### Utilities
- **Lombok**: Reduce boilerplate code
- **Jakarta Validation**: Bean validation (JSR-380)
- **Jackson**: JSON serialization/deserialization

### Build
- **Maven**: Build tool
- **Java**: 17
- **Maven Compiler Plugin**: Compilation configuration

---

## Development Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, etc.)

### Steps
1. Clone repository
2. Create MySQL database: `saloonmanagement`
3. Configure database credentials in `application.properties`
4. Run: `mvn spring-boot:run`
5. Application starts on default port 8080

### Default Configuration
- Server Port: 8080
- Database: localhost:3306/saloonmanagement
- JWT Secret: "your-secret-key-that-is-long-enough-for-learning"
- JWT Expiration: 24 hours

---

## Testing

### Current Test Coverage
- Only one test: `SaloonManagementApplicationTests.contextLoads()`
- No unit tests
- No integration tests
- No repository tests
- No service tests
- No controller tests
- No security tests

### Test Dependencies Included
- spring-boot-starter-data-jpa-test
- spring-boot-starter-security-test
- spring-boot-starter-validation-test
- spring-boot-starter-webmvc-test

---

## Key Design Patterns

### 1. Multi-Tenancy
- Tenant-based data isolation
- All queries filtered by tenant
- Users belong to exactly one tenant

### 2. Role-Based Access Control (RBAC)
- Three roles: CUSTOMER, STYLIST, OWNER
- Role-based endpoint authorization
- Role-based data access

### 3. Repository Pattern
- Spring Data JPA repositories
- Custom query methods for business logic
- Tenant-scoped queries

### 4. Service Layer Pattern
- Business logic in service classes
- Controllers delegate to services
- Services use repositories

### 5. DTO Pattern
- Separate request/response DTOs
- Entities not exposed to API
- Validation on DTOs

### 6. Exception Handling
- Centralized exception handler
- Custom exceptions for business logic
- HTTP status mapping

### 7. JWT Authentication
- Stateless authentication
- Token-based security
- Filter-based validation

---

## Security Implementation Details

### Authentication Flow
1. User provides credentials to `/api/auth/login`
2. AuthService validates credentials
3. JwtService generates JWT token
4. Token returned to client
5. Client includes token in Authorization header: `Bearer <token>`
6. JwtAuthenticationFilter validates token on each request
7. SecurityContext populated with authenticated user
8. Controllers access user from SecurityContext

### Authorization Rules
- **Public**: `/api/auth/**`
- **Owner only**: `/api/treatments/**`, `/api/stylists/**`
- **Owner or Customer**: `/api/bookings/**`
- **Authenticated**: All other requests

### Password Security
- BCrypt encryption with default strength (10 rounds)
- Passwords never stored in plain text
- Passwords never logged or exposed in responses

### JWT Security
- HMAC-SHA signing
- 24-hour expiration
- Secret key from configuration
- Token validation on every request

---

## Performance Considerations

### Current Implementation
- No pagination (all list endpoints return full data)
- No caching
- No database connection pooling configuration
- No query optimization
- N+1 query potential in some service methods

### Recommended Improvements
- Add pagination to all list endpoints
- Implement caching for frequently accessed data
- Configure connection pool (HikariCP)
- Add database indexes on frequently queried fields
- Optimize queries to avoid N+1 problem

---

## Scalability Considerations

### Current Limitations
- Single database instance
- No horizontal scaling support
- No load balancing configuration
- No distributed caching
- No message queue for async operations

### Recommended Improvements
- Database read replicas
- Redis for caching
- Message queue (RabbitMQ/Kafka) for async tasks
- Container orchestration (Kubernetes)
- CDN for static assets

---

## Monitoring & Observability

### Current State
- No application logging
- No metrics collection
- No health checks
- No distributed tracing
- No alerting

### Recommended Improvements
- Add structured logging (SLF4J + Logback)
- Implement health check endpoints
- Add metrics (Micrometer + Prometheus)
- Add distributed tracing (Zipkin/Jaeger)
- Set up monitoring dashboards (Grafana)

---

## Deployment Considerations

### Current State
- No Docker configuration
- No CI/CD pipeline
- No environment-specific configurations
- No database migration scripts
- No deployment documentation

### Recommended Improvements
- Create Dockerfile
- Docker Compose for local development
- CI/CD pipeline (GitHub Actions/Jenkins)
- Database migrations (Flyway/Liquibase)
- Environment profiles (dev/test/prod)
- Externalize secrets (Vault, environment variables)
- Kubernetes deployment manifests
- Helm charts for deployment

---

## Code Quality

### Current State
- No code coverage metrics
- No static code analysis
- No code style enforcement
- No code review process
- No linting

### Recommended Improvements
- Add JaCoCo for code coverage
- Add SonarQube for code quality analysis
- Add Checkstyle/SpotBugs for static analysis
- Enforce code style with Google Java Style
- Implement code review process

---

## Documentation

### Current State
- No API documentation (Swagger/OpenAPI)
- No Javadoc comments
- No architecture documentation
- No deployment guide
- No contributor guide

### Recommended Improvements
- Add SpringDoc OpenAPI for API documentation
- Add Javadoc comments to all public methods
- Create architecture decision records (ADRs)
- Write comprehensive README
- Create contributor guide
- Add API usage examples

---

## Conclusion

This Multi-Saloon Management System provides a solid foundation for a multi-tenant SaaS application with:
- ✅ Multi-tenant architecture with proper data isolation
- ✅ JWT-based stateless authentication
- ✅ Role-based authorization
- ✅ Booking conflict detection
- ✅ Working hours validation
- ✅ Comprehensive entity relationships
- ✅ RESTful API design

However, significant work is needed in:
- ❌ Testing (unit, integration, E2E)
- ❌ Documentation (API, code, deployment)
- ❌ DevOps (Docker, CI/CD, monitoring)
- ❌ Feature completeness (stylist role, user management, search/filtering)
- ❌ Business features (payments, analytics, reporting)
- ❌ Frontend (UI/UX)
- ❌ Security hardening (rate limiting, encryption, compliance)

The project is suitable for a proof-of-concept or MVP but requires substantial development for production readiness.
