package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.revature.dao.GenericDAO;
import com.revature.dao.GenericDaoImpl;
import com.revature.models.Account;
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

public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private GenericDAO dao;
    private ObjectMapper mapper;

    public AccountService() {
        dao = new GenericDaoImpl();
        mapper = new ObjectMapper();
    }

    public void truncateAccount(HttpServletRequest req, HttpServletResponse resp) {
        dao.truncate(Account.class);
        logger.info("Successfully truncated the Account table!");
    }

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

    public void getAllAccounts(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getAccounts());
            resp.getOutputStream().print(json);
            logger.info("Successfully retrieved all accounts!");

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

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
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                logger.warn("Failed to update!");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void deleteAccount(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("accountId"));
        boolean result = delete(id);

        if(result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            logger.info("Successfully deleted an account: " + id);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
