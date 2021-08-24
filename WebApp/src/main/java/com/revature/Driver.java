package com.revature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.GenericDaoImpl;
import com.revature.service.AccountService;
import com.revature.service.UserService;
import com.revature.servlet.AccountServlet;
import com.revature.servlet.UserServlet;
import com.revature.util.ConnectionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
/*
TODO:
    1. Finish create for ORM
    2. Fix log to file for tomcat
    3. Better Mockito tests
    4. Select record with other parameters
 */
@WebListener
public class Driver implements ServletContextListener {

    /**
     * The init method for servlet
     * @param sce ServletContextEvent Object
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // create the dao object
        GenericDaoImpl dao = new GenericDaoImpl();

        // set up database connection
        ConnectionFactory cf = new ConnectionFactory("jdbc:postgresql:" +
                "//database-1.cz4piyzhcf32.us-east-2.rds.amazonaws.com/postgres?currentSchema=mockdb",
                "jinlin", "jinlin226");

        dao.setConnection(cf.getConnection());

        // create an ObjectMapper object
        ObjectMapper mapper = new ObjectMapper();

        // create UserService
        UserService userService = new UserService(dao, mapper);
        // create AccountService
        AccountService accountService = new AccountService(dao, mapper);

        // Add servlet to the context and set url pattern
        ServletContext context = sce.getServletContext();
        context.addServlet("userServlet", new UserServlet(userService)).addMapping("/users");
        context.addServlet("accountServlet", new AccountServlet(accountService)).addMapping("/accounts");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}