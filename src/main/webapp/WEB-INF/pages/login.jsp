<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
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

                <div class="form-group">
                    <input type="password" name="password" placeholder="Password" required>
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

</body>
</html>