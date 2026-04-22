package phimind.example.Backend.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    
    public static boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }
    
    public static String getValidationMessage() {
        return "Password must be at least 8 characters long and contain " +
               "at least one uppercase letter, one lowercase letter, one number, " +
               "and one special character (@#$%^&+=!)";
    }
}
