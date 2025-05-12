<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.model.PodcastModel" %>
<%@ page import="com.statpod.dao.PodcastDao" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Podcast Details</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/podcast.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <main class="main-wrapper">
        <%
            PodcastModel podcast = (PodcastModel) request.getAttribute("podcast");
            PodcastDao podcastDao = new PodcastDao();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        %>

        <% if (podcast == null) { %>
            <div class="alert-danger">
                Podcast not found.
            </div>
        <% } else { %>
            <div class="podcast-hero">
                <div class="podcast-image-wrapper">
                    <img src="${pageContext.request.contextPath}/images/podcasts/<%= podcast.getPodImg() %>"
                         alt="<%= podcast.getPodcastName() %>"
                         class="podcast-cover">
                </div>
                <div class="podcast-details">
                    <h1 class="podcast-title"><%= podcast.getPodcastName() %></h1>
                    <p class="podcast-host">Hosted by <%= podcast.getHostName() %></p>
                    <p class="podcast-meta">
                        <span class="genre-badge"><%= podcastDao.getGenreName(podcast.getGenreId()) %></span>
                        <span class="podcast-date">Released: <%= dateFormat.format(podcast.getReleaseDate()) %></span>
                    </p>
                    <p class="podcast-description"><%= podcast.getDescription() %></p>

                    <div class="action-buttons">
                        <form method="post" action="${pageContext.request.contextPath}/podcast">
                            <input type="hidden" name="action" value="play">
                            <input type="hidden" name="podcastId" value="<%= podcast.getPodcastId() %>">
                            <button type="submit" class="play-button">
                                <img src="${pageContext.request.contextPath}/images/podcast_page/play-button.svg" alt="Play" width="18" height="18">
                                Play
                            </button>
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/podcast">
                            <input type="hidden" name="action" value="toggleLike">
                            <input type="hidden" name="podcastId" value="<%= podcast.getPodcastId() %>">
                            <button type="submit" class="favorite-button" title="${isLiked ? 'Remove from Favorites' : 'Add to Favorites'}">
                                <img src="${pageContext.request.contextPath}/images/podcast_page/${isLiked ? 'filled-heart.svg' : 'empty-heart.svg'}"
                                     alt="${isLiked ? 'Liked' : 'Not Liked'}" width="24" height="24">
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="recommendations">
                <h2 class="section-heading"><span class="icon">✨</span> Recommended For You</h2>
                <div class="recommendation-grid">
                    <%
                        List<PodcastModel> recommendedPods = podcastDao.getRecommendedPodcasts();
                        for (PodcastModel pod : recommendedPods) {
                            if (podcast.getPodcastId() == pod.getPodcastId()) continue;

                            String genreName = podcastDao.getGenreName(pod.getGenreId());
                            String reason;
                            if (pod.getReleaseDate().after(java.sql.Date.valueOf("2024-01-01"))) {
                                reason = "New episode available";
                            } else if (pod.getGenreId() == 1) {
                                reason = "Popular in your area";
                            } else if (pod.getGenreId() == 4) {
                                reason = "Based on your interests";
                            } else {
                                reason = "You might like this";
                            }
                    %>
                    <a href="${pageContext.request.contextPath}/podcast?id=<%= pod.getPodcastId() %>" class="recommendation-card">
                        <div class="recommendation-image"
                             style="background-image: url('${pageContext.request.contextPath}/images/podcasts/<%= pod.getPodImg() %>');">
                        </div>
                        <div class="recommendation-info">
                            <h3><%= pod.getPodcastName() %></h3>
                            <p><%= pod.getHostName() %></p>
                            <div class="why-recommended"><%= reason %></div>
                        </div>
                    </a>
                    <% } %>

                    <%
                        int numRecommendations = recommendedPods.size();
                        for (int i = numRecommendations; i < 5; i++) {
                    %>
                        <div class="recommendation-card empty-card"></div>
                    <% } %>
                </div>
            </div>
        <% } %>
    </main>

    <jsp:include page="footer.jsp" />
</body>
</html>
