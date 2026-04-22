package phimind.example.Backend.Controller;

import phimind.example.Backend.dto.InventoryItemRequest;
import phimind.example.Backend.dto.InventoryItemResponse;
import phimind.example.Backend.dto.StockInRequest;
import phimind.example.Backend.dto.StockOutRequest;
import phimind.example.Backend.dto.ApiResponse;
import phimind.example.Backend.dto.PaginationResponse;
import phimind.example.Backend.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Management", description = "Inventory items CRUD operations and stock management")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create inventory item",
        description = "Creates a new inventory item with initial stock quantity. Requires ADMIN role."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Inventory item created successfully",
            content = @Content(schema = @Schema(implementation = phimind.example.Backend.dto.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input or item already exists"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied - ADMIN role required"
        )
    })
    public ResponseEntity<ApiResponse<InventoryItemResponse>> createInventoryItem(
            @Parameter(description = "Inventory item details", required = true)
            @Valid @RequestBody InventoryItemRequest request) {
        try {
            InventoryItemResponse response = inventoryService.createInventoryItem(request);
            return ResponseEntity.ok(ApiResponse.success("Inventory item created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/items")
    @Operation(
        summary = "Get all inventory items (paginated)",
        description = "Retrieves inventory items with pagination support for efficient data access"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Inventory items retrieved successfully",
            content = @Content(schema = @Schema(implementation = phimind.example.Backend.dto.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid pagination parameters"
        )
    })
    public ResponseEntity<ApiResponse<PaginationResponse<InventoryItemResponse>>> getAllInventoryItems(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        try {
            PaginationResponse<InventoryItemResponse> paginatedItems = inventoryService.getAllInventoryItemsPaginated(page, size);
            return ResponseEntity.ok(ApiResponse.success("Inventory items retrieved successfully", paginatedItems));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> getInventoryItemById(@PathVariable String id) {
        try {
            InventoryItemResponse response = inventoryService.getInventoryItemById(id);
            return ResponseEntity.ok(ApiResponse.success("Inventory item retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> updateInventoryItem(
            @PathVariable String id, 
            @Valid @RequestBody InventoryItemRequest request) {
        try {
            InventoryItemResponse response = inventoryService.updateInventoryItem(id, request);
            return ResponseEntity.ok(ApiResponse.success("Inventory item updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteInventoryItem(@PathVariable String id) {
        try {
            inventoryService.deleteInventoryItem(id);
            return ResponseEntity.ok(ApiResponse.success("Inventory item deleted successfully", "Item with id " + id + " has been deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/items/{id}/stock-in")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Add stock to inventory item",
        description = "Adds stock to an existing inventory item. Requires ADMIN role."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Stock added successfully",
            content = @Content(schema = @Schema(implementation = phimind.example.Backend.dto.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input or item not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied - ADMIN role required"
        )
    })
    public ResponseEntity<ApiResponse<InventoryItemResponse>> addStock(
            @Parameter(description = "Inventory item ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id, 
            @Parameter(description = "Stock addition details", required = true)
            @Valid @RequestBody StockInRequest request) {
        try {
            // Get current user ID from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            InventoryItemResponse response = inventoryService.addStock(id, request, userId);
            return ResponseEntity.ok(ApiResponse.success("Stock added successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/items/{id}/stock-out")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Remove stock from inventory item",
        description = "Reduces stock from an inventory item. Ensures stock does not go below zero. Requires ADMIN role."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Stock removed successfully",
            content = @Content(schema = @Schema(implementation = phimind.example.Backend.dto.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input, insufficient stock, or item not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Access denied - ADMIN role required"
        )
    })
    public ResponseEntity<ApiResponse<InventoryItemResponse>> removeStock(
            @Parameter(description = "Inventory item ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id, 
            @Parameter(description = "Stock removal details", required = true)
            @Valid @RequestBody StockOutRequest request) {
        try {
            // Get current user ID from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            InventoryItemResponse response = inventoryService.removeStock(id, request, userId);
            return ResponseEntity.ok(ApiResponse.success("Stock removed successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
