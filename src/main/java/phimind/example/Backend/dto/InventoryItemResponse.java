package phimind.example.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemResponse {
    
    private String id;
    private String name;
    private int quantity;
    private String description;
    private double price;
    
    public static InventoryItemResponse fromInventoryItem(phimind.example.Backend.model.InventoryItem item) {
        InventoryItemResponse response = new InventoryItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setQuantity(item.getQuantity());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        return response;
    }
}
