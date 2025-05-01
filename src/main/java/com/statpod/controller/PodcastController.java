package com.statpod.controller;

import com.statpod.config.DbConfig;
import com.statpod.dao.PodcastDao;
import com.statpod.dao.UserPodcastDao;
import com.statpod.model.PodcastModel;
import com.statpod.model.UserPodcastsModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/podcast")
public class PodcastController extends HttpServlet {
    private PodcastDao podcastDao;

    @Override
    public void init() throws ServletException {
        podcastDao = new PodcastDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String podcastIdParam = request.getParameter("id");

        if (podcastIdParam != null) {
            try {
                int podcastId = Integer.parseInt(podcastIdParam);
                PodcastModel selectedPodcast = podcastDao.getPodcastById(podcastId);

                if (selectedPodcast != null) {
                    request.setAttribute("podcast", selectedPodcast);

                    HttpSession session = request.getSession(false);
                    String username = (session != null) ? (String) session.getAttribute("username") : null;

                    if (username != null) {
                        try (Connection connection = DbConfig.getDbConnection()) {
                            UserPodcastDao userPodcastDao = new UserPodcastDao(connection);
                            UserPodcastsModel userPodcast = userPodcastDao.getUserPodcast(username, podcastId);
                            boolean isLiked = (userPodcast != null && userPodcast.isLiked());
                            request.setAttribute("isLiked", isLiked);
                        }
                    }

                    request.getRequestDispatcher("/WEB-INF/pages/podcast.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Podcast not found.");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid podcast ID.");
            } catch (Exception e) {
                throw new ServletException("Error retrieving podcast", e);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        int podcastId = Integer.parseInt(request.getParameter("podcastId"));
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = request.getContextPath() + "/podcast?id=" + podcastId;
        }

        try (Connection connection = DbConfig.getDbConnection()) {
            UserPodcastDao userPodcastDao = new UserPodcastDao(connection);

            // Ensure entry exists
            if (userPodcastDao.getUserPodcast(username, podcastId) == null) {
                userPodcastDao.addUserPodcast(username, podcastId);
            }

            switch (action) {
                case "play":
                    userPodcastDao.incrementPlayCount(username, podcastId);
                    break;
                case "toggleLike":
                    userPodcastDao.toggleLikedStatus(username, podcastId);
                    break;
                default:
                    // Do nothing or log invalid action
                    break;
            }

            response.sendRedirect(referer);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }
}
