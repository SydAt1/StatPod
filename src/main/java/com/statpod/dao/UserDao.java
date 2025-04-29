package com.statpod.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.statpod.model.PodcastUserModel;

public class UserDao {
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void addUser(PodcastUserModel user) throws SQLException {
        String query = "INSERT INTO Users (Username, DisplayName, Email_ID, Password, ImageUrl, FavoriteGenre) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getDisplayName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getImageUrl());
            pstmt.setInt(6, user.getFavoriteGenre());
            pstmt.executeUpdate();
        }
    }
    
 // NEW: Update user profile (display name, email, favorite genre)
    public void updateUserProfile(String username, String displayName, String email, int favoriteGenreId, String imageUrl) throws SQLException {
        String sql = "UPDATE Users SET DisplayName = ?, Email_ID = ?, FavoriteGenre = ?, ImageUrl = ? WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, displayName);
            stmt.setString(2, email);
            stmt.setInt(3, favoriteGenreId);
            stmt.setString(4, imageUrl);
            stmt.setString(5, username);
            stmt.executeUpdate();
        }
    }
    
    public PodcastUserModel fetchUserByUsername(String username) throws SQLException {
        String query = "SELECT Username, DisplayName, Email_ID, Password, ImageUrl, FavoriteGenre FROM Users WHERE Username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PodcastUserModel(
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("Email_ID"),
                    rs.getString("DisplayName"),
                    rs.getInt("FavoriteGenre"),
                    rs.getString("ImageUrl")
                );
            }
        }
        return null; // No user found
    }
    
    public void updateUserProfileWithImage(String username, String displayName, String email, int favoriteGenreId, String imageUrl) throws SQLException {
        String sql = "UPDATE Users SET DisplayName = ?, email = ?, FavoriteGenre = ?, ImageUrl = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, displayName);
            stmt.setString(2, email);
            stmt.setInt(3, favoriteGenreId);
            stmt.setString(4, imageUrl);
            stmt.setString(5, username);
            stmt.executeUpdate();
        }
    }

}