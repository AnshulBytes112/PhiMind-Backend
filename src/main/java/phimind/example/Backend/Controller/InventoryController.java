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

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> createInventoryItem(@Valid @RequestBody InventoryItemRequest request) {
        try {
            InventoryItemResponse response = inventoryService.createInventoryItem(request);
            return ResponseEntity.ok(ApiResponse.success("Inventory item created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<PaginationResponse<InventoryItemResponse>>> getAllInventoryItems(
            @RequestParam(defaultValue = "0") int page,
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
    public ResponseEntity<ApiResponse<InventoryItemResponse>> addStock(
            @PathVariable String id, 
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
    public ResponseEntity<ApiResponse<InventoryItemResponse>> removeStock(
            @PathVariable String id, 
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
