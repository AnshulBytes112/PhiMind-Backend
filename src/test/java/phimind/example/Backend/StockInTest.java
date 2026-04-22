package phimind.example.Backend;

import phimind.example.Backend.Controller.InventoryController;
import phimind.example.Backend.Service.InventoryService;
import phimind.example.Backend.dto.StockInRequest;
import phimind.example.Backend.dto.InventoryItemResponse;
import phimind.example.Backend.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockInTest {
    
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private InventoryController inventoryController;
    
    private StockInRequest stockInRequest;
    private InventoryItemResponse inventoryItemResponse;
    
    @BeforeEach
    void setUp() {
        stockInRequest = new StockInRequest();
        stockInRequest.setQuantity(25);
        stockInRequest.setReason("New stock arrival");
        
        inventoryItemResponse = new InventoryItemResponse();
        inventoryItemResponse.setId("123");
        inventoryItemResponse.setName("Test Item");
        inventoryItemResponse.setQuantity(75); 
        inventoryItemResponse.setDescription("Test Description");
        inventoryItemResponse.setPrice(99.99);
    }
    
    @Test
    void testAddStock_Success() {
        // Mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user-id");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            when(inventoryService.addStock(eq("123"), any(StockInRequest.class), any(String.class)))
                    .thenReturn(inventoryItemResponse);
            
            ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                    inventoryController.addStock("123", stockInRequest);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Stock added successfully", response.getBody().getMessage());
            assertEquals(75, response.getBody().getData().getQuantity());
            verify(inventoryService, times(1)).addStock(eq("123"), any(StockInRequest.class), any(String.class));
        }
    }
    
    @Test
    void testAddStock_ItemNotFound() {
        // Mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user-id");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            when(inventoryService.addStock(eq("999"), any(StockInRequest.class), any(String.class)))
                    .thenThrow(new RuntimeException("Item not found with id: 999"));
            
            ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                    inventoryController.addStock("999", stockInRequest);
            
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Item not found with id: 999", response.getBody().getMessage());
            verify(inventoryService, times(1)).addStock(eq("999"), any(StockInRequest.class), any(String.class));
        }
    }
    
    @Test
    void testAddStock_InvalidQuantity() {
        StockInRequest invalidRequest = new StockInRequest();
        invalidRequest.setQuantity(-5); // Invalid quantity
        invalidRequest.setReason("Invalid test");
        
        // This would be handled by validation annotations
        ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                inventoryController.addStock("123", invalidRequest);
        
        // Validation error would result in bad request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
