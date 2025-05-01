<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.statpod.model.PodcastUserModel" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Users</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin/managePodcast.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
    <style>
        .remove-button {
            background-color: #e74c3c;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
            border-radius: 4px;
            font-size: 14px;
        }

        .remove-button:hover {
            background-color: #c0392b;
        }

        .dot-menu {
            position: relative;
            display: inline-block;
            cursor: pointer;
        }

        .actions {
            display: none;
            position: absolute;
            right: 0;
            background-color: white;
            box-shadow: 0px 2px 6px rgba(0,0,0,0.2);
            padding: 8px;
            z-index: 1;
            border-radius: 4px;
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/pages/header.jsp" %>

<div class="container">
    <h2>Manage Users</h2>

    <% if (request.getAttribute("message") != null) { %>
        <div class="message success">
            <%= request.getAttribute("message") %>
        </div>
    <% } %>

    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Profile Image</th>
                <th>Username</th>
                <th>Display Name</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
            int index = 1;
            List<PodcastUserModel> userList = (List<PodcastUserModel>) request.getAttribute("userList");
            if (userList != null) {
                for (PodcastUserModel user : userList) {
            %>
            <tr>
                <td><%= index++ %></td>
                <td>
                    <% if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) { %>
                        <img class="pod-img" src='${pageContext.request.contextPath}/images/users/<%= user.getImageUrl() %>' alt="User Image">
                    <% } else { %>
                        <img class="pod-img" src='${pageContext.request.contextPath}/images/users/default-user.png' alt="Default User Image">
                    <% } %>
                </td>
                <td><%= user.getUsername() %></td>
                <td><%= user.getDisplayName() %></td>
                <td><%= user.getEmail() %></td>
                <td>
                    <div class="dot-menu">⋮
                        <div class="actions">
                            <form method="post" action="${pageContext.request.contextPath}/admin/manage_users" 
                                  onsubmit="return confirm('Are you sure you want to delete this user? This action cannot be undone.');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="username" value="<%= user.getUsername() %>">
                                <button type="submit" class="remove-button">Remove</button>
                            </form>
                        </div>
                    </div>
                </td>
            </tr>
            <% 
                }
            }
            %>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/pages/footer.jsp" %>

<script>
// Close dropdown when clicking outside
window.onclick = function(event) {
    if (!event.target.matches('.dot-menu')) {
        var dropdowns = document.getElementsByClassName("actions");
        for (var i = 0; i < dropdowns.length; i++) {
            dropdowns[i].style.display = "none";
        }
    }
};

// Toggle dropdown visibility
var dotMenus = document.getElementsByClassName('dot-menu');
for (var i = 0; i < dotMenus.length; i++) {
    dotMenus[i].addEventListener('click', function(event) {
        var actions = this.getElementsByClassName('actions')[0];
        var allActions = document.getElementsByClassName('actions');
        for (var j = 0; j < allActions.length; j++) {
            allActions[j].style.display = "none";
        }
        actions.style.display = actions.style.display === "block" ? "none" : "block";
        event.stopPropagation();
    });
}
</script>

</body>
</html>
