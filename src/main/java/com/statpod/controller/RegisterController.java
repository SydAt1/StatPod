package com.statpod.controller;

import java.io.IOException;
import java.util.List;
import com.statpod.model.PodcastUserModel;
import com.statpod.service.RegisterService;
import com.statpod.model.GenreModel;
import com.statpod.service.GenreService;
import com.statpod.util.ImageUtil;
import com.statpod.util.PasswordUtil;
import com.statpod.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(urlPatterns = {"/register"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ImageUtil imageUtil = new ImageUtil();
    private final RegisterService registerService = new RegisterService();
    private final GenreService genreService = new GenreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<GenreModel> genres = genreService.getAllGenres(); 
            req.setAttribute("genres", genres);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String validationMessage = validateRegistrationForm(req);
            if (validationMessage != null) {
                handleError(req, resp, validationMessage);
                return;
            }

            PodcastUserModel userModel = extractUserModel(req);
            
            // Handle image upload only if an image was provided
            Part image = req.getPart("imageUrl");
            if (image != null && image.getSize() > 0) {
                if (!uploadImage(req)) {
                    handleError(req, resp, "Could not upload the image. Please try again later!");
                    return;
                }
            }

            Boolean isAdded = registerService.addUser(userModel);
            
            if (isAdded == null) {
                handleError(req, resp, "Our server is under maintenance. Please try again later!");
            } else if (isAdded) {
                handleSuccess(req, resp, "Your account is successfully created!", "/WEB-INF/pages/login.jsp");
            } else {
                handleError(req, resp, "Could not register your account. Please try again later!");
            }
        } catch (Exception e) {
            handleError(req, resp, "An unexpected error occurred. Please try again later!");
            e.printStackTrace();
        }
    }

    private String validateRegistrationForm(HttpServletRequest req) throws IOException, ServletException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String displayName = req.getParameter("displayName");
        String favoriteGenre = req.getParameter("favoriteGenre");

        if (ValidationUtil.isNullOrEmpty(username)) return "Username is required.";
        if (ValidationUtil.isNullOrEmpty(email)) return "Email is required.";
        if (ValidationUtil.isNullOrEmpty(password)) return "Password is required.";
        if (ValidationUtil.isNullOrEmpty(confirmPassword)) return "Please confirm your password.";
        if (!ValidationUtil.isValidEmail(email)) return "Invalid email format.";
        if (!ValidationUtil.isValidPassword(password)) return "Password must be at least 8 characters long, with 1 uppercase letter, 1 number, and 1 symbol.";
        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) return "Passwords do not match.";

        // Only validate image if one was uploaded
        Part image = req.getPart("imageUrl");
        if (image != null && image.getSize() > 0 && !ValidationUtil.isValidImageExtension(image)) {
            return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
        }

        // Validate genre ID if provided
        if (favoriteGenre != null && !favoriteGenre.isEmpty()) {
            try {
                int genreId = Integer.parseInt(favoriteGenre);
                if (!registerService.genreExists(genreId)) {
                    return "Selected genre does not exist.";
                }
            } catch (NumberFormatException e) {
                return "Invalid genre selection.";
            }
        }

        return null;
    }

    private PodcastUserModel extractUserModel(HttpServletRequest req) throws Exception {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String displayName = req.getParameter("displayName");
        String favoriteGenreStr = req.getParameter("favoriteGenre");
        String password = PasswordUtil.encrypt(username, req.getParameter("password"));
        
        String imageUrl = null;
        Part image = req.getPart("imageUrl");
        if (image != null && image.getSize() > 0) {
            imageUrl = imageUtil.getImageNameFromPart(image);
        }

        // Convert favoriteGenre from String to Integer
        Integer favoriteGenre = null;
        if (favoriteGenreStr != null && !favoriteGenreStr.isEmpty()) {
            try {
                favoriteGenre = Integer.parseInt(favoriteGenreStr);
            } catch (NumberFormatException e) {
                // Invalid genre ID format, leave as null
            }
        }

        return new PodcastUserModel(username, password, email, displayName, favoriteGenre, imageUrl);
    }

    private boolean uploadImage(HttpServletRequest req) throws IOException, ServletException {
        Part image = req.getPart("imageUrl");
        if (image == null || image.getSize() == 0) {
            return true; // No image to upload is not an error
        }
        return imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "users");
    }

    private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, String message, String redirectPage)
            throws ServletException, IOException {
        req.setAttribute("success", message);
        req.getRequestDispatcher(redirectPage).forward(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("username", req.getParameter("username"));
        req.setAttribute("email", req.getParameter("email"));
        req.setAttribute("displayName", req.getParameter("displayName"));
        req.setAttribute("favoriteGenre", req.getParameter("favoriteGenre"));
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }
}