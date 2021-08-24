package com.revature.servlet;

import com.revature.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {

    UserService service;

    public UserServlet(UserService userService) {
        this.service = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("userId")) {
            service.getUser(req, resp);
        } else {
            service.getAllUsers(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        service.insertUser(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        service.updateUser(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("userId")) {
            service.deleteUser(req, resp);
        } else {
            service.truncateUser(req, resp);
        }
    }
}