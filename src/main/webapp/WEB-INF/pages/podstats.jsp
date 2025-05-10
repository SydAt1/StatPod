<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>User Podcast Statistics</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f6f6f6;
      color: #333;
    }
    
    .top-bar {
      background-color: #1a1a1a;
      color: white;
      padding: 12px 24px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .logo {
      font-size: 24px;
      font-weight: bold;
    }
    
    .nav-links {
      display: flex;
      gap: 24px;
      align-items: center;
    }
    
    .nav-links a {
      color: white;
      text-decoration: none;
    }
    
    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }
    
    h1 {
      font-size: 28px;
      margin-bottom: 24px;
      font-weight: 600;
    }
    
    .stats-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
    }
    
    .user-select {
      padding: 8px 12px;
      border-radius: 4px;
      border: 1px solid #ddd;
      font-size: 16px;
    }
    
    .stats-container {
      margin-bottom: 32px;
    }
    
    .stats-heading {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
    }
    
    .stats-title {
      font-size: 20px;
      font-weight: 600;
    }
    
    .stats-count {
      background-color: #f0f0f0;
      padding: 4px 12px;
      border-radius: 16px;
      font-weight: 500;
    }
    
    table {
      width: 100%;
      border-collapse: collapse;
      background-color: white;
      border-radius: 4px;
      overflow: hidden;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }
    
    th {
      text-align: left;
      padding: 12px 16px;
      background-color: #f5f5f5;
      border-bottom: 1px solid #ddd;
      font-weight: 600;
    }
    
    td {
      padding: 12px 16px;
      border-bottom: 1px solid #eee;
    }
    
    tr:last-child td {
      border-bottom: none;
    }
    
    img.pod-img {
		width: 40px;
		height: 40px;
		border-radius: 4px;
		object-fit: cover;
	}
    
    
    .podcast-image {
      width: 50px;
      height: 50px;
      border-radius: 4px;
      object-fit: cover;
    }
    
    .play-count {
      font-weight: 500;
      color: #2563eb;
    }
    
    .no-data {
      text-align: center;
      padding: 24px;
      color: #666;
      background-color: white;
      border-radius: 4px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }
    
    .user-info {
      margin-bottom: 24px;
      padding: 16px;
      background-color: white;
      border-radius: 4px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }
    
    .user-info h2 {
      margin-top: 0;
      margin-bottom: 8px;
      font-size: 22px;
    }
  </style>
</head>
<body>
<%@ include file="header.jsp" %>
  
  <div class="container">
    <div class="stats-header">
      <h1>User Podcast Statistics</h1>
      
      <!-- Only show user selection for admin users -->
      <c:if test="${isAdmin}">
		  <form id="userForm" method="get" action="podstats" target="hiddenFrame">
		    <select class="user-select" id="userSelect" name="username">
		      <c:forEach var="user" items="${userList}">
		        <option value="${user.username}" ${user.username eq username ? 'selected' : ''}>
		          ${user.displayName}
		        </option>
		      </c:forEach>
		    </select>
		  </form>
		</c:if>
    </div>
    
    <div class="user-info">
      <h2>Stats for: ${username}</h2>
      <p>Viewing your podcast listening activity and liked content.</p>
    </div>
    
    <div class="stats-container">
      <div class="stats-heading">
        <div class="stats-title">Played Podcasts</div>
        <div class="stats-count">${totalPlays} total plays</div>
      </div>
      
      <c:choose>
        <c:when test="${not empty playedPodcasts}">
          <table>
            <thead>
              <tr>
                <th>#</th>
                <th>Podcast</th>
                <th>Title</th>
                <th>Play Count</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${playedPodcasts}" var="podcast" varStatus="status">
                <tr>
                  <td>${status.index + 1}</td>
                  <td><img class="pod-img" src="${pageContext.request.contextPath}/images/podcasts/${podcast.image}" alt="${podcast.title}" onerror="this.src='assets/img/default-podcast.png'"></td>
                  <td>${podcast.title}</td>
                  <td class="play-count">${podcast.playCount}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <div class="no-data">No played podcasts found for this user.</div>
        </c:otherwise>
      </c:choose>
    </div>
    
    <div class="stats-container">
      <div class="stats-heading">
        <div class="stats-title">Liked Podcasts</div>
        <div class="stats-count">${likedCount} podcasts</div>
      </div>
      
      <c:choose>
        <c:when test="${not empty likedPodcasts}">
          <table>
            <thead>
              <tr>
                <th>#</th>
                <th>Podcast</th>
                <th>Title</th>
                <th>Play Count</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${likedPodcasts}" var="podcast" varStatus="status">
                <tr>
                  <td>${status.index + 1}</td>
                  <td><img class="pod-img" src="${pageContext.request.contextPath}/images/podcasts/${podcast.image}" alt="${podcast.title}" onerror="this.src='assets/img/default-podcast.png'"></td>
                  <td>${podcast.title}</td>
                  <td class="play-count">${podcast.playCount}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <div class="no-data">No liked podcasts found for this user.</div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
  
  <c:if test="${isAdmin}">
    <script>
      document.getElementById('userSelect').addEventListener('change', function() {
        const username = this.value;
        window.location.href = 'podstats?username=' + username;
      });
    </script>
  </c:if>
  
  <%@ include file="footer.jsp" %>
</body>
</html>