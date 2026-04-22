package phimind.example.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for adding stock to inventory items")
public class StockInRequest {
    
    @Schema(description = "Quantity to add (must be greater than 0)", example = "25", required = true)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
    
    @Schema(description = "Reason for adding stock", example = "New stock arrival from supplier")
    private String reason;
}
