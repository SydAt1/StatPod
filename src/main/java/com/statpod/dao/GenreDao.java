package com.statpod.dao;

import com.statpod.model.GenreModel;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDao {
    private Connection connection;

    public GenreDao(Connection connection) {
        this.connection = connection;
    }

    public List<GenreModel> getAllGenres() throws SQLException {
        List<GenreModel> genres = new ArrayList<>();
        String query = "SELECT * FROM Genres";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                genres.add(new GenreModel(rs.getInt("GenreID"), rs.getString("GenreName"), rs.getString("Description")));
            }
        }
        return genres;
    }

    public GenreModel getGenreById(int genreId) throws SQLException {
        String query = "SELECT * FROM Genres WHERE GenreID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, genreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new GenreModel(rs.getInt("GenreID"), rs.getString("GenreName"), rs.getString("Description"));
                }
            }
        }
        return null;
    }
    

    public int fetchGenreIdByName(String genreName) throws SQLException {
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
}





