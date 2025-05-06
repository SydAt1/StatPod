package com.statpod.dao;

import com.statpod.config.DbConfig;
import com.statpod.model.PodcastModel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PodcastDao {

    public List<PodcastModel> getRecommendedPodcasts() throws SQLException, ClassNotFoundException {
        List<PodcastModel> podcasts = new ArrayList<>();
        String sql = "SELECT PodcastID, Podcast_Name, HostName, ReleaseDate, GenreID, PodImg, Description FROM podcasts ORDER BY ReleaseDate DESC LIMIT 5";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PodcastModel podcast = new PodcastModel(
                        rs.getInt("PodcastID"),
                        rs.getString("Podcast_Name"),
                        rs.getString("HostName"),
                        rs.getDate("ReleaseDate"),
                        rs.getInt("GenreID"),
                        rs.getString("PodImg"),
                        rs.getString("Description") // Added description
                );
                podcasts.add(podcast);
            }
        }
        return podcasts;
    }

    public List<PodcastModel> getAllPodcasts() throws SQLException, ClassNotFoundException {
        List<PodcastModel> podcasts = new ArrayList<>();
        String sql = "SELECT PodcastID, Podcast_Name, HostName, ReleaseDate, GenreID, PodImg, Description FROM podcasts ORDER BY PodcastID";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PodcastModel podcast = new PodcastModel(
                        rs.getInt("PodcastID"),
                        rs.getString("Podcast_Name"),
                        rs.getString("HostName"),
                        rs.getDate("ReleaseDate"),
                        rs.getInt("GenreID"),
                        rs.getString("PodImg"),
                        rs.getString("Description") // Added description
                );
                podcasts.add(podcast);
            }
        }
        return podcasts;
    }
    
    /**
     * Searches for podcasts by podcast name or host name
     * @param query The search term to look for in podcast names and host names
     * @return List of PodcastModel objects matching the search criteria
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public List<PodcastModel> searchPodcasts(String query) throws SQLException, ClassNotFoundException {
        List<PodcastModel> podcasts = new ArrayList<>();
        String sql = "SELECT PodcastID, Podcast_Name, HostName, ReleaseDate, GenreID, PodImg, Description " +
                     "FROM podcasts " +
                     "WHERE Podcast_Name LIKE ? OR HostName LIKE ? " +
                     "ORDER BY ReleaseDate DESC";
                     
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            // Add wildcards for partial matching
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PodcastModel podcast = new PodcastModel(
                            rs.getInt("PodcastID"),
                            rs.getString("Podcast_Name"),
                            rs.getString("HostName"),
                            rs.getDate("ReleaseDate"),
                            rs.getInt("GenreID"),
                            rs.getString("PodImg"),
                            rs.getString("Description")
                    );
                    podcasts.add(podcast);
                }
            }
        }
        return podcasts;
    }

    public String getGenreName(int genreId) throws SQLException, ClassNotFoundException {
        String genreName = null;
        String sql = "SELECT GenreName FROM genres WHERE GenreID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    genreName = rs.getString("GenreName");
                }
            }
        }
        return genreName;
    }


    public List<String[]> getAllGenres() throws SQLException, ClassNotFoundException {
        List<String[]> genres = new ArrayList<>();
        String sql = "SELECT GenreID, GenreName FROM genres ORDER BY GenreName";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] genre = new String[2];
                genre[0] = String.valueOf(rs.getInt("GenreID"));
                genre[1] = rs.getString("GenreName");
                genres.add(genre);
            }
        }
        return genres;
    }
    
    public void deletePodcastById(int podcastId) throws SQLException, ClassNotFoundException {
    	String sql = "DELETE FROM podcasts WHERE PodcastID = ?";
    	try (Connection conn = DbConfig.getDbConnection();
    		     PreparedStatement stmt = conn.prepareStatement(sql)) {
    		    stmt.setInt(1, podcastId);
    		    stmt.executeUpdate();
    	}
    }
    
    // Update all podcast attributes
    public void updatePodcast(PodcastModel podcast) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET Podcast_Name = ?, HostName = ?, ReleaseDate = ?, GenreID = ?, PodImg = ?, Description = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, podcast.getPodcastName());
            stmt.setString(2, podcast.getHostName());
            stmt.setDate(3, new java.sql.Date(podcast.getReleaseDate().getTime()));
            stmt.setInt(4, podcast.getGenreId());
            stmt.setString(5, podcast.getPodImg());
            stmt.setString(6, podcast.getDescription());
            stmt.setInt(7, podcast.getPodcastId());
            stmt.executeUpdate();
        }
    }
    
    // Individual attribute update methods
    public void updatePodcastName(int podcastId, String name) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET Podcast_Name = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, podcastId);
            stmt.executeUpdate();
        }
    }
    
    public void updatePodcastHostName(int podcastId, String hostName) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET HostName = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hostName);
            stmt.setInt(2, podcastId);
            stmt.executeUpdate();
        }
    }
    
    public void updatePodcastReleaseDate(int podcastId, Date releaseDate) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET ReleaseDate = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(releaseDate.getTime()));
            stmt.setInt(2, podcastId);
            stmt.executeUpdate();
        }
    }
    
    public void updatePodcastGenre(int podcastId, int genreId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET GenreID = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genreId);
            stmt.setInt(2, podcastId);
            stmt.executeUpdate();
        }
    }
    
    public void updatePodcastImage(int podcastId, String imageUrl) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET PodImg = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imageUrl);
            stmt.setInt(2, podcastId);
            stmt.executeUpdate();
        }
    }
    
    public void updatePodcastDescription(int podcastId, String description) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE podcasts SET Description = ? WHERE PodcastID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setInt(2, podcastId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Retrieves a single podcast from the database by its ID
     * @param podcastId The ID of the podcast to retrieve
     * @return PodcastModel containing the podcast data, or null if not found
     * @throws SQLException If a database access error occurs
     * @throws ClassNotFoundException If the database driver class is not found
     */
    public PodcastModel getPodcastById(int podcastId) throws SQLException, ClassNotFoundException {
        PodcastModel podcast = null;
        String sql = "SELECT PodcastID, Podcast_Name, HostName, ReleaseDate, GenreID, PodImg, Description FROM podcasts WHERE PodcastID = ?";
        
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, podcastId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    podcast = new PodcastModel(
                        rs.getInt("PodcastID"),
                        rs.getString("Podcast_Name"),
                        rs.getString("HostName"),
                        rs.getDate("ReleaseDate"),
                        rs.getInt("GenreID"),
                        rs.getString("PodImg"),
                        rs.getString("Description")
                    );
                }
            }
        }
        
        return podcast;
    }
}