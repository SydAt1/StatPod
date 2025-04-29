package com.statpod.model;

import java.io.Serializable;

/**
 * Represents the association between a User and a Podcast they've subscribed to or listened to.
 */
public class UserPodcastsModel implements Serializable {
    private String username;
    private int podcastId;

    public UserPodcastsModel() {
    }

    public UserPodcastsModel(String username, int podcastId) {
        this.username = username;
        this.podcastId = podcastId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(int podcastId) {
        this.podcastId = podcastId;
    }

    @Override
    public String toString() {
        return "UserPodcastModel{" +
                "username='" + username + '\'' +
                ", podcastId=" + podcastId +
                '}';
    }
}
