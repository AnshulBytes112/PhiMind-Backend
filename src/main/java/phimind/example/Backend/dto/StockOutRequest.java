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
@Schema(description = "Request object for removing stock from inventory items")
public class StockOutRequest {
    
    @Schema(description = "Quantity to remove (must be greater than 0)", example = "15", required = true)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
    
    @Schema(description = "Reason for removing stock", example = "Customer order fulfillment")
    private String reason;
}
