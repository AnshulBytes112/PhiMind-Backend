# Pagination API Documentation

## Overview
This document describes the pagination functionality for inventory item retrieval, providing efficient data access for large datasets.

## Base URL
```
http://localhost:8080/inventory
```

## Authentication
All endpoints require JWT authentication.

## Paginated Endpoint

### GET /inventory/items/paginated

**Description:** Retrieves inventory items with pagination support for efficient data access.

**Authorization:** Any authenticated user

**Query Parameters:**
- `page` (integer, optional): Page number (0-based, default: 0)
- `size` (integer, optional): Number of items per page (default: 10)

**Response (Success):**
```json
{
  "success": true,
  "message": "Inventory items retrieved successfully",
  "data": {
    "content": [
      {
        "id": "507f1f77bcf86cd799439011",
        "name": "Laptop Dell XPS 15",
        "quantity": 25,
        "description": "High-performance laptop for developers",
        "price": 1499.99
      },
      {
        "id": "507f1f77bcf86cd799439012",
        "name": "Wireless Mouse",
        "quantity": 50,
        "description": "Ergonomic wireless mouse",
        "price": 29.99
      }
    ],
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false
  },
  "timestamp": "2026-04-22T20:10:00.123",
  "statusCode": 200
}
```

## Response Fields

| Field | Type | Description |
|--------|------|-------------|
| `content` | Array | Array of inventory items for the current page |
| `currentPage` | Integer | Current page number (0-based) |
| `pageSize` | Integer | Number of items per page |
| `totalElements` | Long | Total number of items in the database |
| `totalPages` | Integer | Total number of pages |
| `first` | Boolean | `true` if this is the first page |
| `last` | Boolean | `true` if this is the last page |

## Usage Examples

### Basic Pagination
```bash
GET /inventory/items/paginated?page=0&size=10
Authorization: Bearer <jwt_token>
```

### Get Second Page
```bash
GET /inventory/items/paginated?page=1&size=10
Authorization: Bearer <jwt_token>
```

### Custom Page Size
```bash
GET /inventory/items/paginated?page=0&size=5
Authorization: Bearer <jwt_token>
```

### Default Parameters (page=0, size=10)
```bash
GET /inventory/items/paginated
Authorization: Bearer <jwt_token>
```

## Response Examples

### First Page
```json
{
  "success": true,
  "message": "Inventory items retrieved successfully",
  "data": {
    "content": [...],
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false
  }
}
```

### Middle Page
```json
{
  "success": true,
  "message": "Inventory items retrieved successfully",
  "data": {
    "content": [...],
    "currentPage": 1,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": false,
    "last": false
  }
}
```

### Last Page
```json
{
  "success": true,
  "message": "Inventory items retrieved successfully",
  "data": {
    "content": [...],
    "currentPage": 2,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": false,
    "last": true
  }
}
```

### Empty Dataset
```json
{
  "success": true,
  "message": "Inventory items retrieved successfully",
  "data": {
    "content": [],
    "currentPage": 0,
    "pageSize": 10,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "last": true
  }
}
```

## Performance Benefits

### Before Pagination
- **GET /inventory/items** returns ALL items
- **Memory Usage:** High (loads all items into memory)
- **Response Time:** Slow with large datasets
- **Network Load:** Large payload size

### After Pagination
- **GET /inventory/items/paginated** returns subset of items
- **Memory Usage:** Low (only loads requested page)
- **Response Time:** Fast and consistent
- **Network Load:** Controlled payload size

## Client-Side Implementation

### JavaScript/TypeScript Example
```javascript
class InventoryAPI {
  constructor(baseUrl, authToken) {
    this.baseUrl = baseUrl;
    this.authToken = authToken;
  }

  async getInventoryItems(page = 0, size = 10) {
    const response = await fetch(
      `${this.baseUrl}/inventory/items/paginated?page=${page}&size=${size}`,
      {
        headers: {
          'Authorization': `Bearer ${this.authToken}`,
          'Content-Type': 'application/json'
        }
      }
    );
    
    const result = await response.json();
    return result.data;
  }

  async getAllPages(size = 10) {
    const firstPage = await this.getInventoryItems(0, size);
    const allItems = [...firstPage.content];
    
    for (let page = 1; page < firstPage.totalPages; page++) {
      const pageData = await this.getInventoryItems(page, size);
      allItems.push(...pageData.content);
    }
    
    return allItems;
  }
}

// Usage
const api = new InventoryAPI('http://localhost:8080', 'your-jwt-token');

// Get first page with 10 items
const page1 = await api.getInventoryItems(0, 10);

// Get second page with 5 items
const page2 = await api.getInventoryItems(1, 5);

// Get all items (use with caution for large datasets)
const allItems = await api.getAllPages(20);
```

### React Component Example
```jsx
import React, { useState, useEffect } from 'react';

function InventoryList() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size] = useState(10);

  useEffect(() => {
    fetchInventoryItems();
  }, [page]);

  const fetchInventoryItems = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('jwtToken');
      const response = await fetch(
        `http://localhost:8080/inventory/items/paginated?page=${page}&size=${size}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );
      const result = await response.json();
      setData(result.data);
    } catch (error) {
      console.error('Error fetching inventory:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading || !data) return <div>Loading...</div>;

  return (
    <div>
      <h2>Inventory Items (Page {data.currentPage + 1} of {data.totalPages})</h2>
      
      <ul>
        {data.content.map(item => (
          <li key={item.id}>
            {item.name} - Quantity: {item.quantity}
          </li>
        ))}
      </ul>
      
      <div>
        <button 
          disabled={data.first} 
          onClick={() => setPage(page - 1)}
        >
          Previous
        </button>
        
        <span>Page {page + 1} of {data.totalPages}</span>
        
        <button 
          disabled={data.last} 
          onClick={() => setPage(page + 1)}
        >
          Next
        </button>
      </div>
      
      <p>Total items: {data.totalElements}</p>
    </div>
  );
}
```

## Backend Implementation Details

### Service Layer
```java
public PaginationResponse<InventoryItemResponse> getAllInventoryItemsPaginated(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<InventoryItem> itemPage = inventoryItemRepository.findAll(pageable);
    
    List<InventoryItemResponse> content = itemPage.getContent()
            .stream()
            .map(InventoryItemResponse::fromInventoryItem)
            .collect(Collectors.toList());
    
    return new PaginationResponse<>(
            content,
            itemPage.getNumber(),
            itemPage.getSize(),
            itemPage.getTotalElements()
    );
}
```

### Database Query Optimization
- Uses MongoDB's `skip()` and `limit()` for efficient pagination
- Only retrieves required documents from database
- Maintains sort order for consistent pagination

## Best Practices

### Recommended Page Sizes
- **Mobile Apps:** 5-10 items per page
- **Web Applications:** 10-25 items per page
- **Data Tables:** 25-50 items per page

### Performance Considerations
- Use consistent page sizes across requests
- Implement caching for frequently accessed pages
- Consider infinite scroll for better UX in web applications
- Monitor database performance with large datasets

### Error Handling
- Validate page parameters (page >= 0, size > 0)
- Handle empty datasets gracefully
- Provide meaningful error messages
- Implement retry logic for network failures

## Comparison with Non-Paginated Endpoint

| Feature | GET /inventory/items | GET /inventory/items/paginated |
|---------|---------------------|-------------------------------|
| **Performance** | Slow with large datasets | Fast and consistent |
| **Memory Usage** | High (loads all items) | Low (loads only requested page) |
| **Network Load** | Large payload | Controlled payload size |
| **User Experience** | Long loading times | Fast, progressive loading |
| **Scalability** | Poor | Excellent |
| **Use Case** | Small datasets (< 100 items) | Large datasets (100+ items) |

## Migration Guide

### Step 1: Update Client Code
Replace:
```javascript
const response = await fetch('/inventory/items');
const items = await response.json();
```

With:
```javascript
const response = await fetch('/inventory/items/paginated?page=0&size=10');
const result = await response.json();
const items = result.data.content;
```

### Step 2: Add Pagination Controls
Implement previous/next buttons or page number navigation.

### Step 3: Handle Edge Cases
- Empty datasets
- Single page results
- Invalid page parameters

### Step 4: Optimize Page Size
Choose appropriate page size based on your use case and performance requirements.
