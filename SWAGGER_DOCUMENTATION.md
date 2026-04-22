# Swagger/OpenAPI Documentation

## Overview

This document provides comprehensive information about the Swagger/OpenAPI 3.0 documentation integrated into the PhiMind Backend API.

## Accessing Swagger UI

### Development Environment
```
http://localhost:8080/swagger-ui.html
```

### Production Environment
```
https://your-domain.com/swagger-ui.html
```

### OpenAPI Specification
```
http://localhost:8080/v3/api-docs
```

## Configuration

### Dependencies Added
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

### Swagger Configuration
- **File**: `src/main/java/phimind/example/Backend/config/SwaggerConfig.java`
- **Features**: JWT Authentication, API Info, Security Schemes
- **Customization**: Project-specific branding and licensing

## API Documentation Structure

### 1. Authentication Endpoints

#### POST /auth/register
- **Description**: Register a new user and get JWT token
- **Authorization**: None (public endpoint)
- **Request Body**: `RegistrationRequest`
- **Response**: `ApiResponse<RegistrationResponse>`

#### POST /auth/login
- **Description**: Authenticate user and get JWT token
- **Authorization**: None (public endpoint)
- **Request Body**: `LoginRequest`
- **Response**: `ApiResponse<LoginResponse>`

### 2. Inventory Management Endpoints

#### GET /inventory/items
- **Description**: Get paginated list of inventory items
- **Authorization**: JWT token required
- **Parameters**: `page`, `size`
- **Response**: `ApiResponse<PaginationResponse<InventoryItemResponse>>`

#### POST /inventory/items
- **Description**: Create new inventory item
- **Authorization**: ADMIN role required
- **Request Body**: `InventoryItemRequest`
- **Response**: `ApiResponse<InventoryItemResponse>`

#### GET /inventory/items/{id}
- **Description**: Get specific inventory item by ID
- **Authorization**: JWT token required
- **Path Variable**: `id`
- **Response**: `ApiResponse<InventoryItemResponse>`

#### PUT /inventory/items/{id}
- **Description**: Update inventory item
- **Authorization**: ADMIN role required
- **Request Body**: `InventoryItemRequest`
- **Response**: `ApiResponse<InventoryItemResponse>`

#### DELETE /inventory/items/{id}
- **Description**: Delete inventory item
- **Authorization**: ADMIN role required
- **Path Variable**: `id`
- **Response**: `ApiResponse<String>`

#### POST /inventory/items/{id}/stock-in
- **Description**: Add stock to inventory item
- **Authorization**: ADMIN role required
- **Request Body**: `StockInRequest`
- **Response**: `ApiResponse<InventoryItemResponse>`

#### POST /inventory/items/{id}/stock-out
- **Description**: Remove stock from inventory item
- **Authorization**: ADMIN role required
- **Request Body**: `StockOutRequest`
- **Response**: `ApiResponse<InventoryItemResponse>`

## Request/Response Models

### Authentication Models

#### RegistrationRequest
```json
{
  "email": "string (required, valid email)",
  "password": "string (required)",
  "role": "STAFF|ADMIN (optional)"
}
```

#### LoginRequest
```json
{
  "email": "string (required, valid email)",
  "password": "string (required)"
}
```

#### RegistrationResponse
```json
{
  "userId": "string",
  "email": "string",
  "role": "STAFF|ADMIN",
  "token": "string",
  "message": "string"
}
```

#### LoginResponse
```json
{
  "userId": "string",
  "email": "string",
  "role": "STAFF|ADMIN",
  "token": "string",
  "message": "string"
}
```

### Inventory Models

#### InventoryItemRequest
```json
{
  "name": "string (required)",
  "quantity": "integer (>= 0, required)",
  "description": "string (optional)",
  "price": "number (optional)"
}
```

#### InventoryItemResponse
```json
{
  "id": "string",
  "name": "string",
  "quantity": "integer",
  "description": "string",
  "price": "number"
}
```

#### StockInRequest
```json
{
  "quantity": "integer (> 0, required)",
  "reason": "string (optional)"
}
```

#### StockOutRequest
```json
{
  "quantity": "integer (> 0, required)",
  "reason": "string (optional)"
}
```

#### PaginationResponse
```json
{
  "content": "array of items",
  "currentPage": "integer",
  "pageSize": "integer",
  "totalElements": "long",
  "totalPages": "integer",
  "first": "boolean",
  "last": "boolean"
}
```

#### ApiResponse
```json
{
  "success": "boolean",
  "message": "string",
  "data": "object|array|null",
  "timestamp": "string",
  "statusCode": "integer"
}
```

## Authentication

### JWT Token Authentication
1. **Get Token**: Use `/auth/login` or `/auth/register` to obtain JWT token
2. **Include Token**: Add `Authorization: Bearer <token>` header to requests
3. **Token Validation**: Server validates token and extracts user information

### Role-Based Access Control
- **STAFF**: Can read inventory items
- **ADMIN**: Full access to all endpoints including CRUD and stock management

## Error Responses

### Standard Error Format
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2026-04-22T20:20:00.123",
  "statusCode": 400
}
```

### Common HTTP Status Codes
- **200 OK**: Request successful
- **400 Bad Request**: Validation error or invalid input
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Using Swagger UI

### 1. Browse API Documentation
- Open `http://localhost:8080/swagger-ui.html` in your browser
- Explore available endpoints organized by tags

### 2. Test API Endpoints
- Click on any endpoint to see detailed documentation
- Use "Try it out" feature to test endpoints directly
- Fill in request parameters and click "Execute"

### 3. Authentication in Swagger UI
1. Click the "Authorize" button (padlock icon)
2. Enter `Bearer <your-jwt-token>` in the popup
3. Click "Authorize" to authenticate
4. The token will be automatically included in subsequent requests

### 4. Request/Response Examples
- Swagger UI provides example request/response bodies
- Schema definitions show all available fields
- Validation rules and constraints are documented

## API Tags

### Authentication
- User registration and login management
- JWT token generation and validation

### Inventory Management
- CRUD operations for inventory items
- Stock management (add/remove)
- Paginated data retrieval

## Schema Documentation

### Model Schemas
- All DTOs are documented with `@Schema` annotations
- Field descriptions and examples provided
- Validation constraints documented
- Required vs optional fields clearly marked

### Response Schemas
- Standardized `ApiResponse` wrapper for all endpoints
- Pagination metadata included where applicable
- Error response structure documented

## Security Documentation

### JWT Authentication
- Bearer token authentication scheme configured
- Token validation and user extraction
- Role-based access control implemented

### Security Requirements
- All endpoints (except auth) require JWT token
- ADMIN-only endpoints require ADMIN role
- Token expiration and validation

## Development Benefits

### 1. API Discovery
- Interactive API documentation
- Automatic endpoint discovery
- Real-time testing capabilities

### 2. Client Integration
- Auto-generated client SDKs possible
- Request/response examples for developers
- Clear contract documentation

### 3. Testing Support
- Built-in testing interface
- Request validation examples
- Error response documentation

### 4. Documentation Maintenance
- Auto-generated from code annotations
- Always up-to-date with API changes
- Single source of truth for API docs

## Best Practices

### 1. Annotation Usage
- Use `@Operation` for endpoint descriptions
- Use `@Parameter` for parameter documentation
- Use `@ApiResponse` for response documentation
- Use `@Schema` for model documentation

### 2. Documentation Quality
- Provide clear, concise descriptions
- Include meaningful examples
- Document all possible responses

### 3. Security Considerations
- Document authentication requirements
- Include authorization information
- Provide security examples

## Customization Options

### 1. API Information
- Update title, description, version in `SwaggerConfig`
- Modify contact and license information
- Add custom server configurations

### 2. Security Schemes
- Add additional authentication methods
- Configure multiple security schemes
- Customize security requirements

### 3. UI Customization
- Customize Swagger UI appearance
- Add custom CSS/JavaScript
- Configure default expansion state

## Integration Examples

### JavaScript Client
```javascript
// Using fetch with Swagger documentation
const token = await getJwtToken();
const response = await fetch('/inventory/items?page=0&size=10', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

### Postman Collection
1. Import OpenAPI spec from `/v3/api-docs`
2. Generate Postman collection
3. Configure authentication with JWT token
4. Test all endpoints

### cURL Examples
```bash
# Get inventory items with pagination
curl -X GET "http://localhost:8080/inventory/items?page=0&size=10" \
  -H "Authorization: Bearer <jwt-token>"

# Create inventory item
curl -X POST "http://localhost:8080/inventory/items" \
  -H "Authorization: Bearer <jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Item","quantity":25,"description":"Test description","price":99.99}'
```

## Troubleshooting

### Common Issues
1. **401 Unauthorized**: Check JWT token format and validity
2. **403 Forbidden**: Verify user role permissions
3. **400 Bad Request**: Check request body validation
4. **404 Not Found**: Verify endpoint URL and parameters

### Debug Mode
- Enable debug logging in Spring Boot
- Check Swagger configuration
- Review browser console for JavaScript errors

### Performance Considerations
- Swagger UI loads all schemas on startup
- Large APIs may impact initial load time
- Consider lazy loading for very large APIs

## Future Enhancements

### 1. Advanced Features
- API versioning documentation
- Multiple environment documentation
- Custom UI themes and branding

### 2. Developer Tools
- Code generation plugins
- API testing automation
- Documentation validation

### 3. Integration Options
- API Gateway integration
- Microservices documentation
- External API documentation

This Swagger documentation provides comprehensive API coverage and serves as the primary reference for developers integrating with the PhiMind Backend API.
