package phimind.example.Backend.Controller;

import phimind.example.Backend.dto.InventoryItemRequest;
import phimind.example.Backend.dto.InventoryItemResponse;
import phimind.example.Backend.dto.ApiResponse;
import phimind.example.Backend.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

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
    public ResponseEntity<ApiResponse<List<InventoryItemResponse>>> getAllInventoryItems() {
        try {
            List<InventoryItemResponse> items = inventoryService.getAllInventoryItems();
            return ResponseEntity.ok(ApiResponse.success("Inventory items retrieved successfully", items));
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
}
