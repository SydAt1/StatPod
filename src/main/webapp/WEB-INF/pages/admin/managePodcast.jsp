<%@ page import="com.statpod.dao.PodcastDao" %>
<%@ page import="com.statpod.model.PodcastModel" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    PodcastDao dao = new PodcastDao();
    List<PodcastModel> podcastList = dao.getAllPodcasts();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Podcasts</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/managePodcast.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

<%@ include file="/WEB-INF/pages/header.jsp" %>
<div class="container">
    <h2>Manage Podcasts</h2>
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Cover</th>
                <th>Title</th>
                <th>Host</th>
                <th>Release Date</th>
                <th>Genre</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <%
                int index = 1;
                for (PodcastModel podcast : podcastList) {
            %>
            <tr>
                <td><%= index++ %></td>
                <td><img class="pod-img" src='${pageContext.request.contextPath}/images/podcasts/<%= podcast.getPodImg() %>' alt="cover"></td>
                <td><%= podcast.getPodcastName() %></td>
                <td><%= podcast.getHostName() %></td>
                <td><%= podcast.getReleaseDate() %></td>
                <td><%= dao.getGenreName(podcast.getGenreId()) %></td>
                <td>
                    <div class="dot-menu">⋮
                        <div class="actions">
                            <a href="${pageContext.request.contextPath}/admin/editPodcast?id=<%= podcast.getPodcastId() %>">Edit</a>
                            <a href="${pageContext.request.contextPath}/admin/managePodcast?action=delete&id=<%= podcast.getPodcastId() %>"
                               onclick="return confirm('Are you sure you want to delete this podcast?');">Remove</a>
                        </div>
                    </div>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp" %>

<script>
    // Close the dropdown menu when clicking elsewhere
    window.onclick = function(event) {
        if (!event.target.matches('.dot-menu')) {
            var dropdowns = document.getElementsByClassName("actions");
            for (var i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.style.display === "block") {
                    openDropdown.style.display = "none";
                }
            }
        }
    }

    // Toggle dropdown visibility when clicking on the dots
    var dotMenus = document.getElementsByClassName('dot-menu');
    for (var i = 0; i < dotMenus.length; i++) {
        dotMenus[i].addEventListener('click', function(event) {
            var actions = this.getElementsByClassName('actions')[0];
            if (actions.style.display === "block") {
                actions.style.display = "none";
            } else {
                // Close all other open menus first
                var allActions = document.getElementsByClassName('actions');
                for (var j = 0; j < allActions.length; j++) {
                    allActions[j].style.display = "none";
                }
                actions.style.display = "block";
            }
            event.stopPropagation();
        });
    }
</script>
</body>
</html>