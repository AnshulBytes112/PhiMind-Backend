package phimind.example.Backend;

import phimind.example.Backend.Controller.AuthController;
import phimind.example.Backend.Service.UserService;
import phimind.example.Backend.dto.LoginRequest;
import phimind.example.Backend.dto.LoginResponse;
import phimind.example.Backend.dto.RegistrationRequest;
import phimind.example.Backend.dto.RegistrationResponse;
import phimind.example.Backend.dto.ApiResponse;
import phimind.example.Backend.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private AuthController authController;
    
    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;
    private RegistrationResponse registrationResponse;
    private LoginResponse loginResponse;
    
    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRole(Role.STAFF);
        
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123!");
    }
    
    @Test
    void testRegisterUser_Success() {
        registrationResponse = RegistrationResponse.success("123", "test@example.com", "STAFF", "jwt-token");
        
        when(userService.registerUser(any(RegistrationRequest.class))).thenReturn(registrationResponse);
        
        ResponseEntity<ApiResponse<RegistrationResponse>> response = authController.registerUser(registrationRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("jwt-token", response.getBody().getData().getToken());
        verify(userService, times(1)).registerUser(any(RegistrationRequest.class));
    }
    
    @Test
    void testRegisterUser_Failure() {
        registrationResponse = RegistrationResponse.error("Email already exists");
        
        when(userService.registerUser(any(RegistrationRequest.class))).thenReturn(registrationResponse);
        
        ResponseEntity<ApiResponse<RegistrationResponse>> response = authController.registerUser(registrationRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody().getMessage());
        verify(userService, times(1)).registerUser(any(RegistrationRequest.class));
    }
    
    @Test
    void testLoginUser_Success() {
        loginResponse = LoginResponse.success("123", "test@example.com", "STAFF", "jwt-token");
        
        when(userService.loginUser(any(LoginRequest.class))).thenReturn(loginResponse);
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.loginUser(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("jwt-token", response.getBody().getData().getToken());
        verify(userService, times(1)).loginUser(any(LoginRequest.class));
    }
    
    @Test
    void testLoginUser_Failure() {
        loginResponse = LoginResponse.error("Invalid password");
        
        when(userService.loginUser(any(LoginRequest.class))).thenReturn(loginResponse);
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.loginUser(loginRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid password", response.getBody().getMessage());
        verify(userService, times(1)).loginUser(any(LoginRequest.class));
    }
}
