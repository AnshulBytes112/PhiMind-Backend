package phimind.example.Backend.Repository;

import phimind.example.Backend.model.StockTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends MongoRepository<StockTransaction, String> {
    
    List<StockTransaction> findByItemIdOrderByCreatedAtDesc(String itemId);
    
    List<StockTransaction> findByItemIdAndTransactionTypeOrderByCreatedAtDesc(String itemId, StockTransaction.TransactionType transactionType);
}
