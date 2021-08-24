package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.GenericDAO;
import com.revature.models.User;
import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private Logger logger = Logger.getLogger(UserService.class);

    private GenericDAO dao;
    private ObjectMapper mapper;

    /**
     * A constructor that initializes the GenericDAO and ObjectMapper for service methods
     * As well as configure ConsoleAppender and FileAppender from log4j for logging
     *
     * @param dao Generic Dao Object
     * @param objectMapper ObjectMapper object
     */
    public UserService(GenericDAO dao, ObjectMapper objectMapper) {
        this.dao = dao;
        this.mapper = objectMapper;

        PatternLayout layout = new PatternLayout("%p : %d{yyyy-MM-dd HH:mm:ss} : %m%n");

        FileAppender fileAppender = new FileAppender();
        fileAppender.setThreshold(Level.ALL);
        fileAppender.setLayout(layout);
        fileAppender.setFile("src/main/logs/log.txt");
        fileAppender.activateOptions();

        Logger.getRootLogger().addAppender(fileAppender);
    }

    /**
     * The service method that retrieve an user from the database by id.
     * Receives a GET request from the servlet layer and get the parameter
     * "userId" and create a new User object by calling the private getUserById method.
     *
     * If user is not null then use the mapper object to create a string
     * in json format and print the string and set response status code to 200 OK.
     *
     * If user is null set status code to 404 NOT FOUND.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void getUser(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User u = getUserById(Integer.parseInt(req.getParameter("userId")));
            if(u != null) {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(u);
                resp.getOutputStream().print(json);
                resp.setStatus(HttpServletResponse.SC_OK);
                logger.info("Successfully retrieved an User!");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("User Not Found!");
            }

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * The service method that performs the truncate operation of the User table.
     * Service method directly call truncate method in the dao object and set the
     * status code to 200 OK.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void truncateUser(HttpServletRequest req, HttpServletResponse resp) {
        dao.truncate(User.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        logger.info("Successfully truncated the User table!");
    }

    /**
     * The service method that retrieve all users from the database by calling the private
     * method getUsers() which performs the dao method that returns an Optional list of objects
     * and cast each individual objects to User class and return that list to the public service method
     * to print in json format.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void getAllUsers(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getUsers());
            resp.getOutputStream().print(json);
            resp.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully retrieved all users!");

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * The service method that insert an User into the User table in the database.
     * String builder builds the string for mapper to read by appending the input from
     * request body.
     *
     * Call on the private insert method to perform the dao insert method and returns an
     * integer of queries operated which will be 1 or 0.
     *
     * Check if result is not 0 then set status code to 201 CREATED.
     * If equals 0 then no User is inserted, thus set status code to 400 BAD REQUEST.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void insertUser(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);
            User u = mapper.readValue(builder.toString(), User.class);
            int result = insert(u);
            if(result != 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                logger.info("Successfully insert an User: " + u.getUserId());
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.warn("Failed to insert!");
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * The service method that updates an User in the database by id.
     * String builder builds the string for mapper to read by appending the input from
     * request body.
     *
     * Call on the private update method to perform the dao update method and returns a
     * boolean of whether the query succeed or failed.
     *
     * Check if result is true then set status code to 200 OK.
     * If false then no User is updated, thus set status code to 404 NOT FOUND.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void updateUser(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);

            User u = mapper.readValue(builder.toString(), User.class);
            boolean result = update(u);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                logger.info("Successfully update an User: " + u.getUserId());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("Failed to update!");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * The service method that delete an User in the database by id.
     * Getting the userId by calling request getParameter method and parse the result
     * to int.
     *
     * Call on the private delete method to perform the dao delete method and returns
     * a boolean of whether query succeed or failed.
     *
     * Check if result is true then set status code to 200 OK.
     * If false then no User is updated, thus set status code to 404 NOT FOUND.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        int toDelete = Integer.parseInt(req.getParameter("userId"));
        boolean result = delete(toDelete);

        if(result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully delete an User: " + toDelete);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.warn("Failed to delete!");
        }
    }

    private boolean delete(int id) {
        return dao.delete(User.class, id);
    }

    private boolean update(User u) {
        return dao.update(User.class, u);
    }

    private int insert(User u) {
        return dao.insert(User.class, u);
    }

    private List<User> getUsers() {
        Optional<List<Object>> daoResult = dao.selectAll(User.class);
        if(daoResult.isPresent()) {
            ArrayList<User> result = new ArrayList<>();
            for(Object obj : daoResult.get()) {
                result.add((User) obj);
            }
            return result;
        }
        return new ArrayList<>();
    }

    private User getUserById(int id) {
        Optional<Object> daoResult = dao.select(User.class, id);
        if (daoResult.isPresent()) {
            User u = (User) daoResult.get();
            return u;
        }
        return null;
    }
}
