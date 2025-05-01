package com.statpod.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.statpod.model.UserPodcastsModel;

public class UserPodcastDao {
    private Connection connection;
    
    public UserPodcastDao(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Adds a new user-podcast relationship with default values
     */
    public void addUserPodcast(String username, int podcastId) throws SQLException {
        String query = "INSERT INTO UserPodcasts (Username, PodcastID, Play, Liked) VALUES (?, ?, 0, false)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, podcastId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Increments the play counter for a user's podcast
     */
    public void incrementPlayCount(String username, int podcastId) throws SQLException {
        String query = "UPDATE UserPodcasts SET Play = Play + 1 WHERE Username = ? AND PodcastID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, podcastId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Updates the liked status for a user's podcast
     */
    public void updateLikedStatus(String username, int podcastId, boolean liked) throws SQLException {
        String query = "UPDATE UserPodcasts SET Liked = ? WHERE Username = ? AND PodcastID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBoolean(1, liked);
            pstmt.setString(2, username);
            pstmt.setInt(3, podcastId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Toggles the liked status for a user's podcast
     */
    public boolean toggleLikedStatus(String username, int podcastId) throws SQLException {
        // First get current status
        UserPodcastsModel userPodcast = getUserPodcast(username, podcastId);
        boolean newStatus = !userPodcast.isLiked();
        
        // Then update
        updateLikedStatus(username, podcastId, newStatus);
        return newStatus;
    }
    
    /**
     * Retrieves a user-podcast relationship
     */
    public UserPodcastsModel getUserPodcast(String username, int podcastId) throws SQLException {
        String query = "SELECT Username, PodcastID, Play, Liked FROM UserPodcasts WHERE Username = ? AND PodcastID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, podcastId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UserPodcastsModel(
                        rs.getString("Username"),
                        rs.getInt("PodcastID"),
                        rs.getInt("Play"),
                        rs.getBoolean("Liked")
                    );
                }
                return null;
            }
        }
    }
    
    /**
     * Gets all podcasts for a specific user
     */
    public List<UserPodcastsModel> getUserPodcasts(String username) throws SQLException {
        List<UserPodcastsModel> userPodcasts = new ArrayList<>();
        String query = "SELECT Username, PodcastID, Play, Liked FROM UserPodcasts WHERE Username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    userPodcasts.add(new UserPodcastsModel(
                        rs.getString("Username"),
                        rs.getInt("PodcastID"),
                        rs.getInt("Play"),
                        rs.getBoolean("Liked")
                    ));
                }
            }
        }
        return userPodcasts;
    }
    
    /**
     * Gets all liked podcasts for a specific user
     */
    public List<UserPodcastsModel> getLikedPodcasts(String username) throws SQLException {
        List<UserPodcastsModel> likedPodcasts = new ArrayList<>();
        String query = "SELECT Username, PodcastID, Play, Liked FROM UserPodcasts WHERE Username = ? AND Liked = true";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    likedPodcasts.add(new UserPodcastsModel(
                        rs.getString("Username"),
                        rs.getInt("PodcastID"),
                        rs.getInt("Play"),
                        rs.getBoolean("Liked")
                    ));
                }
            }
        }
        return likedPodcasts;
    }
    
    /**
     * Deletes a user-podcast relationship
     */
    public void deleteUserPodcast(String username, int podcastId) throws SQLException {
        String query = "DELETE FROM UserPodcasts WHERE Username = ? AND PodcastID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, podcastId);
            pstmt.executeUpdate();
        }
    }
}