<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" integrity="sha512-..." crossorigin="anonymous" referrerpolicy="no-referrer" />

</head>
<body>

<div class="page-wrapper">
    <main class="main-content">
        <div class="login-box">

            <img src="${pageContext.request.contextPath}/images/login/loginLogo.png"  alt="Logo" class="logo">
            
            <h2>Log in to Statpod</h2>

            <%-- Display error message if present --%>
            <% if(request.getAttribute("error") != null) { %>
            <div class="error-message">${error}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <input type="text" name="username" placeholder="Email or username"
                           value="${not empty username ? username : not empty param.username ? param.username : ''}">
                </div>

                <div class="form-group password-wrapper">
				    <input type="password" name="password" placeholder="Password" id="password" required>
				    <i class="fa-solid fa-eye toggle-password" id="togglePassword"></i>
				</div>

                <div class="form-group">
                    <input type="submit" value="Login" class="login-button">
                </div>
            </form>

            <p class="signup-text">
                Don't have an account?
                <a href="${pageContext.request.contextPath}/register">Sign up for Statpod</a>
            </p>
        </div>
        
    </main>
</div>

<%@ include file="footer.jsp" %>
<script>
    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    togglePassword.addEventListener("click", function () {
        const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);
        this.classList.toggle("fa-eye");
        this.classList.toggle("fa-eye-slash");
    });
</script>


</body>
</html>