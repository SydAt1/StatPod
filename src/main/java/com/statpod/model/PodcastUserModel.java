package com.statpod.model;

/**
 * PodcastUserModel represents a user of the podcast platform.
 * It contains user credentials, profile information, and an image URL.
 */
public class PodcastUserModel {
    private String username;
    private String password;
    private String email;
    private String displayName;
    private Integer favoriteGenre;  // Changed from String to Integer
    private String imageUrl;

    /**
     * Default constructor
     */
    public PodcastUserModel() {
    }

    /**
     * Constructor with basic authentication credentials
     *
     * @param username the user's username
     * @param password the user's password
     */
    public PodcastUserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Full constructor with all user details
     *
     * @param username the user's username
     * @param password the user's password
     * @param email the user's email address
     * @param displayName the user's display name
     * @param favoriteGenre the user's favorite podcast genre ID
     * @param imageUrl the user's profile image URL
     */
    public PodcastUserModel(String username, String password, String email, String displayName,
                          Integer favoriteGenre, String imageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
        this.favoriteGenre = favoriteGenre;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(Integer favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "PodcastUserModel [username=" + username + ", email=" + email + ", displayName=" + displayName
                + ", favoriteGenre=" + favoriteGenre + ", imageUrl=" + imageUrl + "]";
    }
}