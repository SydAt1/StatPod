package com.statpod.model;

import java.util.Date;

/**
 * PodcastModel represents a podcast in the platform.
 * It contains podcast details including name, host, release date, and genre.
 */
public class PodcastModel {
    private Integer podcastId;
    private String podcastName;
    private String hostName;
    private Date releaseDate;
    private Integer genreId;
    private String podImg;
    private String description; // Added description field

    /**
     * Default constructor
     */
    public PodcastModel() {
    }

    /**
     * Constructor with podcast ID
     *
     * @param podcastId the unique identifier for the podcast
     */
    public PodcastModel(Integer podcastId) {
        this.podcastId = podcastId;
    }

    /**
     * Full constructor with all podcast details
     *
     * @param podcastId the unique identifier for the podcast
     * @param podcastName the name of the podcast
     * @param hostName the name of the podcast host
     * @param releaseDate the release date of the podcast
     * @param genreId the genre ID associated with the podcast
     * @param podImg the image file name for the podcast
     * @param description the description of the podcast.
     */
    public PodcastModel(Integer podcastId, String podcastName, String hostName, 
                        Date releaseDate, Integer genreId, String podImg, String description) {
        this.podcastId = podcastId;
        this.podcastName = podcastName;
        this.hostName = hostName;
        this.releaseDate = releaseDate;
        this.genreId = genreId;
        this.podImg = podImg;
        this.description = description;
    }

    /**
     * Constructor without ID for creating new podcasts
     *
     * @param podcastName the name of the podcast
     * @param hostName the name of the podcast host
     * @param releaseDate the release date of the podcast
     * @param genreId the genre ID associated with the podcast
     * @param podImg the image file name for the podcast
     * @param description the description of the podcast.
     */
    public PodcastModel(String podcastName, String hostName, 
                        Date releaseDate, Integer genreId, String podImg, String description) {
        this.podcastName = podcastName;
        this.hostName = hostName;
        this.releaseDate = releaseDate;
        this.genreId = genreId;
        this.podImg = podImg;
        this.description = description;
    }

    // Getters and Setters

    public Integer getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Integer podcastId) {
        this.podcastId = podcastId;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getPodImg() {
        return podImg;
    }

    public void setPodImg(String podImg) {
        this.podImg = podImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PodcastModel [podcastId=" + podcastId + 
               ", podcastName=" + podcastName + 
               ", hostName=" + hostName + 
               ", releaseDate=" + releaseDate + 
               ", genreId=" + genreId +
                ", podImg=" + podImg +
                ", description=" + description + "]";
    }
}