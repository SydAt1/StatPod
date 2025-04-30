package com.statpod.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.statpod.config.DbConfig;
import com.statpod.model.PodcastUserModel;
import com.statpod.util.ImageUtil;
import com.statpod.util.PasswordUtil;
import com.statpod.util.ValidationUtil;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

/**
 * RegisterService handles the registration of new podcast users.
 * It manages database interactions for user registration and provides
 * validation functionality for the registration process.
 */
public class RegisterService {
    private Connection dbConn;
    private final ImageUtil imageUtil;
    
    /**
     * Constructor initializes the database connection and required utilities.
     */
    public RegisterService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
        this.imageUtil = new ImageUtil();
    }
    
    /**
     * Registers a new podcast user in the database.
     *
     * @param userModel the user details to be registered
     * @return Boolean indicating the success of the operation
     */
    public Boolean addUser(PodcastUserModel userModel) {
        if (dbConn == null) {
            System.err.println("Database connection is not available.");
            return null;
        }
        String checkUserQuery = "SELECT Username FROM Users WHERE Username = ? OR Email_ID = ?";
        String insertQuery = "INSERT INTO Users (Username, DisplayName, Email_ID, Password, ImageUrl, FavoriteGenre) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement checkStmt = dbConn.prepareStatement(checkUserQuery);
             PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
            
            // Check if the user already exists
            checkStmt.setString(1, userModel.getUsername());
            checkStmt.setString(2, userModel.getEmail());
            ResultSet result = checkStmt.executeQuery();
            
            if (result.next()) {
                System.err.println("User already exists with the same username or email.");
                return false;
            }
            
            // Insert new user details
            insertStmt.setString(1, userModel.getUsername());
            insertStmt.setString(2, userModel.getDisplayName());
            insertStmt.setString(3, userModel.getEmail());
            insertStmt.setString(4, userModel.getPassword());
            insertStmt.setString(5, userModel.getImageUrl());
            
            // Handle favorite genre
            if (userModel.getFavoriteGenre() != null) {
                insertStmt.setInt(6, userModel.getFavoriteGenre());
            } else {
                insertStmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            return insertStmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Checks if a genre ID exists in the database.
     * 
     * @param genreId the ID of the genre to check
     * @return true if the genre exists, false otherwise
     */
    public boolean genreExists(int genreId) {
        if (dbConn == null) {
            return false;
        }
        
        String query = "SELECT GenreID FROM Genres WHERE GenreID = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, genreId);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            System.err.println("Error checking genre existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Validates the registration form data.
     * 
     * @param username the user's username
     * @param email the user's email
     * @param password the user's password
     * @param confirmPassword the user's password confirmation
     * @param image the user's profile image part (can be null)
     * @param favoriteGenre the user's favorite genre ID (can be null)
     * @return validation error message or null if validation passes
     */
    public String validateRegistrationForm(String username, String email, String password, 
                                          String confirmPassword, Part image, String favoriteGenre) {
        if (ValidationUtil.isNullOrEmpty(username)) return "Username is required.";
        if (ValidationUtil.isNullOrEmpty(email)) return "Email is required.";
        if (ValidationUtil.isNullOrEmpty(password)) return "Password is required.";
        if (ValidationUtil.isNullOrEmpty(confirmPassword)) return "Please confirm your password.";
        if (!ValidationUtil.isValidEmail(email)) return "Invalid email format.";
        if (!ValidationUtil.isValidPassword(password)) return "Password must be at least 8 characters long, with 1 uppercase letter, 1 number, and 1 symbol.";
        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) return "Passwords do not match.";

        // Only validate image if one was uploaded
        if (image != null && image.getSize() > 0 && !ValidationUtil.isValidImageExtension(image)) {
            return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
        }

        // Validate genre ID if provided
        if (favoriteGenre != null && !favoriteGenre.isEmpty()) {
            try {
                int genreId = Integer.parseInt(favoriteGenre);
                if (!genreExists(genreId)) {
                    return "Selected genre does not exist.";
                }
            } catch (NumberFormatException e) {
                return "Invalid genre selection.";
            }
        }

        return null; // No validation errors
    }
    
    /**
     * Creates a PodcastUserModel from the provided user data.
     * 
     * @param username the user's username
     * @param email the user's email
     * @param displayName the user's display name
     * @param favoriteGenreStr the user's favorite genre ID as a string
     * @param password the user's unencrypted password
     * @param image the user's profile image part
     * @return a populated PodcastUserModel object
     */
    public PodcastUserModel createUserModel(String username, String email, String displayName, 
                                          String favoriteGenreStr, String password, Part image) throws Exception {
        // Encrypt the password
        String encryptedPassword = PasswordUtil.encrypt(username, password);
        
        String imageUrl = null;
        if (image != null && image.getSize() > 0) {
            imageUrl = imageUtil.getImageNameFromPart(image);
        }

        // Convert favoriteGenre from String to Integer
        Integer favoriteGenre = null;
        if (favoriteGenreStr != null && !favoriteGenreStr.isEmpty()) {
            try {
                favoriteGenre = Integer.parseInt(favoriteGenreStr);
            } catch (NumberFormatException e) {
                // Invalid genre ID format, leave as null
            }
        }

        return new PodcastUserModel(username, encryptedPassword, email, displayName, favoriteGenre, imageUrl);
    }
    
    /**
     * Uploads the user's profile image to the server.
     * 
     * @param image the image part to upload
     * @param realPath the real path to the web application root
     * @return true if the upload was successful, false otherwise
     */
    public boolean uploadImage(Part image, String realPath) throws IOException, ServletException {
        if (image == null || image.getSize() == 0) {
            return true; // No image to upload is not an error
        }
        return imageUtil.uploadImage(image, realPath, "users");
    }
}