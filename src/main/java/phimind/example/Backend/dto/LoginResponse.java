package phimind.example.Backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String userId;
    private String email;
    private String role;
    private String token;
    private String message;
    
    public static LoginResponse success(String userId, String email, String role, String token) {
        LoginResponse response = new LoginResponse();
        response.setUserId(userId);
        response.setEmail(email);
        response.setRole(role);
        response.setToken(token);
        response.setMessage("Login successful");
        return response;
    }
    
    public static LoginResponse error(String message) {
        LoginResponse response = new LoginResponse();
        response.setMessage(message);
        return response;
    }
}
