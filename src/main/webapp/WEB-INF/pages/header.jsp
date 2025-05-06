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

/* Inline critical styles for search functionality */
.search-input-container {
  position: absolute;
  right: 0;
  top: 0;
  display: flex;
  align-items: center;
  background-color: rgb(25, 30, 40);
  border: 1px solid #333;
  border-radius: 4px;
  overflow: hidden;
  width: 0;
  opacity: 0;
  transition: width 0.3s ease, opacity 0.3s ease;
}

.search-input-container.active {
  width: 240px;
  opacity: 1;
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
<!-- Netflix-style Search Component -->
<div class="search-container">
    <button type="button" class="search-button" id="searchButton">
        <i class="fas fa-search"></i>
    </button>
    <div class="search-input-container" id="searchInputContainer">
        <input type="text" class="search-input" placeholder="Search..." id="searchInput">
        <button type="button" class="search-close-button" id="searchCloseButton">
            <i class="fas fa-times"></i>
        </button>
    </div>
</div>

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
            <a href="${pageContext.request.contextPath}/admin/manage_users"><i class="fas fa-users-cog"></i> Manage Users</a>
            
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
    
    // Netflix-style search functionality
    const searchButton = document.getElementById('searchButton');
    const searchInputContainer = document.getElementById('searchInputContainer');
    const searchInput = document.getElementById('searchInput');
    const searchCloseButton = document.getElementById('searchCloseButton');
    
    // Stop event propagation in search container
    searchInputContainer.addEventListener('click', function(e) {
        e.stopPropagation();
    });
    
    // Open search field when search button is clicked
    searchButton.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation(); // Stop event from bubbling up
        searchInputContainer.classList.add('active');
        setTimeout(() => {
            searchInput.focus(); // Auto focus with slight delay to ensure animation completes
        }, 300);
    });
    
    // Close search field when close button is clicked
    searchCloseButton.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation(); // Stop event from bubbling up
        searchInputContainer.classList.remove('active');
        searchInput.value = ''; // Clear the input when closed
    });
    
    // Handle search submission
    searchInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            // Implement search functionality here
            // For example:
            if (searchInput.value.trim() !== '') {
                window.location.href = '${pageContext.request.contextPath}/search?q=' + encodeURIComponent(searchInput.value);
            }
        }
    });
    
    // Close search when clicking elsewhere on the page
    document.addEventListener('click', function(e) {
        if (!searchInputContainer.contains(e.target) && e.target !== searchButton) {
            searchInputContainer.classList.remove('active');
        }
    });
});
</script>
</body>
</html>