package com.statpod.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // 1. Basic null/empty check (for all fields)
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // 2. Username validation (alphanumeric with underscores, 4-20 chars)
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    // 3. Password validation (at least 8 chars, 1 uppercase, 1 number, 1 special char)
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    // 4. Email validation
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    // 5. Phone number validation (country specific if needed)
    public static boolean isValidPhoneNumber(String number) {
        // Generic international format validation
        return number != null && number.matches("^\\+?\\d{8,15}$");
    }

    // 6. Simple name validation (letters and spaces)
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]{2,50}$");
    }

    // 7. Confirm password match
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    // 8. Image file extension validation
    public static boolean isValidImageExtension(jakarta.servlet.http.Part imagePart) {
        if (imagePart == null || imagePart.getSubmittedFileName() == null) {
            return false;
        }
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }
}
