package com.statpod.dao;

import com.statpod.config.DbConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardDao {

    /**
     * Gets total count of users in the application
     * @return Total number of users
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public int getTotalUserCount() throws SQLException, ClassNotFoundException {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM Users";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        }
        
        return count;
    }
    
    /**
     * Gets total count of podcasts in the application
     * @return Total number of podcasts
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public int getTotalPodcastCount() throws SQLException, ClassNotFoundException {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM podcasts";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        }
        
        return count;
    }
    
    /**
     * Gets count of podcasts by genre
     * @return Map with genre name as key and count as value
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public Map<String, Integer> getPodcastCountByGenre() throws SQLException, ClassNotFoundException {
        Map<String, Integer> genreCounts = new HashMap<>();
        String sql = "SELECT g.GenreName, COUNT(p.PodcastID) AS podcast_count " +
                     "FROM genres g " +
                     "LEFT JOIN podcasts p ON g.GenreID = p.GenreID " +
                     "GROUP BY g.GenreID, g.GenreName " +
                     "ORDER BY podcast_count DESC";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                genreCounts.put(rs.getString("GenreName"), rs.getInt("podcast_count"));
            }
        }
        
        return genreCounts;
    }
    
    /**
     * Gets total number of users with a favorite genre
     * @return Map with genre name as key and count as value
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public Map<String, Integer> getUserCountByFavoriteGenre() throws SQLException, ClassNotFoundException {
        Map<String, Integer> genreCounts = new HashMap<>();
        String sql = "SELECT g.GenreName, COUNT(u.Username) AS user_count " +
                     "FROM genres g " +
                     "LEFT JOIN Users u ON g.GenreID = u.FavoriteGenre " +
                     "GROUP BY g.GenreID, g.GenreName " +
                     "ORDER BY user_count DESC";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                genreCounts.put(rs.getString("GenreName"), rs.getInt("user_count"));
            }
        }
        
        return genreCounts;
    }
    
    /**
     * Gets the recently added podcasts
     * @param limit Number of recent podcasts to fetch
     * @return Map with podcast name as key and release date as value
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public Map<String, String> getRecentlyAddedPodcasts(int limit) throws SQLException, ClassNotFoundException {
        Map<String, String> recentPodcasts = new HashMap<>();
        String sql = "SELECT Podcast_Name, ReleaseDate FROM podcasts ORDER BY ReleaseDate DESC LIMIT ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recentPodcasts.put(
                        rs.getString("Podcast_Name"), 
                        rs.getDate("ReleaseDate").toString()
                    );
                }
            }
        }
        
        return recentPodcasts;
    }
}