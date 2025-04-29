<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="page-wrapper">
    <main class="main-content">
        <div class="register-box">
            <img src="${pageContext.request.contextPath}/images/login/loginLogo.png" alt="Logo" class="logo">
            
            <h2>Create Account</h2>
            <p class="subtitle">Join our community today</p>

            <%-- Display success message if present --%>
            <% if(request.getAttribute("success") != null && !((String)request.getAttribute("success")).isEmpty()) { %>
            <div class="success-message">${success}</div>
            <% } %>

            <%-- Display error message if present --%>
            <% if(request.getAttribute("error") != null && !((String)request.getAttribute("error")).isEmpty()) { %>
            <div class="error-message">${error}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="text" name="username" placeholder="Username" 
                        value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" required>
                </div>
                
                <div class="form-group">
                    <input type="email" name="email" placeholder="Email Address" 
                        value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
                </div>
                
                <div class="form-group">
                    <input type="text" name="displayName" placeholder="Display Name" 
                        value="<%= request.getAttribute("displayName") != null ? request.getAttribute("displayName") : "" %>" required>
                </div>
                
                <div class="form-group">
                    <select name="favoriteGenre">
                        <option value="">Select Favorite Genre</option>
                        <option value="1" <%= "1".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Comedy</option>
                        <option value="2" <%= "2".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Technology</option>
                        <option value="3" <%= "3".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Music</option>
                        <option value="4" <%= "4".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Business</option>
                        <option value="5" <%= "5".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Education</option>
                        <option value="6" <%= "6".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Horror</option>
                        <option value="7" <%= "7".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>True Crime</option>
                        <option value="8" <%= "8".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>History</option>
                        <option value="9" <%= "9".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Science</option>
                        <option value="10" <%= "10".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Health And Fitness</option>
                        <option value="11" <%= "11".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Fiction</option>
                        <option value="12" <%= "12".equals(request.getAttribute("favoriteGenre")) ? "selected" : "" %>>Lifestyle</option>
                        <%
                            Object genreList = request.getAttribute("genres");
                            Integer selectedGenre = null;
                            if (request.getAttribute("favoriteGenre") != null) {
                                try {
                                    selectedGenre = Integer.parseInt(request.getAttribute("favoriteGenre").toString());
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
			    <div class="password-container">
			        <input type="password" name="password" id="password" placeholder="Password" required>
			        <button type="button" class="password-toggle" onclick="togglePassword('password')">
			            <i class="fa-solid fa-eye"></i>
			        </button>
			    </div>
			</div>
			
			<div class="form-group">
			    <div class="password-container">
			        <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm Password" required>
			        <button type="button" class="password-toggle" onclick="togglePassword('confirmPassword')">
			            <i class="fa-solid fa-eye"></i>
			        </button>
			    </div>
			</div>
                
                <div class="form-group file-upload">
                    <label for="imageUrl">Upload Profile Picture</label>
                    <input type="file" name="imageUrl" id="imageUrl" accept="image/*">
                </div>
                
                <div class="form-group">
                    <input type="submit" value="Register" class="register-button">
                </div>
            </form>
            
            <p class="login-text">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login">Log in to Statpod</a>
            </p>
        </div>
    </main>
</div>

<%@ include file="/WEB-INF/pages/footer.jsp" %>

<script>
function togglePassword(fieldId) {
    const passwordField = document.getElementById(fieldId);
    const toggleIcon = passwordField.nextElementSibling.querySelector('i');
    
    if (passwordField.type === "password") {
        passwordField.type = "text";
        toggleIcon.classList.remove('fa-eye');
        toggleIcon.classList.add('fa-solid fa-eye-slash');
    } else {
        passwordField.type = "password";
        toggleIcon.classList.remove('fa-solid fa-eye-slash');
        toggleIcon.classList.add('fa-eye');
    }
}
</script>
</body>
</html>