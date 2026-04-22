package phimind.example.Backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BackendApplication {
		@Autowired
private org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;


	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


@PostConstruct
public void checkDb() {
    System.out.println("🔥 ACTUAL DB USED: " + mongoTemplate.getDb().getName());
}

}
