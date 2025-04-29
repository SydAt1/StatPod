<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Access Denied</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/not_authorized.css">
    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <%@ include file="header.jsp" %>
    <div class="unauthorized-container">
        <div class="unauthorized-card">
            <div class="unauthorized-header">
                <span class="icon">
                    <i class="fas fa-lock fa-3x"></i>
                </span>
                <h1>Access Denied</h1>
            </div>
            <div class="unauthorized-body">
                <p>Sorry, you don't have permission to access this page. Please contact the administrator if you believe this is an error.</p>
                <a href="${pageContext.request.contextPath}/home" class="back-button">
                    <i class="fa fa-arrow-left" aria-hidden="true"></i> Return to Home
                </a>
            </div>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>