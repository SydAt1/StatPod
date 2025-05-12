<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.dao.AdminDashboardDao" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
    // Get dashboard data
    AdminDashboardDao dashboardDao = new AdminDashboardDao();
    int totalUsers = dashboardDao.getTotalUserCount();
    int totalPodcasts = dashboardDao.getTotalPodcastCount();
    Map<String, Integer> podcastsByGenre = dashboardDao.getPodcastCountByGenre();
    Map<String, Integer> usersByFavoriteGenre = dashboardDao.getUserCountByFavoriteGenre();
    Map<String, String> recentPodcasts = dashboardDao.getRecentlyAddedPodcasts(5);
    
    // Format the current date for the dashboard display
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
    String formattedDate = dateFormat.format(new Date());
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - StatPod</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/admin_dashboard.css">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Chart.js for charts -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/pages/header.jsp" %>
    <div class="dashboard-container">
        <!-- Main Content -->
        <div class="main-content">
            <!-- Top Header -->
            <div class="header">
                <h1>Dashboard</h1>
                <div class="user-info">
                    <span><%= formattedDate %></span>
                    <div class="admin-profile">
                        <span>Welcome, <%= username %></span>
                    </div>
                </div>
            </div>

            <!-- Stats Overview -->
            <div class="stats-overview">
                <div class="stat-card">
                    <div class="stat-info">
                        <h3><%= totalUsers %></h3>
                        <p>Total Users</p>
                    </div>
                    <div class="stat-icon users">
                        <i class="fas fa-users"></i>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-info">
                        <h3><%= totalPodcasts %></h3>
                        <p>Total Podcasts</p>
                    </div>
                    <div class="stat-icon podcasts">
                        <i class="fas fa-podcast"></i>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-info">
                        <h3><%= podcastsByGenre.size() %></h3>
                        <p>Genres</p>
                    </div>
                    <div class="stat-icon genres">
                        <i class="fas fa-list"></i>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-info">
                        <h3>Admin</h3>
                        <p>User Status</p>
                    </div>
                    <div class="stat-icon admin">
                        <i class="fas fa-user-shield"></i>
                    </div>
                </div>
            </div>

            <!-- Charts & Tables -->
            <div class="data-visualizations">
                <!-- Charts Row -->
                <div class="charts-row">
                    <div class="chart-container">
                        <h2>Podcasts by Genre</h2>
                        <canvas id="podcastsChart"></canvas>
                    </div>
                    <div class="chart-container">
                        <h2>Users by Favorite Genre</h2>
                        <canvas id="usersChart"></canvas>
                    </div>
                </div>
                
                <!-- Recent Podcasts -->
                <div class="recent-table">
                    <h2>Recently Added Podcasts</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Podcast Name</th>
                                <th>Release Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Map.Entry<String, String> entry : recentPodcasts.entrySet()) { %>
                            <tr>
                                <td><%= entry.getKey() %></td>
                                <td><%= entry.getValue() %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <!-- Quick Links -->
            <div class="quick-actions">
                <h2>Quick Actions</h2>
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/admin/new_podcast" class="action-btn"><i class="fas fa-plus"></i> Add New Podcast</a>
                    <a href="${pageContext.request.contextPath}/admin/manage_users" class="action-btn"><i class="fas fa-users-cog"></i> Add New User</a>
                    <a href="add_genre.jsp" class="action-btn"><i class="fas fa-folder-plus"></i> Add New Genre</a>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript for Charts -->
    <script>
        // Prepare data for Podcasts by Genre chart
        const podcastGenres = [<%= String.join(", ", podcastsByGenre.keySet().stream().map(genre -> "'" + genre + "'").toArray(String[]::new)) %>];
        const podcastCounts = [<%= String.join(", ", podcastsByGenre.values().stream().map(Object::toString).toArray(String[]::new)) %>];
        
        // Prepare data for Users by Favorite Genre chart
        const userGenres = [<%= String.join(", ", usersByFavoriteGenre.keySet().stream().map(genre -> "'" + genre + "'").toArray(String[]::new)) %>];
        const userCounts = [<%= String.join(", ", usersByFavoriteGenre.values().stream().map(Object::toString).toArray(String[]::new)) %>];
        
        // Create Podcasts by Genre chart
        const podcastsCtx = document.getElementById('podcastsChart').getContext('2d');
        new Chart(podcastsCtx, {
            type: 'doughnut',
            data: {
                labels: podcastGenres,
                datasets: [{
                    data: podcastCounts,
                    backgroundColor: [
                        '#4e73df', '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b',
                        '#5a5c69', '#858796', '#6f42c1', '#20c9a6', '#fd7e14'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right',
                    }
                }
            }
        });
        
        // Create Users by Favorite Genre chart
        const usersCtx = document.getElementById('usersChart').getContext('2d');
        new Chart(usersCtx, {
            type: 'bar',
            data: {
                labels: userGenres,
                datasets: [{
                    label: 'Number of Users',
                    data: userCounts,
                    backgroundColor: '#4e73df',
                    borderColor: '#4e73df',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        precision: 0
                    }
                }
            }
        });
    </script>
</body>
</html>