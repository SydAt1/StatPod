package com.statpod.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.statpod.config.DbConfig;
import com.statpod.model.PodcastUserModel;

/**
 * RegisterService handles the registration of new podcast users.
 * It manages database interactions for user registration.
 */
public class RegisterService {
    private Connection dbConn;

    /**
     * Constructor initializes the database connection.
     */
    public RegisterService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
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
}