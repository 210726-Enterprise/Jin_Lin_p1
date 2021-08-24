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

@WebListener
public class Driver implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GenericDaoImpl dao = new GenericDaoImpl();

        ConnectionFactory cf = new ConnectionFactory("jdbc:postgresql:" +
                "//database-1.cz4piyzhcf32.us-east-2.rds.amazonaws.com/postgres?currentSchema=mockdb",
                "jinlin", "jinlin226");

        dao.setConnection(cf.getConnection());

        ObjectMapper mapper = new ObjectMapper();

        UserService userService = new UserService(dao, mapper);
        AccountService accountService = new AccountService(dao, mapper);

        ServletContext context = sce.getServletContext();
        context.addServlet("userServlet", new UserServlet(userService)).addMapping("/users");
        context.addServlet("accountServlet", new AccountServlet(accountService)).addMapping("/accounts");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}