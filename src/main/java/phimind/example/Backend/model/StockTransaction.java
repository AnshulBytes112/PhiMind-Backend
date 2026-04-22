package phimind.example.Backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_transactions")
public class StockTransaction {
    
    @Id
    private String id;
    
    @Field("item_id")
    private String itemId;
    
    @Field("transaction_type")
    private TransactionType transactionType;
    
    private Integer quantity;
    
    private String reason;
    
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @Field("created_by")
    private String createdBy;
    
    public enum TransactionType {
        STOCK_IN,
        STOCK_OUT
    }
    
    public StockTransaction(String itemId, TransactionType transactionType, Integer quantity, String reason, String createdBy) {
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.reason = reason;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
}
