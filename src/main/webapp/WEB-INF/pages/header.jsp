<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.util.SessionUtil" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<style>
/* Inline critical styles to ensure dropdown works */
.user-dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  right: 0;
  background-color: rgb(25, 30, 40);
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1000;
  border-radius: 4px;
}

.user-dropdown.open .dropdown-content {
  display: block;
}

.dropdown-content a {
  color: white;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  text-align: left;
}

.dropdown-content a:hover {
  background-color: rgba(74, 144, 226, 0.2);
  color: #4a90e2;
}
</style>

<body>
<header>
<div class="navbar">
<a href="${pageContext.request.contextPath}/home" class="logo-link">
<div class="logo-container">
<img src="${pageContext.request.contextPath}/images/logo.png" alt="Website Logo" class="logo-img">
</div>
</a>

<nav>
<a href="${pageContext.request.contextPath}/about-us">About</a>
<a href="${pageContext.request.contextPath}/contact">Contact</a>

<%
boolean loggedIn = SessionUtil.isLoggedIn(request);
String username = SessionUtil.getCurrentUser(request);
Boolean isAdmin = (Boolean) SessionUtil.getAttribute(request, "isAdmin"); // Retrieve isAdmin from session

if (loggedIn) {
%>
<div class="user-dropdown" id="userDropdown">
    <div class="dropdown-toggle" id="dropdownToggle">
        <div class="profile-pic-container">
            <i class="fas fa-user"></i>
        </div>
        <i class="fas fa-chevron-down"></i>
    </div>
    <div class="dropdown-content">
        <a href="${pageContext.request.contextPath}/profile" class="username-link">
            <i class="fas fa-user-circle"></i> <%= username %>
        </a>
	<%
        if (isAdmin != null && isAdmin) { // Check isAdmin attribute
	%>
            <a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Admin Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/managePodcast"><i class="fas fa-podcast"></i> Manage Podcast</a>
            <a href="${pageContext.request.contextPath}/admin/new_podcast"><i class="fas fa-podcast"></i> Add New Podcast</a>
	<%
        }
	%>
        <a href="${pageContext.request.contextPath}/logout">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>
    </div>
	</div>
		<%
		} else {
			// Only show login/register links if not on those pages
			String currentPage = request.getRequestURI();
			if (!currentPage.endsWith("/login") && !currentPage.endsWith("/register")) {
		%>
			<a href="${pageContext.request.contextPath}/login" class="login-btn">Login</a>
		<%
			}
		}
		%>
	</nav>
	</div>
</header>

<script>
// JavaScript to ensure dropdown works correctly
document.addEventListener('DOMContentLoaded', function() {
    const dropdown = document.getElementById('userDropdown');
    const toggle = document.getElementById('dropdownToggle');
    
    if (dropdown && toggle) {
        // Toggle dropdown on click
        toggle.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            dropdown.classList.toggle('open');
        });
        
        // Close dropdown when clicking elsewhere
        document.addEventListener('click', function(e) {
            if (!dropdown.contains(e.target)) {
                dropdown.classList.remove('open');
            }
        });
    }
});
</script>
</body>
</html>