# Stock-In API Documentation

## Overview
This document describes the stock-in API endpoint for adding inventory to existing items.

## Base URL
```
http://localhost:8080/inventory
```

## Authentication
All endpoints require JWT authentication with ADMIN role.

## Endpoint

### POST /inventory/items/{id}/stock-in

**Description:** Adds stock to an existing inventory item. Increases item quantity, updates inventory count, and logs the transaction.

**Authorization:** ADMIN only

**Path Parameters:**
- `id` (string, required): The ID of the inventory item to add stock to

**Request Body:**
```json
{
  "quantity": "integer (> 0, required)",
  "reason": "string (optional)"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Stock added successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "Laptop Dell XPS 15",
    "quantity": 75,
    "description": "High-performance laptop for developers",
    "price": 1499.99
  },
  "timestamp": "2026-04-22T19:50:00.123",
  "statusCode": 200
}
```

**Response (Error - Item Not Found):**
```json
{
  "success": false,
  "message": "Item not found with id: 507f1f77bcf86cd799439999",
  "data": null,
  "timestamp": "2026-04-22T19:50:00.123",
  "statusCode": 400
}
```

**Response (Error - Invalid Quantity):**
```json
{
  "success": false,
  "message": "Quantity must be greater than 0",
  "data": null,
  "timestamp": "2026-04-22T19:50:00.123",
  "statusCode": 400
}
```

## Validation Rules

| Field | Validation | Error Message |
|--------|-------------|---------------|
| quantity | Must be > 0 | "Quantity must be greater than 0" |
| quantity | Required | "Quantity is required" |
| itemId (path) | Must exist | "Item not found with id: {id}" |

## Transaction Logging

Every stock-in operation creates a transaction record in the `stock_transactions` collection:

```json
{
  "_id": "transaction_id",
  "item_id": "inventory_item_id",
  "transaction_type": "STOCK_IN",
  "quantity": 25,
  "reason": "New stock arrival from supplier",
  "created_at": "2026-04-22T19:50:00.123",
  "created_by": "user_id_from_jwt"
}
```

## Usage Examples

### Basic Stock-In
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-in
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 25,
  "reason": "New stock arrival from supplier"
}
```

### Minimal Stock-In (No Reason)
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-in
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 10
}
```

### Error Cases

#### Invalid Quantity
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-in
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": -5
}
```
**Response:** 400 Bad Request

#### Item Not Found
```bash
POST /inventory/items/nonexistent_id/stock-in
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 25,
  "reason": "Test stock addition"
}
```
**Response:** 400 Bad Request

#### Unauthorized (Non-Admin User)
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-in
Content-Type: application/json
Authorization: Bearer <staff_jwt_token>

{
  "quantity": 25
}
```
**Response:** 403 Forbidden

## Business Logic

1. **Validation**: Request is validated for required fields and business rules
2. **Item Lookup**: System verifies the inventory item exists
3. **Quantity Update**: Item quantity is increased by the specified amount
4. **Transaction Logging**: A stock transaction record is created with:
   - Item ID reference
   - Transaction type: STOCK_IN
   - Quantity added
   - Optional reason
   - User ID who performed the action
   - Timestamp
5. **Response**: Updated item details are returned

## Database Schema Changes

### Inventory Items Collection
```json
{
  "_id": "item_id",
  "name": "string",
  "quantity": "integer (updated)",
  "description": "string",
  "price": "number"
}
```

### Stock Transactions Collection (New)
```json
{
  "_id": "transaction_id",
  "item_id": "string",
  "transaction_type": "STOCK_IN|STOCK_OUT",
  "quantity": "integer",
  "reason": "string",
  "created_at": "datetime",
  "created_by": "string"
}
```

## Security Considerations

- **Authentication**: Valid JWT token required
- **Authorization**: User must have ADMIN role
- **Audit Trail**: All stock changes are logged with user ID
- **Input Validation**: All inputs are validated before processing

## Integration Notes

- The endpoint integrates with existing inventory management
- Stock transactions can be queried for audit purposes
- Real-time quantity updates ensure inventory accuracy
- Transaction logging supports traceability and compliance
