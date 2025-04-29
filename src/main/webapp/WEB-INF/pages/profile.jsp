<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.model.PodcastUserModel" %>
<%@ page import="com.statpod.model.GenreModel" %>
<%
    PodcastUserModel user = (PodcastUserModel) request.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    GenreModel favoriteGenre = (GenreModel) request.getAttribute("favoriteGenre");
    String genreName = (favoriteGenre != null) ? favoriteGenre.getGenreName() : "Not specified";
%>
<!DOCTYPE html>
<html>
<head>
    <title>User Profile</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/profile.css">
    <style>
        .edit-profile-btn {
            background-color: #5865f2;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1rem;
            transition: background-color 0.3s ease;
        }
        .edit-profile-btn:hover {
            background-color: #0056b3;
        }
        .change-picture-btn {
            margin-top: 33px;
            font-size: 0.9rem;
            cursor: pointer;
            color: #5865f2;
            background: none;
            border: none;
            text-decoration: underline;
        }
    </style>
</head>
<body>

<%@ include file="header.jsp" %>

<div class="profile-container">
    <div class="profile-header"></div>

    <!-- Profile Form Start -->
    <form id="profileForm" method="post" action="<%= request.getContextPath() %>/profile" enctype="multipart/form-data">
        <div class="profile-user-info">
            <div class="profile-picture">
                <%
				    String userImage = (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) 
				                        ? user.getImageUrl() 
				                        : "default-profile.png"; // fallback default image
				%>

				<img id="profileImage" src="<%= request.getContextPath() + "/images/users/" + userImage %>" alt="Profile Picture" style="max-width: 150px; border-radius: 50%;">
            </div>

            <!-- Button to trigger file input -->
            <button type="button" class="change-picture-btn" onclick="document.getElementById('imageUpload').click();">Change Profile Picture</button>

            <!-- Hidden file input -->
            <input type="file" id="imageUpload" name="imageUrl" accept="image/png, image/jpeg, image/jpg" style="display: none;" onchange="previewImage(event)">
        </div>

        <div class="profile-details">
            <div class="detail-row">
                <div>
                    <div class="detail-label">Display Name</div>
                    <input class="detail-value-input" type="text" name="displayName" value="<%= user.getDisplayName() %>">
                </div>
            </div>

            <div class="detail-row">
                <div>
                    <div class="detail-label">Email</div>
                    <input class="detail-value-input" type="email" name="email" value="<%= user.getEmail() %>">
                </div>
            </div>

            <div class="detail-row">
                <div>
                    <div class="detail-label">Favorite Genre</div>
                    <input class="detail-value-input" type="text" name="favoriteGenre" value="<%= genreName %>">
                </div>
            </div>
        </div>

        <div style="margin-top: 10px; text-align: center;">
            <button type="submit" class="edit-profile-btn">Save Changes</button>
        </div>
    </form>
    <!-- Profile Form End -->

</div>

<%@ include file="footer.jsp" %>

<script>
    // JavaScript to preview selected image
    function previewImage(event) {
        const reader = new FileReader();
        reader.onload = function() {
            const output = document.getElementById('profileImage');
            output.src = reader.result;
        };
        reader.readAsDataURL(event.target.files[0]);
    }
</script>

</body>
</html>
