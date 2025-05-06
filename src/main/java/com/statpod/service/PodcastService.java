//package com.statpod.service;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//
//import com.statpod.config.DbConfig;
//import com.statpod.model.PodcastModel;
//
///**
// * PodcastService handles the addition of new podcasts.
// * It manages database interactions for podcast creation.
// */
//public class PodcastService {
//    private Connection dbConn;
//
//    /**
//     * Constructor initializes the database connection.
//     */
//    public PodcastService() {
//        try {
//            this.dbConn = DbConfig.getDbConnection();
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.err.println("Database connection error: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }
//
//    /**
//     * Adds a new podcast to the database.
//     *
//     * @param podcastModel the podcast details to be added
//     * @return Boolean indicating the success of the operation
//     */
//    public Boolean addPodcast(PodcastModel podcastModel) {
//        if (dbConn == null) {
//            System.err.println("Database connection is not available.");
//            return null;
//        }
//
//        String insertQuery = "INSERT INTO Podcasts (Podcast_Name, HostName, ReleaseDate, GenreID, Description, PodImg) "
//                + "VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
//
//            insertStmt.setString(1, podcastModel.getPodcastName());
//            insertStmt.setString(2, podcastModel.getHostName());
//
//            // Directly convert java.util.Date to java.sql.Timestamp
//            insertStmt.setTimestamp(3, new Timestamp(podcastModel.getReleaseDate().getTime()));
//
//            // Handle genre ID
//            if (podcastModel.getGenreId() != null) {
//                insertStmt.setInt(4, podcastModel.getGenreId());
//            } else {
//                insertStmt.setNull(4, java.sql.Types.INTEGER);
//            }
//
//            insertStmt.setString(5, podcastModel.getDescription());
//            insertStmt.setString(6, podcastModel.getPodImg());
//
//            return insertStmt.executeUpdate() > 0;
//
//        } catch (Exception e) {
//            System.err.println("Error during podcast addition: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * Checks if a genre ID exists in the database.
//     *
//     * @param genreId the ID of the genre to check
//     * @return true if the genre exists, false otherwise
//     */
//    public boolean genreExists(int genreId) {
//        if (dbConn == null) {
//            return false;
//        }
//
//        String query = "SELECT GenreID FROM Genres WHERE GenreID = ?";
//
//        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
//            stmt.setInt(1, genreId);
//            ResultSet result = stmt.executeQuery();
//            return result.next();
//        } catch (SQLException e) {
//            System.err.println("Error checking genre existence: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    /**
//     * Updates an existing podcast in the database.
//     *
//     * @param podcastModel the podcast details to be updated
//     * @return Boolean indicating the success of the update operation
//     */
//    public Boolean updatePodcast(PodcastModel podcastModel) {
//        if (dbConn == null) {
//            System.err.println("Database connection is not available.");
//            return null;
//        }
//
//        String updateQuery = "UPDATE Podcasts SET Podcast_Name = ?, HostName = ?, ReleaseDate = ?, "
//                + "GenreID = ?, Description = ?, PodImg = ? WHERE PodcastID = ?";
//
//        try (PreparedStatement updateStmt = dbConn.prepareStatement(updateQuery)) {
//            updateStmt.setString(1, podcastModel.getPodcastName());
//            updateStmt.setString(2, podcastModel.getHostName());
//            updateStmt.setTimestamp(3, new Timestamp(podcastModel.getReleaseDate().getTime()));
//
//            if (podcastModel.getGenreId() != null) {
//                updateStmt.setInt(4, podcastModel.getGenreId());
//            } else {
//                updateStmt.setNull(4, java.sql.Types.INTEGER);
//            }
//
//            updateStmt.setString(5, podcastModel.getDescription());
//            updateStmt.setString(6, podcastModel.getPodImg());
//            updateStmt.setInt(7, podcastModel.getPodcastId());
//
//            return updateStmt.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            System.err.println("Error during podcast update: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//}