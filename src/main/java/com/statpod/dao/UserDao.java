package com.statpod.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    public List<PodcastUserModel> getAllUsers() throws SQLException {
        List<PodcastUserModel> userList = new ArrayList<>();
        String query = "SELECT Username, DisplayName, Email_ID, ImageUrl, FavoriteGenre FROM Users";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PodcastUserModel user = new PodcastUserModel();
                user.setUsername(rs.getString("Username"));
                user.setDisplayName(rs.getString("DisplayName"));
                user.setEmail(rs.getString("Email_ID"));
                user.setImageUrl(rs.getString("ImageUrl"));
                user.setFavoriteGenre(rs.getInt("FavoriteGenre"));
                userList.add(user);
            }
        }
        return userList;
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
    
    public void deleteUser(String username) throws SQLException {
        String query = "DELETE FROM Users WHERE Username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }
    
    public void editUserProfile(String username, String displayName, String email, int favoriteGenreId, String imageUrl, String password) throws SQLException {
        String sql = "UPDATE Users SET DisplayName = ?, Email_ID = ?, FavoriteGenre = ?, ImageUrl = ?, Password = ? WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, displayName);
            stmt.setString(2, email);
            stmt.setInt(3, favoriteGenreId);
            stmt.setString(4, imageUrl);
            stmt.setString(5, password);
            stmt.setString(6, username);
            stmt.executeUpdate();
        }
    }
}