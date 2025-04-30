package com.statpod.controller;

import java.io.IOException;
import java.util.List;
import com.statpod.model.PodcastUserModel;
import com.statpod.service.RegisterService;
import com.statpod.model.GenreModel;
import com.statpod.service.GenreService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * RegisterController is a servlet that handles user registration for the StatPod application.
 * It provides functionality for displaying the registration form and processing 
 * the registration request by delegating business logic to the RegisterService.
 * 
 * This servlet is mapped to the URL pattern "/register" and supports multipart form data
 * for handling file uploads, specifically user profile images.
 */
@WebServlet(urlPatterns = {"/register"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final RegisterService registerService = new RegisterService();
    private final GenreService genreService = new GenreService();

    /**
     * Handles GET requests to display the registration form.
     * Retrieves a list of available podcast genres and sets it as a request attribute.
     * Forwards the request to the registration JSP page.
     *
     * @param req the HttpServletRequest object that contains the request the client made
     * @param resp the HttpServletResponse object that contains the response the servlet returns
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
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

    /**
     * Handles POST requests to process the registration form submission.
     * Delegates validation, user creation, and image handling to the RegisterService.
     * Handles success or error responses accordingly.
     *
     * @param req the HttpServletRequest object that contains the request the client made
     * @param resp the HttpServletResponse object that contains the response the servlet returns
     * @throws ServletException if the request for the POST could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Extract form parameters
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");
            String displayName = req.getParameter("displayName");
            String favoriteGenre = req.getParameter("favoriteGenre");
            Part image = req.getPart("imageUrl");
            
            // Validate the registration form
            String validationMessage = registerService.validateRegistrationForm(
                username, email, password, confirmPassword, image, favoriteGenre);
                
            if (validationMessage != null) {
                handleError(req, resp, validationMessage);
                return;
            }

            // Create user model
            PodcastUserModel userModel = registerService.createUserModel(
                username, email, displayName, favoriteGenre, password, image);
            
            // Handle image upload only if an image was provided
            if (image != null && image.getSize() > 0) {
                if (!registerService.uploadImage(image, req.getServletContext().getRealPath("/"))) {
                    handleError(req, resp, "Could not upload the image. Please try again later!");
                    return;
                }
            }

            // Add user to database
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

    /**
     * Handles successful registration by setting a success message and forwarding to the specified page.
     * 
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @param message the success message to display
     * @param redirectPage the page to forward to
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, String message, String redirectPage)
            throws ServletException, IOException {
        req.setAttribute("success", message);
        req.getRequestDispatcher(redirectPage).forward(req, resp);
    }

    /**
     * Handles registration errors by setting appropriate attributes and forwarding back to the register page.
     * 
     * @param req the HttpServletRequest object
     * @param resp the HttpServletResponse object
     * @param message the error message to display
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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