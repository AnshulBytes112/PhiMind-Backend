package phimind.example.Backend;

import phimind.example.Backend.Controller.InventoryController;
import phimind.example.Backend.Service.InventoryService;
import phimind.example.Backend.dto.StockOutRequest;
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
public class StockOutTest {
    
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private InventoryController inventoryController;
    
    private StockOutRequest stockOutRequest;
    private InventoryItemResponse inventoryItemResponse;
    
    @BeforeEach
    void setUp() {
        stockOutRequest = new StockOutRequest();
        stockOutRequest.setQuantity(15);
        stockOutRequest.setReason("Customer order fulfillment");
        
        inventoryItemResponse = new InventoryItemResponse();
        inventoryItemResponse.setId("123");
        inventoryItemResponse.setName("Test Item");
        inventoryItemResponse.setQuantity(10); // 25 - 15 = 10
        inventoryItemResponse.setDescription("Test Description");
        inventoryItemResponse.setPrice(99.99);
    }
    
    @Test
    void testRemoveStock_Success() {
        // Mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user-id");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            when(inventoryService.removeStock(eq("123"), any(StockOutRequest.class), any(String.class)))
                    .thenReturn(inventoryItemResponse);
            
            ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                    inventoryController.removeStock("123", stockOutRequest);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Stock removed successfully", response.getBody().getMessage());
            assertEquals(10, response.getBody().getData().getQuantity());
            verify(inventoryService, times(1)).removeStock(eq("123"), any(StockOutRequest.class), any(String.class));
        }
    }
    
    @Test
    void testRemoveStock_ItemNotFound() {
        // Mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user-id");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            when(inventoryService.removeStock(eq("999"), any(StockOutRequest.class), any(String.class)))
                    .thenThrow(new RuntimeException("Item not found with id: 999"));
            
            ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                    inventoryController.removeStock("999", stockOutRequest);
            
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Item not found with id: 999", response.getBody().getMessage());
            verify(inventoryService, times(1)).removeStock(eq("999"), any(StockOutRequest.class), any(String.class));
        }
    }
    
    @Test
    void testRemoveStock_InsufficientStock() {
        // Mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user-id");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            when(inventoryService.removeStock(eq("123"), any(StockOutRequest.class), any(String.class)))
                    .thenThrow(new RuntimeException("Insufficient stock. Available: 10, Requested: 15"));
            
            ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                    inventoryController.removeStock("123", stockOutRequest);
            
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Insufficient stock. Available: 10, Requested: 15", response.getBody().getMessage());
            verify(inventoryService, times(1)).removeStock(eq("123"), any(StockOutRequest.class), any(String.class));
        }
    }
    
    @Test
    void testRemoveStock_InvalidQuantity() {
        StockOutRequest invalidRequest = new StockOutRequest();
        invalidRequest.setQuantity(0); // Invalid quantity (not > 0)
        invalidRequest.setReason("Invalid test");
        
        // Mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user-id");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            
            // Mock the service to throw exception for invalid quantity
            when(inventoryService.removeStock(eq("123"), any(StockOutRequest.class), any(String.class)))
                    .thenThrow(new RuntimeException("Quantity must be greater than 0"));
            
            ResponseEntity<ApiResponse<InventoryItemResponse>> response = 
                    inventoryController.removeStock("123", invalidRequest);
            
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Quantity must be greater than 0", response.getBody().getMessage());
        }
    }
}
