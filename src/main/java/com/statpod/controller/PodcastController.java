package com.statpod.controller;

import com.statpod.dao.PodcastDao;
import com.statpod.model.PodcastModel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

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
        System.out.println("Raw ID param: " + podcastIdParam);

        if (podcastIdParam != null) {
            try {
                int podcastId = Integer.parseInt(podcastIdParam);
                PodcastModel selectedPodcast = podcastDao.getPodcastById(podcastId);

                if (selectedPodcast != null) {
                    request.setAttribute("podcast", selectedPodcast);
                    System.out.println("About to forward...");
                    request.getRequestDispatcher("/WEB-INF/pages/podcast.jsp").forward(request, response);
                    return; // Add return here to ensure no further processing
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Podcast not found.");
                    return; // Add return here
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid podcast ID.");
                return; // Add return here
            } catch (Exception e) {
                throw new ServletException("Error retrieving podcast", e);
            }
        } else {
            response.sendRedirect("/WEB-INF/pages/home.jsp");
            return; // Add return here
        }
    }
}
