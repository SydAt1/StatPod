package com.statpod.controller;

import java.io.IOException;
import com.statpod.model.PodcastUserModel;
import com.statpod.service.LoginService;
import com.statpod.util.CookieUtil;
import com.statpod.util.SessionUtil;
import com.statpod.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(request)) {
            redirectBasedOnRole(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            redirectBasedOnRole(req, resp);
            return;
        }
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if (validateInput(req, resp, username, password)) {
            return;
        }
        
        PodcastUserModel podcastUser = new PodcastUserModel(username, password);
        Boolean loginStatus = loginService.loginUser(podcastUser);
        
        if (loginStatus != null && loginStatus) {
            handleSuccessfulLogin(req, resp, username);
        } else {
            handleLoginFailure(req, resp, loginStatus);
        }
        
        System.out.println("Login attempt for username: " + username);
    }

    private boolean validateInput(HttpServletRequest req, HttpServletResponse resp, String username, String password) 
            throws ServletException, IOException {
        boolean hasValidationErrors = false;
        
        if (ValidationUtil.isNullOrEmpty(username)) {
            req.setAttribute("usernameError", "Username cannot be empty");
            hasValidationErrors = true;
        } else if (!ValidationUtil.isValidUsername(username)) {
            req.setAttribute("usernameError", "Username must be 4-20 characters with only letters, numbers, and underscores");
            hasValidationErrors = true;
        }
        
        if (ValidationUtil.isNullOrEmpty(password)) {
            req.setAttribute("passwordError", "Password cannot be empty");
            hasValidationErrors = true;
        } else if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("passwordError", "Password must be at least 8 characters with 1 uppercase letter, 1 number, and 1 special character");
            hasValidationErrors = true;
        }
        
        if (hasValidationErrors) {
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
            return true;
        }
        return false;
    }

    private void handleSuccessfulLogin(HttpServletRequest req, HttpServletResponse resp, String username) throws IOException {
        SessionUtil.setAttribute(req, "username", username);
        SessionUtil.setAttribute(req, "isLoggedIn", true);

        boolean isAdmin = "admin".equals(username); // Determine if the user is admin
        SessionUtil.setAttribute(req, "isAdmin", isAdmin); // Set the isAdmin attribute in the session

        HttpSession session = req.getSession();
        System.out.println("✅ After login:");
        System.out.println("Session ID: " + session.getId());
        System.out.println("isLoggedIn: " + session.getAttribute("isLoggedIn"));
        System.out.println("username: " + session.getAttribute("username"));
        System.out.println("isAdmin: " + session.getAttribute("isAdmin")); // Debugging

        SessionUtil.setSessionTimeout(req, 1800);

        if (isAdmin) {
            CookieUtil.addCookie(resp, "role", "admin", 5 * 30);
        } else {
            CookieUtil.addCookie(resp, "role", "user", 5 * 30);
        }
        resp.sendRedirect(req.getContextPath() + "/"); // Redirect to home page
    }

    private void redirectBasedOnRole(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = SessionUtil.getCurrentUser(req);
        if ("admin".equals(username)) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/"); // Redirect to home page
        }
    }

    private void handleLoginFailure(HttpServletRequest req, HttpServletResponse resp, Boolean loginStatus)
            throws ServletException, IOException {
        String errorMessage = (loginStatus == null) 
            ? "Our server is currently unavailable. Please try again later!"
            : "Invalid username or password. Please try again!";
        req.setAttribute("error", errorMessage);
        req.setAttribute("username", req.getParameter("username"));
        req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req, resp);
    }
}