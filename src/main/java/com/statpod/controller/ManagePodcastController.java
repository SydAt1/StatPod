package com.statpod.controller;

import com.statpod.dao.PodcastDao;
import com.statpod.model.PodcastModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to display and handle actions on the Manage Podcasts admin view.
 */
@WebServlet("/admin/managePodcast")
public class ManagePodcastController extends HttpServlet {

    private PodcastDao podcastDao;

    @Override
    public void init() throws ServletException {
        try {
            podcastDao = new PodcastDao();
        } catch (Exception e) {
            // Log the error properly instead of just printing the stack trace
            e.printStackTrace();
            throw new ServletException("Error initializing PodcastDao", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if ("delete".equals(action) && idParam != null) {
            try {
                int podcastId = Integer.parseInt(idParam);
                podcastDao.deletePodcastById(podcastId);
                // Optionally set a success message for display on the page
                request.setAttribute("message", "Podcast deleted successfully.");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid podcast ID.");
                return;
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete podcast from the database.");
                return;
            }
        }

        // Fetch and display the list of podcasts
        try {
            List<PodcastModel> podcastList = podcastDao.getAllPodcasts();
            request.setAttribute("podcastList", podcastList);
            request.getRequestDispatcher("/WEB-INF/pages/admin/managePodcast.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving podcast list from the database.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    int podcastId = Integer.parseInt(idParam);
                    podcastDao.deletePodcastById(podcastId);
                    // Optionally set a success message
                    request.setAttribute("message", "Podcast deleted successfully.");
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid podcast ID.");
                    return;
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred during deletion.");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing podcast ID for deletion.");
                return;
            }
        } else {
            // Handle other POST requests if needed. For now, inform about invalid action.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);
            return;
        }

        // After a successful POST action (like delete), redirect to refresh the page
        doGet(request, response);
    }
}