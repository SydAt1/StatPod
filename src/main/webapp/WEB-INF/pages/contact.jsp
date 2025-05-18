<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Contact Us</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/contact.css">
</head>
<body>

    <%@ include file="header.jsp" %>

	<section class="contact-section">
	    <div class="container">
	        <div class="contact-info">
	            <span class="tag">Contact Us</span>
	            <h2>Let's Get In Touch.</h2>
	            <p>Or just reach out manually to <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ">statpod@pods.com</a></p>
	        </div>
	        <div class="contact-form">
	            <form action="#" method="post">
	                <div class="form-group">
	                    <label for="full-name">Full Name</label>
	                    <input type="text" id="full-name" name="full_name" placeholder="Enter your full name..." required>
	                    </div>
	                <div class="form-group">
	                    <label for="email">Email Address</label>
	                    <input type="email" id="email" name="email_address" placeholder="Enter your email address..." required>
	                     </div>
	                <div class="form-group">
	                    <label for="message">Message</label>
	                    <textarea id="message" name="message" rows="5" placeholder="Enter your main text here..." required></textarea>
	                    </div>
	                <div class="form-group terms">
	                    <input type="checkbox" id="terms" name="terms" required>
	                    <label for="terms">I hereby agree to our <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" target="_blank">Privacy Policy terms</a>.</label>
	                </div>
	                <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" class="submit-button">
					    Submit Form &rarr;
					</a>
	            </form>
	        </div>
	    </div>
	</section>
</body>
</html>
