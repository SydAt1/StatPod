package com.statpod.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.statpod.config.DbConfig;
import com.statpod.dao.PodcastDao;
import com.statpod.dao.UserDao;
import com.statpod.dao.UserPodcastDao;
import com.statpod.model.PodcastModel;
import com.statpod.model.PodcastUserModel;
import com.statpod.model.UserPodcastsModel;
import com.statpod.util.SessionUtil;

@WebServlet("/podstats")
public class PodStatController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect to login if not logged in
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect("login");
            return;
        }

        // Get username from session by default
        String username = SessionUtil.getCurrentUser(request);

        // Allow admin to view other users' stats
        boolean isAdmin = SessionUtil.isAdminUser(request);
        if (isAdmin && request.getParameter("username") != null) {
            username = request.getParameter("username");
        }

        Connection conn = null;
        try {
            conn = DbConfig.getDbConnection();

            UserPodcastDao userPodcastDao = new UserPodcastDao(conn);
            PodcastDao podcastDao = new PodcastDao();

            // Get played and liked podcasts for the user
            List<UserPodcastsModel> playedPodcasts = userPodcastDao.getUserPodcasts(username);
            List<UserPodcastsModel> likedPodcasts = userPodcastDao.getLikedPodcasts(username);

            // Enrich data with podcast info
            List<Map<String, Object>> playedPodcastsInfo = enrichPodcastData(playedPodcasts, podcastDao);
            List<Map<String, Object>> likedPodcastsInfo = enrichPodcastData(likedPodcasts, podcastDao);

            // Total play count
            int totalPlays = playedPodcasts.stream().mapToInt(UserPodcastsModel::getPlay).sum();

            // Add attributes to request
            request.setAttribute("username", username);
            request.setAttribute("isAdmin", isAdmin);
            request.setAttribute("totalPlays", totalPlays);
            request.setAttribute("playedPodcasts", playedPodcastsInfo);
            request.setAttribute("likedPodcasts", likedPodcastsInfo);
            request.setAttribute("likedCount", likedPodcasts.size());

            // Admin: also get all users for dropdown
            if (isAdmin) {
                UserDao userDao = new UserDao(conn);
                List<PodcastUserModel> userList = userDao.getAllUsers();
                request.setAttribute("userList", userList);
            }

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/pages/podstats.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } finally {
            // Close connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Map<String, Object>> enrichPodcastData(List<UserPodcastsModel> userPodcasts, PodcastDao podcastDao)
            throws SQLException, ClassNotFoundException {

        List<Map<String, Object>> result = new ArrayList<>();

        for (UserPodcastsModel userPodcast : userPodcasts) {
            PodcastModel podcast = podcastDao.getPodcastById(userPodcast.getPodcastId());
            if (podcast != null) {
                Map<String, Object> podcastInfo = new HashMap<>();
                podcastInfo.put("id", podcast.getPodcastId());
                podcastInfo.put("title", podcast.getPodcastName());
                podcastInfo.put("image", podcast.getPodImg());
                podcastInfo.put("playCount", userPodcast.getPlay());
                podcastInfo.put("liked", userPodcast.isLiked());
                result.add(podcastInfo);
            }
        }

        return result;
    }
}