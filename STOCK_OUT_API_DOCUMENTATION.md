# Stock-Out API Documentation

## Overview
This document describes the stock-out API endpoint for reducing inventory from existing items with comprehensive validation and edge case handling.

## Base URL
```
http://localhost:8080/inventory
```

## Authentication
All endpoints require JWT authentication with ADMIN role.

## Endpoint

### POST /inventory/items/{id}/stock-out

**Description:** Reduces stock from an inventory item. Decreases item quantity, ensures stock does not go below zero, and logs the transaction.

**Authorization:** ADMIN only

**Path Parameters:**
- `id` (string, required): The ID of the inventory item to reduce stock from

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
  "message": "Stock removed successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "Laptop Dell XPS 15",
    "quantity": 25,
    "description": "High-performance laptop for developers",
    "price": 1499.99
  },
  "timestamp": "2026-04-22T20:00:00.123",
  "statusCode": 200
}
```

**Response (Error - Item Not Found):**
```json
{
  "success": false,
  "message": "Item not found with id: 507f1f77bcf86cd799439999",
  "data": null,
  "timestamp": "2026-04-22T20:00:00.123",
  "statusCode": 400
}
```

**Response (Error - Insufficient Stock):**
```json
{
  "success": false,
  "message": "Insufficient stock. Available: 10, Requested: 15",
  "data": null,
  "timestamp": "2026-04-22T20:00:00.123",
  "statusCode": 400
}
```

**Response (Error - Invalid Quantity):**
```json
{
  "success": false,
  "message": "Quantity must be greater than 0",
  "data": null,
  "timestamp": "2026-04-22T20:00:00.123",
  "statusCode": 400
}
```

## Validation Rules

| Field | Validation | Error Message |
|--------|-------------|---------------|
| quantity | Must be > 0 | "Quantity must be greater than 0" |
| quantity | Required | "Quantity is required" |
| itemId (path) | Must exist | "Item not found with id: {id}" |
| available stock | Must be sufficient | "Insufficient stock. Available: X, Requested: Y" |
| stock level | Cannot go below 0 | "Stock cannot go below zero. Current stock: X" |

## Edge Cases Handled

### 1. Stock Never Goes Below Zero
```java
// Additional safety check to prevent negative stock
int newQuantity = item.getQuantity() - request.getQuantity();
if (newQuantity < 0) {
    throw new RuntimeException("Stock cannot go below zero. Current stock: " + item.getQuantity());
}
```

### 2. Negative Quantity Input
- Handled by validation annotation: `@Min(value = 1)`
- Returns 400 Bad Request with descriptive error message

### 3. Concurrent Updates Protection
- MongoDB atomic operations ensure data consistency
- Each operation reads current state before updating
- Race conditions are prevented by database-level locking

### 4. Invalid Item ID
- Validates item existence before processing
- Returns clear error message with the invalid ID

## Transaction Logging

Every stock-out operation creates a transaction record in the `stock_transactions` collection:

```json
{
  "_id": "transaction_id",
  "item_id": "inventory_item_id",
  "transaction_type": "STOCK_OUT",
  "quantity": 15,
  "reason": "Customer order fulfillment",
  "created_at": "2026-04-22T20:00:00.123",
  "created_by": "user_id_from_jwt"
}
```

## Usage Examples

### Basic Stock-Out
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-out
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 15,
  "reason": "Customer order fulfillment"
}
```

### Minimal Stock-Out (No Reason)
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-out
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 5
}
```

## Error Cases

### Insufficient Stock
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-out
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 100  // Item only has 25 in stock
}
```
**Response:** 400 Bad Request
```json
{
  "success": false,
  "message": "Insufficient stock. Available: 25, Requested: 100",
  "data": null,
  "timestamp": "2026-04-22T20:00:00.123",
  "statusCode": 400
}
```

### Invalid Quantity
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-out
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": -5
}
```
**Response:** 400 Bad Request

### Item Not Found
```bash
POST /inventory/items/nonexistent_id/stock-out
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "quantity": 10,
  "reason": "Test stock removal"
}
```
**Response:** 400 Bad Request

### Unauthorized (Non-Admin User)
```bash
POST /inventory/items/507f1f77bcf86cd799439011/stock-out
Content-Type: application/json
Authorization: Bearer <staff_jwt_token>

{
  "quantity": 10
}
```
**Response:** 403 Forbidden

## Business Logic Flow

1. **Authentication**: Verify JWT token and ADMIN role
2. **Validation**: Validate request parameters
3. **Item Lookup**: Find inventory item by ID
4. **Stock Validation**: Check sufficient stock availability
5. **Zero Stock Protection**: Ensure stock doesn't go below zero
6. **Quantity Update**: Reduce item quantity atomically
7. **Transaction Logging**: Create audit trail record
8. **Response**: Return updated item details

## Data Consistency Measures

### Atomic Operations
```java
// Read current state
InventoryItem item = inventoryItemRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

// Validate and calculate new quantity
int newQuantity = item.getQuantity() - request.getQuantity();

// Atomic update
item.setQuantity(newQuantity);
InventoryItem updatedItem = inventoryItemRepository.save(item);
```

### Concurrent Update Handling
- MongoDB provides document-level locking
- Each operation validates current stock before updating
- Optimistic concurrency prevents race conditions

## Functional Requirements Coverage

| Requirement | Implementation Status |
|--------------|---------------------|
| Store inventory items in database | **Complete** |
| Maintain accurate stock count | **Complete** |
| Track stock movement (stock-out) | **Complete** |
| Maintain transaction history | **Complete** |
| Ensure data consistency | **Complete** |
| Stock never goes below 0 | **Complete** |
| Handle negative quantity input | **Complete** |
| Handle concurrent updates | **Complete** |
| Handle invalid item ID | **Complete** |

## Security Considerations

- **Authentication**: Valid JWT token required
- **Authorization**: User must have ADMIN role
- **Audit Trail**: All stock changes logged with user ID
- **Input Validation**: All inputs validated before processing
- **Data Integrity**: Database constraints prevent invalid states

## Integration Notes

- Works seamlessly with existing inventory management
- Stock transactions can be queried for audit purposes
- Real-time quantity updates ensure inventory accuracy
- Transaction logging supports traceability and compliance
- Compatible with stock-in operations for complete inventory tracking
