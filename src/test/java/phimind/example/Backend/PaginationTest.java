package phimind.example.Backend;

import phimind.example.Backend.Controller.InventoryController;
import phimind.example.Backend.Service.InventoryService;
import phimind.example.Backend.dto.InventoryItemResponse;
import phimind.example.Backend.dto.ApiResponse;
import phimind.example.Backend.dto.PaginationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaginationTest {
    
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private InventoryController inventoryController;
    
    private PaginationResponse<InventoryItemResponse> paginationResponse;
    private List<InventoryItemResponse> mockItems;
    
    @BeforeEach
    void setUp() {
        // Create mock inventory items
        InventoryItemResponse item1 = new InventoryItemResponse();
        item1.setId("1");
        item1.setName("Item 1");
        item1.setQuantity(10);
        
        InventoryItemResponse item2 = new InventoryItemResponse();
        item2.setId("2");
        item2.setName("Item 2");
        item2.setQuantity(20);
        
        mockItems = Arrays.asList(item1, item2);
        
        // Create pagination response
        paginationResponse = new PaginationResponse<>(
                mockItems,
                0,  // currentPage
                10, // pageSize
                25  // totalElements
        );
    }
    
    @Test
    void testGetAllInventoryItemsPaginated_Success() {
        when(inventoryService.getAllInventoryItemsPaginated(0, 10))
                .thenReturn(paginationResponse);
        
        ResponseEntity<ApiResponse<PaginationResponse<InventoryItemResponse>>> response = 
                inventoryController.getAllInventoryItems(0, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        
        PaginationResponse<InventoryItemResponse> data = response.getBody().getData();
        assertEquals(2, data.getContent().size());
        assertEquals(0, data.getCurrentPage());
        assertEquals(10, data.getPageSize());
        assertEquals(25, data.getTotalElements());
        assertEquals(3, data.getTotalPages()); // 25 / 10 = 2.5 -> 3 pages
        assertTrue(data.isFirst());
        assertFalse(data.isLast());
        
        verify(inventoryService, times(1)).getAllInventoryItemsPaginated(0, 10);
    }
    
    @Test
    void testGetAllInventoryItemsPaginated_WithDefaultParameters() {
        when(inventoryService.getAllInventoryItemsPaginated(0, 10))
                .thenReturn(paginationResponse);
        
        ResponseEntity<ApiResponse<PaginationResponse<InventoryItemResponse>>> response = 
                inventoryController.getAllInventoryItems(0, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        
        verify(inventoryService, times(1)).getAllInventoryItemsPaginated(0, 10);
    }
    
    @Test
    void testGetAllInventoryItemsPaginated_LastPage() {
        // Create last page response
        PaginationResponse<InventoryItemResponse> lastPageResponse = new PaginationResponse<>(
                Arrays.asList(mockItems.get(0)), // Only one item on last page
                2,  // currentPage (page 2)
                10, // pageSize
                25  // totalElements
        );
        
        when(inventoryService.getAllInventoryItemsPaginated(2, 10))
                .thenReturn(lastPageResponse);
        
        ResponseEntity<ApiResponse<PaginationResponse<InventoryItemResponse>>> response = 
                inventoryController.getAllInventoryItems(2, 10);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        
        PaginationResponse<InventoryItemResponse> data = response.getBody().getData();
        assertEquals(1, data.getContent().size());
        assertEquals(2, data.getCurrentPage());
        assertFalse(data.isFirst());
        assertTrue(data.isLast());
        
        verify(inventoryService, times(1)).getAllInventoryItemsPaginated(2, 10);
    }
    
    @Test
    void testGetAllInventoryItemsPaginated_ServiceError() {
        when(inventoryService.getAllInventoryItemsPaginated(anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database error"));
        
        ResponseEntity<ApiResponse<PaginationResponse<InventoryItemResponse>>> response = 
                inventoryController.getAllInventoryItems(0, 10);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Database error", response.getBody().getMessage());
        verify(inventoryService, times(1)).getAllInventoryItemsPaginated(0, 10);
    }
}
