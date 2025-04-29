<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.dao.PodcastDao" %>
<%@ page import="com.statpod.model.PodcastModel" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Podcast</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/new_podcast.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

<%@ include file="/WEB-INF/pages/header.jsp" %>

<%
    // Get podcast ID from the request parameter
    String podcastIdStr = request.getParameter("id");
    int podcastId = 0;
    PodcastModel podcast = null;
    
    if (podcastIdStr != null && !podcastIdStr.isEmpty()) {
        try {
            podcastId = Integer.parseInt(podcastIdStr);
            
            // Get podcast data from the database
            PodcastDao podcastDao = new PodcastDao();
            podcast = podcastDao.getPodcastById(podcastId);
            
        } catch (NumberFormatException e) {
            // Handle invalid podcast ID
            request.setAttribute("error", "Invalid podcast ID");
        }
    } else {
        // No podcast ID provided
        request.setAttribute("error", "No podcast ID provided");
    }
%>

<div class="page-wrapper">
    <main class="main-content">
        <div class="podcast-box">
            <img src="${pageContext.request.contextPath}/images/login/loginLogo.png" alt="Podcast Logo" class="logo">
            
            <h2>Edit Podcast</h2>
            <p class="subtitle">Update your podcast information</p>

            <%-- Display success message if present --%>
            <% if(request.getAttribute("success") != null && !((String)request.getAttribute("success")).isEmpty()) { %>
            <div class="success-message">${success}</div>
            <% } %>

            <%-- Display error message if present --%>
            <% if(request.getAttribute("error") != null && !((String)request.getAttribute("error")).isEmpty()) { %>
            <div class="error-message">${error}</div>
            <% } %>

            <% if(podcast != null) { %>
            <form action="${pageContext.request.contextPath}/admin/updatePodcast" method="post" enctype="multipart/form-data">
                <input type="hidden" name="podcastId" value="<%= podcast.getPodcastId() %>">
                
                <div class="form-group">
                    <input type="text" name="podcastName" placeholder="Podcast Name" 
                        value="<%= podcast.getPodcastName() %>" required>
                </div>
                
                <div class="form-group">
                    <input type="text" name="hostName" placeholder="Host Name" 
                        value="<%= podcast.getHostName() %>" required>
                </div>
                
                <div class="form-group">
                    <input type="text" name="date" value="<%= podcast.getReleaseDate() %>" readonly>
                </div>
                
                <div class="form-group">
                    <select name="genre">
                        <option value="">Select Genre</option>
                        <option value="1" <%= podcast.getGenreId() == 1 ? "selected" : "" %>>Comedy</option>
                        <option value="2" <%= podcast.getGenreId() == 2 ? "selected" : "" %>>Technology</option>
                        <option value="3" <%= podcast.getGenreId() == 3 ? "selected" : "" %>>Music</option>
                        <option value="4" <%= podcast.getGenreId() == 4 ? "selected" : "" %>>Business</option>
                        <option value="5" <%= podcast.getGenreId() == 5 ? "selected" : "" %>>Education</option>
                        <option value="6" <%= podcast.getGenreId() == 6 ? "selected" : "" %>>Horror</option>
                        <option value="7" <%= podcast.getGenreId() == 7 ? "selected" : "" %>>True Crime</option>
                        <option value="8" <%= podcast.getGenreId() == 8 ? "selected" : "" %>>History</option>
                        <option value="9" <%= podcast.getGenreId() == 9 ? "selected" : "" %>>Science</option>
                        <option value="10" <%= podcast.getGenreId() == 10 ? "selected" : "" %>>Health And Fitness</option>
                        <option value="11" <%= podcast.getGenreId() == 11 ? "selected" : "" %>>Fiction</option>
                        <option value="12" <%= podcast.getGenreId() == 12 ? "selected" : "" %>>Lifestyle</option>
                        <option value="13" <%= podcast.getGenreId() == 13 ? "selected" : "" %>>Interview</option>
                        <%
                            Object genreList = request.getAttribute("genres");
                            if (genreList != null && genreList instanceof java.util.List) {
                                java.util.List<com.statpod.model.GenreModel> genres = (java.util.List<com.statpod.model.GenreModel>) genreList;
                                for (com.statpod.model.GenreModel genre : genres) {
                        %>
                            <option value="<%= genre.getGenreId() %>" <%= podcast.getGenreId() == genre.getGenreId() ? "selected" : "" %>>
                                <%= genre.getGenreName() %>
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="form-group">
                    <textarea name="description" placeholder="Description" required><%= podcast.getDescription() %></textarea>
                </div>
                
                <div class="form-group">
                    <p>Current Image:</p>
                    <img src="${pageContext.request.contextPath}/images/podcasts/<%= podcast.getPodImg() %>" 
                         alt="Current Podcast Image" style="max-width: 100px; margin: 10px 0;">
                </div>
                
                <div class="form-group file-upload">
                    <label for="podcastImage">Upload New Podcast Image (optional)</label>
                    <input type="file" name="podcastImage" id="podcastImage" accept="image/*">
                    <input type="hidden" name="currentImage" value="<%= podcast.getPodImg() %>">
                </div>
                
                <div class="form-group">
                    <input type="submit" value="Update Podcast" class="podcast-button">
                </div>
            </form>
            <% } else if(request.getAttribute("error") == null) { %>
                <div class="error-message">Podcast not found or could not be loaded.</div>
            <% } %>
        </div>
    </main>
</div>

<%@ include file="/WEB-INF/pages/footer.jsp" %>

</body>
</html>