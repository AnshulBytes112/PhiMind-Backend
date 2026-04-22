# Inventory Management API Documentation

## Overview
This document describes the inventory management API endpoints for creating and managing inventory items.

## Base URL
```
http://localhost:8080/inventory
```

## Authentication
All endpoints require JWT authentication except where noted. Admin-only endpoints require the user to have the `ADMIN` role.

## Endpoints

### 1. Create Inventory Item
**POST** `/inventory/items`

**Description:** Creates a new inventory item with initial stock quantity.

**Authorization:** ADMIN only

**Request Body:**
```json
{
  "name": "string (required)",
  "quantity": "integer (>= 0, required)",
  "description": "string (optional)",
  "price": "number (optional)"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Inventory item created successfully",
  "data": {
    "id": "string",
    "name": "string",
    "quantity": "integer",
    "description": "string",
    "price": "number"
  },
  "timestamp": "string",
  "statusCode": 200
}
```

**Validation Rules:**
- Name must not be empty
- Quantity must be greater than or equal to 0
- Item name must be unique

**Edge Cases:**
- Negative quantity: Returns 400 Bad Request
- Missing name: Returns 400 Bad Request
- Duplicate item name: Returns 400 Bad Request

**Example Request:**
```bash
POST /inventory/items
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "name": "Laptop Dell XPS 15",
  "quantity": 25,
  "description": "High-performance laptop for developers",
  "price": 1499.99
}
```

### 2. Get All Inventory Items
**GET** `/inventory/items`

**Description:** Retrieves all inventory items.

**Authorization:** Any authenticated user

**Response:**
```json
{
  "success": true,
  "message": "Inventory items retrieved successfully",
  "data": [
    {
      "id": "string",
      "name": "string",
      "quantity": "integer",
      "description": "string",
      "price": "number"
    }
  ],
  "timestamp": "string",
  "statusCode": 200
}
```

### 3. Get Inventory Item by ID
**GET** `/inventory/items/{id}`

**Description:** Retrieves a specific inventory item by its ID.

**Authorization:** Any authenticated user

**Response:**
```json
{
  "success": true,
  "message": "Inventory item retrieved successfully",
  "data": {
    "id": "string",
    "name": "string",
    "quantity": "integer",
    "description": "string",
    "price": "number"
  },
  "timestamp": "string",
  "statusCode": 200
}
```

### 4. Update Inventory Item
**PUT** `/inventory/items/{id}`

**Description:** Updates an existing inventory item.

**Authorization:** ADMIN only

**Request Body:** Same as create inventory item

**Response:** Similar to create inventory item response

### 5. Delete Inventory Item
**DELETE** `/inventory/items/{id}`

**Description:** Deletes an inventory item.

**Authorization:** ADMIN only

**Response:**
```json
{
  "success": true,
  "message": "Inventory item deleted successfully",
  "data": "Item with id {id} has been deleted",
  "timestamp": "string",
  "statusCode": 200
}
```

## Error Responses

All endpoints return standardized error responses:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "string",
  "statusCode": 400
}
```

## Database Schema

**Collection:** `inventory_items`

**Document Structure:**
```json
{
  "_id": "ObjectId",
  "name": "string (unique)",
  "quantity": "integer",
  "description": "string",
  "price": "number"
}
```

## Usage Examples

### Create Item with Minimum Required Fields
```bash
POST /inventory/items
{
  "name": "Wireless Mouse",
  "quantity": 50
}
```

### Create Item with All Fields
```bash
POST /inventory/items
{
  "name": "Mechanical Keyboard",
  "quantity": 15,
  "description": "RGB mechanical keyboard with cherry switches",
  "price": 89.99
}
```

### Error Response Example
```json
{
  "success": false,
  "message": "Item with name 'Wireless Mouse' already exists",
  "data": null,
  "timestamp": "2026-04-22T19:30:00.123",
  "statusCode": 400
}
```
