# PhiMind Backend API Documentation

## Authentication Endpoints

### POST /auth/register
Registers a new user in the system with a specified role.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "role": "STAFF" // or "ADMIN"
}
```

**Validation Rules:**
- Email must be valid format and unique
- Password must be at least 8 characters with uppercase, lowercase, number, and special character
- Role must be either ADMIN or STAFF

**Success Response (200 OK):**
```json
{
  "userId": "507f1f77bcf86cd799439011",
  "email": "user@example.com",
  "role": "STAFF",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "User registered successfully"
}
```

**Error Response (400 Bad Request):**
```json
{
  "message": "Email already exists"
}
```

### POST /auth/login
Authenticates a user and returns a JWT access token.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "Password123!"
}
```

**Validation Rules:**
- Email must exist in the system
- Password must match the stored hash

**Success Response (200 OK):**
```json
{
  "userId": "507f1f77bcf86cd799439011",
  "email": "user@example.com",
  "role": "STAFF",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

**Error Response (400 Bad Request):**
```json
{
  "message": "Invalid password"
}
```

## JWT Token
The JWT token contains:
- `userId`: The user's unique identifier
- `role`: The user's role (ADMIN or STAFF)

## Configuration
- **Database**: MongoDB running on localhost:27017
- **Database Name**: phimind_db
- **Server Port**: 8080
- **JWT Secret**: Configurable via `jwt.secret` property
- **JWT Expiration**: 24 hours (86400000 ms)

## Setup Instructions
1. Ensure MongoDB is running on localhost:27017
2. Run the application: `mvn spring-boot:run`
3. The API will be available at `http://localhost:8080`

## Password Requirements
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter  
- At least one number
- At least one special character (@#$%^&+=!)
