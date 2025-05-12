package com.statpod.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.statpod.model.PodcastModel;
import com.statpod.dao.PodcastDao;
import com.statpod.util.SessionUtil;

/**
 * SearchController - Handles search requests for podcasts
 */
@WebServlet("/search")
public class SearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Handles GET requests to the search page.
     * Processes the search query and returns matching podcasts.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("SearchController is running...");
        
        // Get search query parameter
        String query = request.getParameter("q");
        System.out.println("Search query: " + query);
        
        // Check if user is logged in and set request attributes accordingly
        if (SessionUtil.isLoggedIn(request)) {
            String currentUser = SessionUtil.getCurrentUser(request);
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("currentUser", currentUser);
            boolean isAdmin = SessionUtil.isAdminUser(request);
            request.setAttribute("isAdmin", isAdmin);
            System.out.println("User is logged in: " + currentUser + " (Admin: " + isAdmin + ")");
        } else {
            request.setAttribute("isLoggedIn", false);
            System.out.println("User is not logged in");
        }
        
        try {
            PodcastDao podcastDAO = new PodcastDao();
            
            if (query != null && !query.trim().isEmpty()) {
                // Search for podcasts matching the query
                List<PodcastModel> searchResults = podcastDAO.searchPodcasts(query);
                request.setAttribute("searchResults", searchResults);
                request.setAttribute("searchQuery", query);
                request.setAttribute("resultCount", searchResults.size());
                
                System.out.println("Found " + searchResults.size() + " results for: " + query);
            } else {
                // If no query, show all podcasts as a fallback
                List<PodcastModel> allPodcasts = podcastDAO.getAllPodcasts();
                request.setAttribute("searchResults", allPodcasts);
                request.setAttribute("searchQuery", "");
                request.setAttribute("resultCount", allPodcasts.size());
                
                System.out.println("No search query provided, showing all podcasts");
            }
            
            // Get all genres for filtering options
            List<String[]> allGenres = podcastDAO.getAllGenres();
            request.setAttribute("genres", allGenres);
            
        } catch (Exception e) {
            System.err.println("Error searching podcasts: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while searching: " + e.getMessage());
        }
        
        // Forward to the search results page
        request.getRequestDispatcher("/WEB-INF/pages/search-results.jsp").forward(request, response);
    }
}