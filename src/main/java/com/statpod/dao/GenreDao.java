package com.statpod.dao;

import com.statpod.config.DbConfig;
import com.statpod.model.GenreModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDao {
    private Connection connection;
    
    /**
     * Default constructor initializes connection with DB
     */
    public GenreDao() {
        try {
            this.connection = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Constructor for dependency injection
     * @param connection the database connection
     */
    public GenreDao(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Retrieves all genres from the database
     * @return List of GenreModel objects
     */
    public List<GenreModel> getAllGenres() {
        try {
            return getAllGenresImpl();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Implementation method that can throw SQLException
    private List<GenreModel> getAllGenresImpl() throws SQLException {
        List<GenreModel> genres = new ArrayList<>();
        String query = "SELECT * FROM Genres";
        try (Statement stmt = connection.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                genres.add(new GenreModel(rs.getInt("GenreID"), 
                                         rs.getString("GenreName"), 
                                         rs.getString("Description")));
            }
        }
        return genres;
    }
    
    /**
     * Retrieves a genre by its ID
     * @param genreId the ID of the genre
     * @return GenreModel object if found, null otherwise
     */
    public GenreModel getGenreById(int genreId) {
        try {
            return getGenreByIdImpl(genreId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Implementation method that can throw SQLException
    private GenreModel getGenreByIdImpl(int genreId) throws SQLException {
        String query = "SELECT * FROM Genres WHERE GenreID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, genreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new GenreModel(rs.getInt("GenreID"), 
                                         rs.getString("GenreName"), 
                                         rs.getString("Description"));
                }
            }
        }
        return null;
    }
    
    /**
     * Retrieves a genre ID by its name
     * @param genreName the name of the genre
     * @return genre ID if found, -1 otherwise
     */
    public int fetchGenreIdByName(String genreName) {
        try {
            return fetchGenreIdByNameImpl(genreName);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    // Implementation method that can throw SQLException
    private int fetchGenreIdByNameImpl(String genreName) throws SQLException {
        String query = "SELECT GenreID FROM Genres WHERE GenreName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, genreName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("GenreID");
            }
        }
        return -1; // Not found
    }
    
    /**
     * Checks if a genre exists by ID
     * @param genreId the ID to check
     * @return true if exists, false otherwise
     */
    public boolean genreExists(int genreId) {
        return getGenreById(genreId) != null;
    }
}