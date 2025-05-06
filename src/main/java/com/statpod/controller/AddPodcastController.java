package com.statpod.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.statpod.model.GenreModel;
import com.statpod.model.PodcastModel;
import com.statpod.service.GenreService;
import com.statpod.dao.PodcastDao;
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

@WebServlet(urlPatterns = {"/admin/new_podcast"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class AddPodcastController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ImageUtil imageUtil = new ImageUtil();
    private final PodcastDao podcastService = new PodcastDao();
    private final GenreService genreService = new GenreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {    	
        try {
            List<GenreModel> genres = genreService.getAllGenres();
            req.setAttribute("genres", genres);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/pages/admin/new_podcast.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ✅ Admin check
    	if (!SessionUtil.isAdminUser(req)) {
            req.getRequestDispatcher("/WEB-INF/pages/common/not_authorized.jsp").forward(req, resp);
            return;
        }

        try {
            String validationMessage = validatePodcastForm(req);
            if (validationMessage != null) {
                handleError(req, resp, validationMessage);
                return;
            }

            PodcastModel podcastModel = extractPodcastModel(req);

            // Handle image upload and set imageUrl in podcastModel
            Part image = req.getPart("podcastImage");
            String imageUrl = null;
            if (image != null && image.getSize() > 0) {
                if (imageUtil.uploadImage(image, req.getServletContext().getRealPath("/"), "podcasts")) {
                    imageUrl = imageUtil.getImageNameFromPart(image);
                } else {
                    handleError(req, resp, "Could not upload the podcast image. Please try again later!");
                    return;
                }
            }

            podcastModel.setPodImg(imageUrl);
            Boolean isAdded = podcastService.addPodcast(podcastModel);

            if (isAdded == null) {
                handleError(req, resp, "Our server is under maintenance. Please try again later!");
            } else if (isAdded) {
                handleSuccess(req, resp, "Podcast successfully added!", "/WEB-INF/pages/home.jsp");
            } else {
                handleError(req, resp, "Could not add the podcast. Please try again later!");
            }
        } catch (Exception e) {
            handleError(req, resp, "An unexpected error occurred. Please try again later!");
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
            } catch (NumberFormatException e) {
                // Handle invalid genre format (optional)
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date releaseDate = dateFormat.parse(dateStr);

        // Handle null description
        if (description == null) {
            description = ""; // Or any default value
        }

        return new PodcastModel(podcastName, hostName, releaseDate, genre, null, description); // imageUrl will be set later
    }

    private void handleSuccess(HttpServletRequest req, HttpServletResponse resp, String message, String redirectPage)
            throws ServletException, IOException {
        req.setAttribute("success", message);
        req.getRequestDispatcher(redirectPage).forward(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("podcastName", req.getParameter("podcastName"));
        req.setAttribute("hostName", req.getParameter("hostName"));
        req.setAttribute("genre", req.getParameter("genre"));
        req.setAttribute("description", req.getParameter("description"));
        req.getRequestDispatcher("/WEB-INF/pages/admin/new_podcast.jsp").forward(req, resp);
    }
}