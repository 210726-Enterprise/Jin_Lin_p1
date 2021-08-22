package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.dao.GenericDAO;
import com.revature.dao.GenericDaoImpl;
import com.revature.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private GenericDAO dao;
    private ObjectMapper mapper;

    public UserService() {
        dao = new GenericDaoImpl();
        mapper = new ObjectMapper();
    }
    public void getUser(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User u = getUserById(Integer.parseInt(req.getParameter("userId")));
            if(u != null) {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(u);
                resp.getOutputStream().print(json);
                logger.info("Successfully retrieved an User!");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("User Not Found!");
            }

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public void truncateUser(HttpServletRequest req, HttpServletResponse resp) {
        dao.truncate(User.class);
        logger.info("Successfully truncated the User table!");
    }

    public void getAllUsers(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getUsers());
            resp.getOutputStream().print(json);
            logger.info("Successfully retrieved all users!");

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

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

    public void updateUser(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);

            User u = mapper.readValue(builder.toString(), User.class);
            boolean result = update(u);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                logger.info("Successfully update an User: " + u.getUserId());
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                logger.warn("Failed to update!");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        int toDelete = Integer.parseInt(req.getParameter("userId"));
        boolean result = delete(toDelete);

        if(result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully delete an User: " + toDelete);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
