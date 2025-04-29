<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Podcast</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/new_podcast.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-wrapper">
    <main class="main-content">
        <div class="podcast-box">
            <img src="${pageContext.request.contextPath}/images/login/loginLogo.png" alt="Podcast Logo" class="logo">
            
            <h2>Add New Podcast</h2>
            <p class="subtitle">Share your podcast with the world</p>

            <%-- Display success message if present --%>
            <% if(request.getAttribute("success") != null && !((String)request.getAttribute("success")).isEmpty()) { %>
            <div class="success-message">${success}</div>
            <% } %>

            <%-- Display error message if present --%>
            <% if(request.getAttribute("error") != null && !((String)request.getAttribute("error")).isEmpty()) { %>
            <div class="error-message">${error}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/admin/new_podcast" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="text" name="podcastName" placeholder="Podcast Name" 
                        value="<%= request.getAttribute("podcastName") != null ? request.getAttribute("podcastName") : "" %>" required>
                </div>
                
                <div class="form-group">
                    <input type="text" name="hostName" placeholder="Host Name" 
                        value="<%= request.getAttribute("hostName") != null ? request.getAttribute("hostName") : "" %>" required>
                </div>
                
                <div class="form-group">
                    <%
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDate = dateFormat.format(new Date());
                    %>
                    <input type="text" name="date" value="<%= currentDate %>" readonly>
                </div>
                
                <div class="form-group">
                    <select name="genre">
                        <option value="">Select Genre</option>
                        <option value="1" <%= "1".equals(request.getAttribute("genre")) ? "selected" : "" %>>Comedy</option>
                        <option value="2" <%= "2".equals(request.getAttribute("genre")) ? "selected" : "" %>>Technology</option>
                        <option value="3" <%= "3".equals(request.getAttribute("genre")) ? "selected" : "" %>>Music</option>
                        <option value="4" <%= "4".equals(request.getAttribute("genre")) ? "selected" : "" %>>Business</option>
                        <option value="5" <%= "5".equals(request.getAttribute("genre")) ? "selected" : "" %>>Education</option>
                        <option value="6" <%= "6".equals(request.getAttribute("genre")) ? "selected" : "" %>>Horror</option>
                        <option value="7" <%= "7".equals(request.getAttribute("genre")) ? "selected" : "" %>>True Crime</option>
                        <option value="8" <%= "8".equals(request.getAttribute("genre")) ? "selected" : "" %>>History</option>
                        <option value="9" <%= "9".equals(request.getAttribute("genre")) ? "selected" : "" %>>Science</option>
                        <option value="10" <%= "10".equals(request.getAttribute("genre")) ? "selected" : "" %>>Health And Fitness</option>
                        <option value="11" <%= "11".equals(request.getAttribute("genre")) ? "selected" : "" %>>Fiction</option>
                        <option value="12" <%= "12".equals(request.getAttribute("genre")) ? "selected" : "" %>>Lifestyle</option>
                        <option value="13" <%= "13".equals(request.getAttribute("genre")) ? "selected" : "" %>>Interview</option>
                        <%
                            Object genreList = request.getAttribute("genres");
                            Integer selectedGenre = null;
                            if (request.getAttribute("genre") != null) {
                                try {
                                    selectedGenre = Integer.parseInt(request.getAttribute("genre").toString());
                                } catch (NumberFormatException ignored) {}
                            }
                            if (genreList != null && genreList instanceof java.util.List) {
                                java.util.List<com.statpod.model.GenreModel> genres = (java.util.List<com.statpod.model.GenreModel>) genreList;
                                for (com.statpod.model.GenreModel genre : genres) {
                        %>
                            <option value="<%= genre.getGenreId() %>" <%= (selectedGenre != null && selectedGenre == genre.getGenreId()) ? "selected" : "" %>>
                                <%= genre.getGenreName() %>
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="form-group">
                    <textarea name="description" placeholder="Description" required><%= request.getAttribute("description") != null ? request.getAttribute("description") : "" %></textarea>
                </div>
                
                <div class="form-group file-upload">
                    <label for="podcastImage">Upload Podcast Image</label>
                    <input type="file" name="podcastImage" id="podcastImage" accept="image/*">
                </div>
                
                <div class="form-group">
                    <input type="submit" value="Add Podcast" class="podcast-button">
                </div>
            </form>
        </div>
    </main>
</div>

<%@ include file="/WEB-INF/pages/footer.jsp" %>

</body>
</html>