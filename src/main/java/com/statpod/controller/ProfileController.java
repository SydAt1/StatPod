package com.statpod.controller;

import com.statpod.config.DbConfig;
import com.statpod.dao.GenreDao;
import com.statpod.dao.UserDao;
import com.statpod.model.GenreModel;
import com.statpod.model.PodcastUserModel;
import com.statpod.util.ImageUtil;
import com.statpod.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/profile"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class ProfileController extends HttpServlet {

    private final ImageUtil imageUtil = new ImageUtil();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = SessionUtil.getCurrentUser(request);

        try (Connection conn = DbConfig.getDbConnection()) {
            UserDao userDao = new UserDao(conn); 
            GenreDao genreDao = new GenreDao(conn);

            PodcastUserModel user = userDao.fetchUserByUsername(username);
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/error");
                return;
            }

            GenreModel favoriteGenre = genreDao.getGenreById(user.getFavoriteGenre());

            request.setAttribute("user", user);
            request.setAttribute("favoriteGenre", favoriteGenre);
            request.getRequestDispatcher("/WEB-INF/pages/profile.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = SessionUtil.getCurrentUser(request);
        String displayName = request.getParameter("displayName");
        String email = request.getParameter("email");
        String favoriteGenreName = request.getParameter("favoriteGenre");

        try (Connection conn = DbConfig.getDbConnection()) {
            UserDao userDao = new UserDao(conn);
            GenreDao genreDao = new GenreDao(conn);

            PodcastUserModel existingUser = userDao.fetchUserByUsername(username);
            if (existingUser == null) {
                response.sendRedirect(request.getContextPath() + "/error");
                return;
            }

            int genreId = genreDao.fetchGenreIdByName(favoriteGenreName);
            if (genreId == -1) {
                genreId = existingUser.getFavoriteGenre(); // fallback to old genre
            }

            Part imagePart = request.getPart("imageUrl");
            String imageUrl = existingUser.getImageUrl(); // Default to existing image

            if (imagePart != null && imagePart.getSize() > 0) {
                // Uploading a new image
                boolean uploadSuccess = imageUtil.uploadImage(
                    imagePart,
                    request.getServletContext().getRealPath("/"), // ROOT PATH
                    "users" // FOLDER inside webapp
                );

                if (uploadSuccess) {
                    imageUrl = imageUtil.getImageNameFromPart(imagePart);
                }
            }

            // Update user
            userDao.updateUserProfile(username, displayName, email, genreId, imageUrl);

            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
