package phimind.example.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemRequest {
    
    @NotBlank(message = "Item name is required")
    private String name;
    
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private int quantity;
    
    private String description;
    
    private double price;
}
