<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.statpod.model.PodcastModel" %>
<%@ page import="com.statpod.dao.PodcastDao" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Discover Your Next Podcast</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <!-- Hero outside any container -->
	<div class="hero">
	    <div class="container hero-content">
	        <h1>Discover Your Next Podcast</h1>
	        <p>Listen to thousands of podcasts on any topic. Find what speaks to you and start your audio journey today</p>
	    </div>
	</div>

    <div class="container">
        <section class="recommendations">
            <h2 class="section-heading"><span class="icon">✨</span> Recommended For You</h2>
            <div class="recommendation-row">
                <%
                PodcastDao podcastDAO = new PodcastDao();
                List<PodcastModel> recommendedPods = podcastDAO.getRecommendedPodcasts();
                for (PodcastModel pod : recommendedPods) {
                    String genreName = podcastDAO.getGenreName(pod.getGenreId());
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
                <div class="recommendation-card">
                    <div class="recommendation-image" style="background-image: url('images/podcasts/<%= pod.getPodImg() %>');"></div>
                    <div class="recommendation-info">
                        <h3><%= pod.getPodcastName() %></h3>
                        <p><%= pod.getHostName() %></p>
                        <div class="why-recommended"><%= reason %></div>
                    </div>
                </div>
                <% } %>
            </div>
        </section>

        <div class="category-container">
            <div class="category-filters">
                <button class="category-btn active">All</button>
                <%
                List<String[]> genres = (List<String[]>) request.getAttribute("genres");
                if (genres != null) {
                    for (String[] genre : genres) {
                %>
                <button class="category-btn" data-genre-id="<%= genre[0] %>"><%= genre[1] %></button>
                <%
                    }
                }
                %>
            </div>
        </div>

        <h2 class="section-heading"><span class="icon">🔍-</span> Explore Podcasts</h2>
        <div class="podcast-grid">
		    <%
		    List<PodcastModel> allPods = podcastDAO.getAllPodcasts();
		    for (PodcastModel pod : allPods) {
		        String genreName = podcastDAO.getGenreName(pod.getGenreId());
		    %>
		    <div class="podcast-card" data-genre-id="<%= pod.getGenreId() %>">
		        <div class="podcast-image" style="background-image: url('images/podcasts/<%= pod.getPodImg() %>');"></div>
		        <div class="podcast-info">
		            <h3><%= pod.getPodcastName() %></h3>
		            <p><%= pod.getHostName() %></p>
		        </div>
		    </div>
		    <% } %>
		</div>
    </div>
    
    <script>
	    document.addEventListener('DOMContentLoaded', function () {
	        const categoryButtons = document.querySelectorAll('.category-btn');
	        const podcastCards = document.querySelectorAll('.podcast-card');
	
	        categoryButtons.forEach(button => {
	            button.addEventListener('click', function () {
	                categoryButtons.forEach(btn => btn.classList.remove('active'));
	                this.classList.add('active');
	                const genreId = this.dataset.genreId;
	
	                podcastCards.forEach(card => {
	                    if (genreId && card.dataset.genreId !== genreId) {
	                        card.style.display = 'none';
	                    } else {
	                        card.style.display = 'block';
	                    }
	                });
	
	                if(!genreId){
	                    podcastCards.forEach(card=>{
	                        card.style.display = 'block';
	                    })
	                }
	            });
	        });
	    });
    </script>
</body>
</html>