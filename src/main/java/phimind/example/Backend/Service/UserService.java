package phimind.example.Backend.Service;

import phimind.example.Backend.dto.RegistrationRequest;
import phimind.example.Backend.dto.RegistrationResponse;
import phimind.example.Backend.dto.LoginRequest;
import phimind.example.Backend.dto.LoginResponse;
import phimind.example.Backend.model.User;
import phimind.example.Backend.model.Role;
import phimind.example.Backend.Repository.UserRepository;
import phimind.example.Backend.util.JwtUtil;
import phimind.example.Backend.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    public RegistrationResponse registerUser(RegistrationRequest request) {
        if (!isValidEmail(request.getEmail())) {
            return RegistrationResponse.error("Invalid email format");
        }
        
        if (!PasswordValidator.isValid(request.getPassword())) {
            return RegistrationResponse.error(PasswordValidator.getValidationMessage());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            return RegistrationResponse.error("Email already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        User savedUser = userRepository.save(user);
        
        String token = jwtUtil.generateToken(savedUser);
        
        return RegistrationResponse.success(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            token
        );
    }
    
    public LoginResponse loginUser(LoginRequest request) {
        if (!isValidEmail(request.getEmail())) {
            return LoginResponse.error("Invalid email format");
        }
        
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return LoginResponse.error("Email not found");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.error("Invalid password");
        }
        
        String token = jwtUtil.generateToken(user);
        
        return LoginResponse.success(
            user.getId(),
            user.getEmail(),
            user.getRole().name(),
            token
        );
    }
    
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
