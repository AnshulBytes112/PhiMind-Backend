package phimind.example.Backend;

import phimind.example.Backend.Controller.InventoryController;
import phimind.example.Backend.Service.InventoryService;
import phimind.example.Backend.dto.InventoryItemRequest;
import phimind.example.Backend.dto.InventoryItemResponse;
import phimind.example.Backend.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {
    
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private InventoryController inventoryController;
    
    private InventoryItemRequest inventoryItemRequest;
    private InventoryItemResponse inventoryItemResponse;
    
    @BeforeEach
    void setUp() {
        inventoryItemRequest = new InventoryItemRequest();
        inventoryItemRequest.setName("Test Item");
        inventoryItemRequest.setQuantity(10);
        inventoryItemRequest.setDescription("Test Description");
        inventoryItemRequest.setPrice(99.99);
        
        inventoryItemResponse = new InventoryItemResponse();
        inventoryItemResponse.setId("123");
        inventoryItemResponse.setName("Test Item");
        inventoryItemResponse.setQuantity(10);
        inventoryItemResponse.setDescription("Test Description");
        inventoryItemResponse.setPrice(99.99);
    }
    
    @Test
    void testCreateInventoryItem_Success() {
        when(inventoryService.createInventoryItem(any(InventoryItemRequest.class))).thenReturn(inventoryItemResponse);
        
        ResponseEntity<ApiResponse<InventoryItemResponse>> response = inventoryController.createInventoryItem(inventoryItemRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Test Item", response.getBody().getData().getName());
        assertEquals(10, response.getBody().getData().getQuantity());
        verify(inventoryService, times(1)).createInventoryItem(any(InventoryItemRequest.class));
    }
    
    @Test
    void testCreateInventoryItem_Failure() {
        when(inventoryService.createInventoryItem(any(InventoryItemRequest.class)))
                .thenThrow(new RuntimeException("Item already exists"));
        
        ResponseEntity<ApiResponse<InventoryItemResponse>> response = inventoryController.createInventoryItem(inventoryItemRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Item already exists", response.getBody().getMessage());
        verify(inventoryService, times(1)).createInventoryItem(any(InventoryItemRequest.class));
    }
    
    @Test
    void testGetInventoryItemById_Success() {
        when(inventoryService.getInventoryItemById("123")).thenReturn(inventoryItemResponse);
        
        ResponseEntity<ApiResponse<InventoryItemResponse>> response = inventoryController.getInventoryItemById("123");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Test Item", response.getBody().getData().getName());
        verify(inventoryService, times(1)).getInventoryItemById("123");
    }
    
    @Test
    void testGetInventoryItemById_NotFound() {
        when(inventoryService.getInventoryItemById("123"))
                .thenThrow(new RuntimeException("Item not found"));
        
        ResponseEntity<ApiResponse<InventoryItemResponse>> response = inventoryController.getInventoryItemById("123");
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Item not found", response.getBody().getMessage());
        verify(inventoryService, times(1)).getInventoryItemById("123");
    }
}
