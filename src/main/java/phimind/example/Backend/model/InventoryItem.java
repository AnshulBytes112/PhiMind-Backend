package phimind.example.Backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory_items")
public class InventoryItem {
    
    @Id
    private String id;
    
    @NotBlank(message = "Item name is required")
    @Indexed(unique = true)
    private String name;
    
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private int quantity;
    
    private String description;
    
    private double price;
    
    public InventoryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
