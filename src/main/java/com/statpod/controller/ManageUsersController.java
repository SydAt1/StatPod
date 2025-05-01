package com.statpod.controller;

import com.statpod.config.DbConfig;
import com.statpod.dao.UserDao;
import com.statpod.model.PodcastUserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to display and handle actions on the Manage Users admin view.
 * Admin authentication is now handled by AuthenticatorFilter.
 */
@WebServlet("/admin/manage_users")
public class ManageUsersController extends HttpServlet {

    private UserDao userDao;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = DbConfig.getDbConnection();
            userDao = new UserDao(connection);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error initializing UserDao", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String username = request.getParameter("username");

        if ("delete".equals(action) && username != null) {
            try {
                userDao.deleteUser(username);
                // Set a success message for display on the page
                request.setAttribute("message", "User deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user from the database.");
                return;
            }
        }

        // Fetch and display the list of users
        try {
            List<PodcastUserModel> userList = userDao.getAllUsers();
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("/WEB-INF/pages/admin/manage_users.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving user list from the database.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String username = request.getParameter("username");
            if (username != null) {
                try {
                    userDao.deleteUser(username);
                    // Set a success message
                    request.setAttribute("message", "User deleted successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred during deletion.");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing username for deletion.");
                return;
            }
        } else {
            // Handle other POST requests if needed
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);
            return;
        }

        // After a successful POST action (like delete), redirect to refresh the page
        doGet(request, response);
    }
}