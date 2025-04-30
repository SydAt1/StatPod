<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.model.PodcastModel" %>
<%@ page import="com.statpod.dao.PodcastDao" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Podcast Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/podcast.css">
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

                <div class="action-buttons mt-4">
                    <button class="btn btn-primary play-button">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                             class="bi bi-play-fill" viewBox="0 0 16 16">
                            <path d="M11.596 8.697l-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01
                                     1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"/>
                        </svg>
                        Play
                    </button>
                    <button class="btn btn-outline-light try-button">Try Free</button>
                </div>
            </div>
        </div>
    <% } %>
</main>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
