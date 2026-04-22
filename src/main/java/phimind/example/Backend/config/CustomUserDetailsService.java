package phimind.example.Backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import phimind.example.Backend.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        phimind.example.Backend.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_" + user.getRole().name());

        return User.builder()
                .username(user.getId())
                .password(user.getPassword())
                .authorities(authorities.toArray(new String[0]))
                .build();
    }
}
