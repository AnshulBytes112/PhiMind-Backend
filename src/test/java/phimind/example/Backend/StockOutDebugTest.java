package phimind.example.Backend;

import phimind.example.Backend.Service.InventoryService;
import phimind.example.Backend.Repository.InventoryItemRepository;
import phimind.example.Backend.Repository.StockTransactionRepository;
import phimind.example.Backend.model.InventoryItem;
import phimind.example.Backend.dto.StockOutRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockOutDebugTest {
    
    @Mock
    private InventoryItemRepository inventoryItemRepository;
    
    @Mock
    private StockTransactionRepository stockTransactionRepository;
    
    @InjectMocks
    private InventoryService inventoryService;
    
    @Test
    void testStockOutLogic() {
        // Create test item with 25 quantity
        InventoryItem item = new InventoryItem();
        item.setId("123");
        item.setName("Test Item");
        item.setQuantity(25);
        
        // Mock repository to return our test item
        when(inventoryItemRepository.findById("123")).thenReturn(java.util.Optional.of(item));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> {
            InventoryItem savedItem = invocation.getArgument(0);
            System.out.println("Saved item quantity: " + savedItem.getQuantity());
            return savedItem;
        });
        
        // Create stock-out request for 15
        StockOutRequest request = new StockOutRequest();
        request.setQuantity(15);
        request.setReason("Test removal");
        
        // Call the method
        var response = inventoryService.removeStock("123", request, "test-user");
        
        // Verify the result
        assertEquals(10, response.getQuantity()); // 25 - 15 = 10
        System.out.println("Final quantity: " + response.getQuantity());
        
        // Verify the repository was called
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }
}
