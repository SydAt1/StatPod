package com.statpod.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.statpod.dao.PodcastDao;
import com.statpod.model.GenreModel;
import com.statpod.model.PodcastModel;
import com.statpod.service.GenreService;
import com.statpod.service.PodcastService;
import com.statpod.util.ImageUtil;
import com.statpod.util.SessionUtil;
import com.statpod.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/admin/editPodcast", "/admin/updatePodcast"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class EditPodcastController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ImageUtil imageUtil = new ImageUtil();
    private final PodcastDao podcastDao = new PodcastDao();
    private final PodcastService podcastService = new PodcastService();
    private final GenreService genreService = new GenreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/managePodcast.jsp");
            return;
        }

        try {
            int podcastId = Integer.parseInt(idStr);
            PodcastModel podcast = podcastDao.getPodcastById(podcastId);
            List<GenreModel> genres = genreService.getAllGenres();

            if (podcast != null) {
                req.setAttribute("podcast", podcast);
                req.setAttribute("genres", genres);
                req.getRequestDispatcher("/WEB-INF/pages/admin/editPodcast.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Podcast not found with ID: " + podcastId);
                req.getRequestDispatcher("/admin/managePodcast.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid Podcast ID format.");
            req.getRequestDispatcher("/admin/managePodcast.jsp").forward(req, resp);
        } catch (SQLException | ClassNotFoundException e) {
            req.setAttribute("error", "Database error occurred: " + e.getMessage());
            e.printStackTrace();
            req.getRequestDispatcher("/admin/managePodcast.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Admin check (keep this)
        if (!SessionUtil.isAdminUser(req)) {
            req.getRequestDispatcher("/WEB-INF/pages/not_authorized.jsp").forward(req, resp);
            return;
        }

        try {
            String podcastIdStr = req.getParameter("podcastId");
            if (ValidationUtil.isNullOrEmpty(podcastIdStr)) {
                handleError(req, resp, "Podcast ID is required.");
                return;
            }

            int podcastId;
            try {
                podcastId = Integer.parseInt(podcastIdStr);
            } catch (NumberFormatException e) {
                handleError(req, resp, "Invalid podcast ID format.");
                return;
            }

            String validationMessage = validatePodcastForm(req);
            if (validationMessage != null) {
                handleError(req, resp, validationMessage, podcastId);
                return;
            }

            PodcastModel podcastModel = extractPodcastModel(req);
            podcastModel.setPodcastId(podcastId);

            String currentImage = req.getParameter("currentImage");
            Part imagePart = req.getPart("podcastImage");

            if (imagePart != null && imagePart.getSize() > 0) {
                if (imageUtil.uploadImage(imagePart, req.getServletContext().getRealPath("/"), "podcasts")) {
                    podcastModel.setPodImg(imageUtil.getImageNameFromPart(imagePart));
                } else {
                    handleError(req, resp, "Could not upload the podcast image. Please try again later!", podcastId);
                    return;
                }
            } else {
                podcastModel.setPodImg(currentImage);
            }

            podcastDao.updatePodcast(podcastModel);

            // Set a success message attribute
            req.setAttribute("success", "Podcast successfully updated!");

            // Re-fetch the podcast details to display the updated information
            PodcastModel updatedPodcast = podcastDao.getPodcastById(podcastId);
            req.setAttribute("podcast", updatedPodcast);

            // Re-fetch the genres to keep the dropdown consistent
            List<GenreModel> genres = genreService.getAllGenres();
            req.setAttribute("genres", genres);

            // Forward back to the editPodcast.jsp page
            req.getRequestDispatcher("/WEB-INF/pages/admin/editPodcast.jsp").forward(req, resp);

        } catch (SQLException | ClassNotFoundException e) {
            handleError(req, resp, "Database error during update: " + e.getMessage(), Integer.parseInt(req.getParameter("podcastId")));
            e.printStackTrace();
        } catch (Exception e) {
            handleError(req, resp, "Unexpected error: " + e.getMessage(), Integer.parseInt(req.getParameter("podcastId")));
            e.printStackTrace();
        }
    }

    private String validatePodcastForm(HttpServletRequest req) throws IOException, ServletException {
        String podcastName = req.getParameter("podcastName");
        String hostName = req.getParameter("hostName");
        String genre = req.getParameter("genre");
        String description = req.getParameter("description");

        if (ValidationUtil.isNullOrEmpty(podcastName)) return "Podcast name is required.";
        if (ValidationUtil.isNullOrEmpty(hostName)) return "Host name is required.";
        if (ValidationUtil.isNullOrEmpty(description)) return "Description is required.";

        Part image = req.getPart("podcastImage");
        if (image != null && image.getSize() > 0 && !ValidationUtil.isValidImageExtension(image)) {
            return "Invalid image format. Only jpg, jpeg, png, and gif are allowed.";
        }

        if (genre != null && !genre.isEmpty()) {
            try {
                int genreId = Integer.parseInt(genre);
                // Use the DAO directly for checking existence if needed, or the service
                // if that's the intended layer for such logic.
                // Assuming PodcastService.genreExists is still valid.
                if (!podcastService.genreExists(genreId)) {
                    return "Selected genre does not exist.";
                }
            } catch (NumberFormatException e) {
                return "Invalid genre selection.";
            }
        }

        return null;
    }

    private PodcastModel extractPodcastModel(HttpServletRequest req) throws Exception {
        String podcastName = req.getParameter("podcastName");
        String hostName = req.getParameter("hostName");
        String genreStr = req.getParameter("genre");
        String description = req.getParameter("description");
        String dateStr = req.getParameter("date");

        Integer genre = null;
        if (genreStr != null && !genreStr.isEmpty()) {
            try {
                genre = Integer.parseInt(genreStr);
            } catch (NumberFormatException ignored) {}
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date releaseDate;
        try {
            releaseDate = dateFormat.parse(dateStr);
        } catch (Exception e) {
            SimpleDateFormat altDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            releaseDate = altDateFormat.parse(dateStr);
        }

        if (description == null) {
            description = "";
        }

        return new PodcastModel(podcastName, hostName, releaseDate, genre, null, description);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("podcastName", req.getParameter("podcastName"));
        req.setAttribute("hostName", req.getParameter("hostName"));
        req.setAttribute("genre", req.getParameter("genre"));
        req.setAttribute("description", req.getParameter("description"));
        req.getRequestDispatcher("/admin/editPodcast.jsp").forward(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message, int podcastId)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("podcastName", req.getParameter("podcastName"));
        req.setAttribute("hostName", req.getParameter("hostName"));
        req.setAttribute("genre", req.getParameter("genre"));
        req.setAttribute("description", req.getParameter("description"));
        resp.sendRedirect(req.getContextPath() + "/admin/editPodcast.jsp?id=" + podcastId);
    }
}