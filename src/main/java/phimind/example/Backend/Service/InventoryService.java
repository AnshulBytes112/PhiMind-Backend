package phimind.example.Backend.Service;

import phimind.example.Backend.Repository.InventoryItemRepository;
import phimind.example.Backend.model.InventoryItem;
import phimind.example.Backend.dto.InventoryItemRequest;
import phimind.example.Backend.dto.InventoryItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    public InventoryItemResponse createInventoryItem(InventoryItemRequest request) {
        if (inventoryItemRepository.existsByName(request.getName())) {
            throw new RuntimeException("Item with name '" + request.getName() + "' already exists");
        }
        
        InventoryItem item = new InventoryItem();
        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        
        InventoryItem savedItem = inventoryItemRepository.save(item);
        return InventoryItemResponse.fromInventoryItem(savedItem);
    }
    
    public List<InventoryItemResponse> getAllInventoryItems() {
        return inventoryItemRepository.findAll()
                .stream()
                .map(InventoryItemResponse::fromInventoryItem)
                .collect(Collectors.toList());
    }
    
    public InventoryItemResponse getInventoryItemById(String id) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return InventoryItemResponse.fromInventoryItem(item);
    }
    
    public InventoryItemResponse updateInventoryItem(String id, InventoryItemRequest request) {
        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        
        InventoryItem updatedItem = inventoryItemRepository.save(item);
        return InventoryItemResponse.fromInventoryItem(updatedItem);
    }
    
    public void deleteInventoryItem(String id) {
        if (!inventoryItemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        inventoryItemRepository.deleteById(id);
    }
}
