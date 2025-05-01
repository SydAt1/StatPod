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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/podcast.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/recommendations.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <main class="container py-5">
        <%
            PodcastModel podcast = (PodcastModel) request.getAttribute("podcast");
            PodcastDao podcastDao = new PodcastDao();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        %>

        <% if (podcast == null) { %>
            <div class="alert alert-danger" role="alert">
                Podcast not found.
            </div>
        <% } else { %>
            <div class="podcast-hero d-flex flex-column flex-md-row gap-4 align-items-center">
                <div class="text-center text-md-start">
                    <img src="${pageContext.request.contextPath}/images/podcasts/<%= podcast.getPodImg() %>"
                         alt="<%= podcast.getPodcastName() %>"
                         class="podcast-cover">
                </div>
                <div class="podcast-details">
                    <h1 class="podcast-title"><%= podcast.getPodcastName() %></h1>
                    <p class="podcast-host">Hosted by <%= podcast.getHostName() %></p>
                    <p class="podcast-meta">
                        <span class="badge genre-badge"><%= podcastDao.getGenreName(podcast.getGenreId()) %></span>
                        <span class="podcast-date">Released: <%= dateFormat.format(podcast.getReleaseDate()) %></span>
                    </p>
                    <p class="podcast-description"><%= podcast.getDescription() %></p>

                    <div class="action-buttons mt-4 d-flex gap-2">
				    <form method="post" action="${pageContext.request.contextPath}/podcast">
				        <input type="hidden" name="action" value="play">
				        <input type="hidden" name="podcastId" value="<%= podcast.getPodcastId() %>">
				        <button type="submit" class="btn btn-primary play-button">
				            <img src="${pageContext.request.contextPath}/images/podcast_page/play-button.svg" alt="Play Button" width="18" height="18">
				            Play
				        </button>
				    </form>

				    <form method="post" action="${pageContext.request.contextPath}/podcast">
					    <input type="hidden" name="action" value="toggleLike">
					    <input type="hidden" name="podcastId" value="<%= podcast.getPodcastId() %>">
					    <button type="submit" class="btn btn-icon favorite-button" title="${isLiked ? 'Remove from Favorites' : 'Add to Favorites'}">
					        <img src="${pageContext.request.contextPath}/images/podcast_page/${isLiked ? 'filled-heart.svg' : 'empty-heart.svg'}"
					             alt="${isLiked ? 'Liked' : 'Not Liked'}" width="24" height="24">
					    </button>
					</form>
				</div>
                </div>
            </div>

            <div class="mt-5">
                <section class="recommendations">
                    <h2 class="section-heading"><span class="icon">✨</span> Recommended For You</h2>
                    <div class="recommendation-grid">
                        <%
                            List<PodcastModel> recommendedPods = podcastDao.getRecommendedPodcasts();
                            for (PodcastModel pod : recommendedPods) {
                                // Skip the current podcast in recommendations
                                if (podcast.getPodcastId() == pod.getPodcastId()) {
                                    continue;
                                }

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
                            // Add empty cards to fill the grid if there are fewer than 5 recommendations
                            int numRecommendations = recommendedPods.size();
                            for (int i = numRecommendations; i < 5; i++) {
                        %>
                            <div class="recommendation-card empty-card"></div>
                        <% } %>
                    </div>
                </section>
            </div>
        <% } %>
    </main>

    <jsp:include page="footer.jsp" />

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>