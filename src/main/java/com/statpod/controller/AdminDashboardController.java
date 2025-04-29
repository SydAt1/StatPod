package com.statpod.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.statpod.dao.AdminDashboardDao;
import com.statpod.model.GenreModel;
import com.statpod.util.SessionUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // Admin check
//    	if (!SessionUtil.isAdminUser(request)) {
//            request.getRequestDispatcher("/WEB-INF/pages/not_authorized.jsp").forward(request, response);
//            return;
//        }
    	
        
        try {
            // Get dashboard data
            AdminDashboardDao dashboardDao = new AdminDashboardDao();
            int totalUsers = dashboardDao.getTotalUserCount();
            int totalPodcasts = dashboardDao.getTotalPodcastCount();
            Map<String, Integer> podcastsByGenre = dashboardDao.getPodcastCountByGenre();
            Map<String, Integer> usersByFavoriteGenre = dashboardDao.getUserCountByFavoriteGenre();
            Map<String, String> recentPodcasts = dashboardDao.getRecentlyAddedPodcasts(5);
            
            // Set attributes for JSP
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalPodcasts", totalPodcasts);
            request.setAttribute("podcastsByGenre", podcastsByGenre);
            request.setAttribute("usersByFavoriteGenre", usersByFavoriteGenre);
            request.setAttribute("recentPodcasts", recentPodcasts);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/pages/admin/admin_dashboard.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            // Log the error
            e.printStackTrace();
            
            // Set error message
            request.setAttribute("errorMessage", "An error occurred while fetching dashboard data: " + e.getMessage());
            
            // Forward to error page
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }
}
