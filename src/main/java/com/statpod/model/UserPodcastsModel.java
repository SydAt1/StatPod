package com.statpod.model;

import java.io.Serializable;

/**
 * Represents the association between a User and a Podcast they've subscribed to or listened to.
 */
public class UserPodcastsModel implements Serializable {
    private String username;
    private int podcastId;
    private int play;
    private boolean liked;

    public UserPodcastsModel() {
    }

    public UserPodcastsModel(String username, int podcastId, int play, boolean liked) {
        this.username = username;
        this.podcastId = podcastId;
        this.play = play;
        this.liked = liked;
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

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "UserPodcastsModel{" +
                "username='" + username + '\'' +
                ", podcastId=" + podcastId +
                ", play=" + play +
                ", liked=" + liked +
                '}';
    }
}
