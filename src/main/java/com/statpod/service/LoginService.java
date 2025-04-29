package com.statpod.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.statpod.config.DbConfig;
import com.statpod.model.PodcastUserModel;
import com.statpod.util.PasswordUtil;

/**
 * Service class for handling login operations. Connects to the database,
 * verifies user credentials, and returns login status.
 */
public class LoginService {

    /**
     * Validates the user credentials against the database records.
     *
     * @param podcastUserModel the PodcastUserModel object containing user credentials
     * @return true if the user credentials are valid, false otherwise; null if a
     *         connection error occurs
     */
    public Boolean loginUser(PodcastUserModel podcastUserModel) {
        String query = "SELECT username, password FROM users WHERE username = ?";

        try (Connection dbConn = DbConfig.getDbConnection();
             PreparedStatement stmt = dbConn.prepareStatement(query)) {
            
            stmt.setString(1, podcastUserModel.getUsername());
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return validatePassword(result, podcastUserModel);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return false;
    }

    /**
     * Validates the password retrieved from the database.
     *
     * @param result           the ResultSet containing the username and password from
     *                         the database
     * @param podcastUserModel the PodcastUserModel object containing user credentials
     * @return true if the passwords match, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean validatePassword(ResultSet result, PodcastUserModel podcastUserModel) throws SQLException {
        String dbPassword = result.getString("password");
        String decryptedPassword = PasswordUtil.decrypt(dbPassword, podcastUserModel.getUsername());

        return decryptedPassword != null && decryptedPassword.equals(podcastUserModel.getPassword());
    }
}
