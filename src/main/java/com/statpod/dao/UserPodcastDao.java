package com.statpod.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class UserPodcastDao {
    private Connection connection;

    public UserPodcastDao(Connection connection) {
        this.connection = connection;
    }

    public void addUserPodcast(String username, int podcastId) throws SQLException {
        String query = "INSERT INTO UserPodcasts (Username, PodcastID) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, podcastId);
            pstmt.executeUpdate();
        }
    }
}
