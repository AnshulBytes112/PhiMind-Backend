package phimind.example.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    
    private String userId;
    private String email;
    private String role;
    private String token;
    private String message;
    
    public static RegistrationResponse success(String userId, String email, String role, String token) {
        RegistrationResponse response = new RegistrationResponse();
        response.setUserId(userId);
        response.setEmail(email);
        response.setRole(role);
        response.setToken(token);
        response.setMessage("User registered successfully");
        return response;
    }
    
    public static RegistrationResponse error(String message) {
        RegistrationResponse response = new RegistrationResponse();
        response.setMessage(message);
        return response;
    }
}
