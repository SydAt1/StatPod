package com.statpod.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.statpod.config.DbConfig;
import com.statpod.util.SessionUtil;
import com.statpod.model.PodcastModel;
import com.statpod.dao.PodcastDao;

/**
 * HomeController - Handles home page requests and demonstrates database connectivity
 */
@WebServlet(urlPatterns = {"/home", "/"})
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests to the home page.
     * Adds user session information to request attributes if the user is logged in.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("✅ HomeController is running...");

        // Check if user is logged in and set request attributes accordingly
        if (SessionUtil.isLoggedIn(request)) {
            String currentUser = SessionUtil.getCurrentUser(request);
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("currentUser", currentUser);

            boolean isAdmin = SessionUtil.isAdminUser(request);
            request.setAttribute("isAdmin", isAdmin);

            System.out.println("✅ User is logged in: " + currentUser + " (Admin: " + isAdmin + ")");
        } else {
            request.setAttribute("isLoggedIn", false);
            System.out.println("ℹ️ User is not logged in");
        }

        checkDatabaseConnection(request);

        try {
            PodcastDao podcastDAO = new PodcastDao();

            List<PodcastModel> recommendedPodcasts = podcastDAO.getRecommendedPodcasts();
            List<PodcastModel> allPodcasts = podcastDAO.getAllPodcasts();
            List<String[]> allGenres = podcastDAO.getAllGenres();

            request.setAttribute("recommendedPodcasts", recommendedPodcasts);
            request.setAttribute("allPodcasts", allPodcasts);
            request.setAttribute("genres", allGenres);

        } catch (Exception e) {
            System.err.println("❌ Error fetching podcasts: " + e.getMessage());
        }

        // ✅ Forward only once — after all attributes are set
        request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
    }

    /**
     * Checks database connection and sets relevant request attributes.
     *
     * @param request HttpServletRequest to set attributes on
     */
    private void checkDatabaseConnection(HttpServletRequest request) {
        try (Connection connection = DbConfig.getDbConnection()) {
            System.out.println("✅ Successfully connected to database!");
            request.setAttribute("dbStatus", "Connected successfully to database: " + DbConfig.getDbName());

            // Optional: Perform a simple query to demonstrate functionality
            performSampleQuery(connection, request);

        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            request.setAttribute("dbStatus", "Connection failed: " + e.getMessage());
        }
    }

    /**
     * Performs a sample query to list database tables.
     *
     * @param connection Database connection
     * @param request    HttpServletRequest to set attributes on
     */
    private void performSampleQuery(Connection connection, HttpServletRequest request) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {

            StringBuilder tables = new StringBuilder();
            while (rs.next()) {
                tables.append(rs.getString(1)).append(", ");
            }

            if (tables.length() > 0) {
                tables.setLength(tables.length() - 2); // Remove last comma
                request.setAttribute("dbTables", "Database tables: " + tables.toString());
                System.out.println("Found tables: " + tables);
            } else {
                request.setAttribute("dbTables", "No tables found in database");
            }

        } catch (Exception e) {
            System.err.println("❌ Query failed: " + e.getMessage());
            request.setAttribute("dbTables", "Query failed: " + e.getMessage());
        }
    }
}