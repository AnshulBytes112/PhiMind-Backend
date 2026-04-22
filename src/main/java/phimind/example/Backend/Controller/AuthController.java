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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration and login management")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account and returns a JWT token for authentication"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = phimind.example.Backend.dto.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input or user already exists"
        )
    })
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerUser(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = userService.registerUser(request);
        
        if (response.getToken() != null) {
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
        }
    }
    
    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Validates user credentials and returns a JWT token for authentication"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User successfully authenticated",
            content = @Content(schema = @Schema(implementation = phimind.example.Backend.dto.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid credentials"
        )
    })
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(
            @Parameter(description = "User login credentials", required = true)
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.loginUser(request);
        
        if (response.getToken() != null) {
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
        }
    }
}
