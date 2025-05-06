package com.statpod.filter;

import com.statpod.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = {"/*"})
public class AuthenticatorFilter implements Filter {

    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";
    private static final String HOME = "/home";
    private static final String ROOT = "/";
    private static final String NOT_AUTHORIZED_JSP = "/WEB-INF/pages/not_authorized.jsp";

    private Set<String> publicResources;
    private Set<String> loginRegisterPages;

    @Override
    public void init(FilterConfig filterConfig) {
        publicResources = new HashSet<>(Arrays.asList(HOME, ROOT, "/aboutus.jsp", "/contact.jsp", "/profile.jsp", "/podcast", "/manageusers", "/search-results"));
        loginRegisterPages = new HashSet<>(Arrays.asList(LOGIN, REGISTER));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

    	HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUri = uri.substring(contextPath.length());

        // Allow static resources
        if (relativeUri.matches(".*\\.(css|js|png|jpg|jpeg|gif|woff|woff2|ttf|svg)$")) {
            chain.doFilter(request, response);
            return;
        }

        boolean isLoggedIn = SessionUtil.isLoggedIn(request);
        boolean isAdmin = SessionUtil.isAdminUser(request);

        // Allow access to public resources
        if (publicResources.contains(relativeUri)) {
            chain.doFilter(request, response);
            return;
        }


        // Redirect logged-in users away from login/register
        if (loginRegisterPages.contains(relativeUri)) {
            if (isLoggedIn) {
                response.sendRedirect(contextPath + HOME);
            } else {
                chain.doFilter(request, response);
            }
            return;
        }

        // Block access to unauthenticated users
        if (!isLoggedIn) {
            response.sendRedirect(contextPath + LOGIN);
            return;
        }

        // Restrict /admin/* to admin users
        if (relativeUri.startsWith("/admin/")) {
            if (isAdmin) {
                chain.doFilter(request, response);
            } else {
                request.getRequestDispatcher(NOT_AUTHORIZED_JSP).forward(request, response);
            }
            return;
        }

        // All other requests allowed for logged-in users
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup, if necessary
    }
}
