<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.statpod.model.PodcastModel" %>
<%@ page import="com.statpod.dao.PodcastDao" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Results - StatPod</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/search-results.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

    <%@ include file="header.jsp" %>

    <div class="content-container">
        <div class="search-header">
            <% String searchQuery = (String) request.getAttribute("searchQuery"); %>
            <h1>Search Results <%= searchQuery != null && !searchQuery.isEmpty() ? "for \"" + searchQuery + "\"" : "" %></h1>
            <p><%= request.getAttribute("resultCount") %> results found</p>
        </div>

        <div class="results-grid">
            <%
                List<PodcastModel> searchResults = (List<PodcastModel>) request.getAttribute("searchResults");
                PodcastDao podcastDao = new PodcastDao();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

                if (searchResults != null && !searchResults.isEmpty()) {
                    for (PodcastModel podcast : searchResults) {
                        String genreName = podcastDao.getGenreName(podcast.getGenreId());
            %>
                <div class="podcast-card">
                    <a href="${pageContext.request.contextPath}/podcast?id=<%= podcast.getPodcastId() %>">
                        <div class="podcast-image" style="background-image: url('${pageContext.request.contextPath}/images/podcasts/<%= podcast.getPodImg() != null 
                            ? podcast.getPodImg() 
                            : "default-podcast.jpg" 
                        %>');"></div>
                        <div class="podcast-info">
                            <h3><%= podcast.getPodcastName() %></h3>
                            <p>By <%= podcast.getHostName() %></p>
                        </div>
                    </a>
                </div>
            <%
                    }
                } else {
            %>
                <div class="no-results">
                    <i class="fas fa-search"></i>
                    <h2>No podcasts found</h2>
                    <p>Try different keywords or check out our podcast collection</p>
                </div>
            <%
                }
            %>
        </div>
    </div>

    <%@ include file="footer.jsp" %>

</body>
</html>