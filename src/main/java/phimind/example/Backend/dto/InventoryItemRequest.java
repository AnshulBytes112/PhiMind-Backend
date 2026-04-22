package phimind.example.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating or updating inventory items")
public class InventoryItemRequest {
    
    @Schema(description = "Name of the inventory item", example = "Laptop Dell XPS 15")
    @NotBlank(message = "Item name is required")
    private String name;
    
    @Schema(description = "Initial quantity of the item", example = "25")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private int quantity;
    
    @Schema(description = "Description of the inventory item", example = "High-performance laptop for developers")
    private String description;
    
    @Schema(description = "Price of the inventory item", example = "1499.99")
    private double price;
}
