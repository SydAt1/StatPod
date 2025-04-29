package com.statpod.model;

/**
 * GenreModel represents a genre in the platform.
 * It contains genre details including name and description.
 */
public class GenreModel {
    private Integer genreId;
    private String genreName;
    private String description;

    /**
     * Default constructor
     */
    public GenreModel() {
    }

    /**
     * Constructor with genre ID
     *
     * @param genreId the unique identifier for the genre
     */
    public GenreModel(Integer genreId) {
        this.genreId = genreId;
    }

    /**
     * Full constructor with all genre details
     *
     * @param genreId the unique identifier for the genre
     * @param genreName the name of the genre
     * @param description the description of the genre
     */
    public GenreModel(Integer genreId, String genreName, String description) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.description = description;
    }

    /**
     * Constructor without ID for creating new genres
     *
     * @param genreName the name of the genre
     * @param description the description of the genre
     */
    public GenreModel(String genreName, String description) {
        this.genreName = genreName;
        this.description = description;
    }

    // Getters and Setters

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GenreModel [genreId=" + genreId + ", genreName=" + genreName + ", description=" + description + "]";
    }
}