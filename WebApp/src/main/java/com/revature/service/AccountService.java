package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.dao.GenericDAO;
import com.revature.dao.GenericDaoImpl;
import com.revature.models.Account;
import com.revature.models.User;
import org.apache.log4j.*;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountService {
    private org.apache.log4j.Logger logger = Logger.getLogger(UserService.class);

    private GenericDAO dao;
    private ObjectMapper mapper;

    /**
     * A constructor that initializes the GenericDAO and ObjectMapper for service methods
     * As well as configure ConsoleAppender and FileAppender from log4j for logging
     *
     * @param dao Generic Dao Object
     * @param objectMapper ObjectMapper object
     */
    public AccountService(GenericDAO dao, ObjectMapper objectMapper) {
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
     * The service method that performs the truncate operation of the User table.
     * Service method directly call truncate method in the dao object and set the
     * status code to 200 OK.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void truncateAccount(HttpServletRequest req, HttpServletResponse resp) {
        dao.truncate(Account.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        logger.info("Successfully truncated the Account table!");
    }

    /**
     * The service method that retrieve an account from the database by id.
     * Receives a GET request from the servlet layer and get the parameter
     * "accountId" and create a new Account object by calling the private getAccountById method.
     *
     * If account is not null then use the mapper object to create a string
     * in json format and print the string and set response status code to 200 OK.
     *
     * If account is null set status code to 404 NOT FOUND.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void getAccount(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("accountId"));
            Account a = getAccountById(id);
            if(a != null) {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(a);
                resp.getOutputStream().print(json);
                logger.info("Successfully retrieved an account: " + id);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("Account Not Found!");
            }

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * The service method that retrieve all accounts from the database by calling the private
     * method getAccounts() which performs the dao method that returns an Optional list of objects
     * and cast each individual objects to Account class and return that list to the public service method
     * to print in json format.
     *
     * @param req request from servlet
     * @param resp response from servlet
     */
    public void getAllAccounts(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getAccounts());
            resp.getOutputStream().print(json);
            resp.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully retrieved all accounts!");

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * The service method that insert an Account into the Account table in the database.
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
    public void insertAccount(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);

            Account a = mapper.readValue(builder.toString(), Account.class);
            int result = insert(a);
            if(result != 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                logger.info("Successfully inserted an account: " + a.getAccountId());
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                logger.warn("Failed to insert!");
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * The service method that updates an Account in the database by id.
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
    public void updateAccount(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader().lines().collect(Collectors.toList()).forEach(builder::append);

            Account a = mapper.readValue(builder.toString(), Account.class);
            boolean result = update(a);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                logger.info("Successfully updated an account: " + a.getAccountId());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("Failed to update!");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * The service method that delete an Account in the database by id.
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
    public void deleteAccount(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("accountId"));
        boolean result = delete(id);

        if(result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully deleted an account: " + id);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.warn("Failed to delete!");
        }
    }

    private boolean delete(int id) {
        return dao.delete(Account.class, id);
    }

    private boolean update(Account u) {
        return dao.update(Account.class, u);
    }

    private int insert(Account u) {
        return dao.insert(Account.class, u);
    }

    private List<Account> getAccounts() {
        Optional<List<Object>> daoResult = dao.selectAll(Account.class);
        if(daoResult.isPresent()) {
            ArrayList<Account> result = new ArrayList<>();
            for(Object obj : daoResult.get()) {
                result.add((Account) obj);
            }
            return result;
        }
        return new ArrayList<>();
    }

    private Account getAccountById(int id) {
        Optional<Object> daoResult = dao.select(Account.class, id);
        if (daoResult.isPresent()) {
            Account a = (Account) daoResult.get();
            return a;
        }
        return null;
    }
}
