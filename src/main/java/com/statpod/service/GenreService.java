package com.statpod.service;

import com.statpod.config.DbConfig;
import com.statpod.dao.GenreDao;
import com.statpod.model.GenreModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class for managing genres.
 */
public class GenreService {
    private GenreDao genreDao;

    /**
     * Default constructor initializes GenreDao with DB connection
     */
    public GenreService() {
        try {
            Connection connection = DbConfig.getDbConnection();
            this.genreDao = new GenreDao(connection);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for dependency injection
     * @param genreDao the GenreDao instance
     */
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    /**
     * Retrieves all genres from the database
     * @return List of GenreModel objects
     */
    public List<GenreModel> getAllGenres() {
        try {
            return genreDao.getAllGenres();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a genre by its ID
     * @param genreId the ID of the genre
     * @return GenreModel object if found, null otherwise
     */
    public GenreModel getGenreById(int genreId) {
        try {
            return genreDao.getGenreById(genreId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if a genre exists by ID
     * @param genreId the ID to check
     * @return true if exists, false otherwise
     */
    public boolean genreExists(int genreId) {
        return getGenreById(genreId) != null;
    }
}
