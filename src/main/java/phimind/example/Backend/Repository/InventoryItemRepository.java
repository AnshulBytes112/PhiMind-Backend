package phimind.example.Backend.Repository;

import phimind.example.Backend.model.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemRepository extends MongoRepository<InventoryItem, String> {
    
    Optional<InventoryItem> findByName(String name);
    
    boolean existsByName(String name);
}
