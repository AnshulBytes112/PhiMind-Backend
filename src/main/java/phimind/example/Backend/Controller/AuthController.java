package phimind.example.Backend.Controller;

import phimind.example.Backend.dto.RegistrationRequest;
import phimind.example.Backend.dto.RegistrationResponse;
import phimind.example.Backend.dto.LoginRequest;
import phimind.example.Backend.dto.LoginResponse;
import phimind.example.Backend.dto.ApiResponse;
import phimind.example.Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerUser(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = userService.registerUser(request);
        
        if (response.getToken() != null) {
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.loginUser(request);
        
        if (response.getToken() != null) {
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
        }
    }
}
