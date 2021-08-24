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

    /**
     * The servlet method that performs GET operation.
     * Receives a request from the endpoint.
     *
     * Check if the request have a parameter "userId" then
     * call the getUser from service to get single user.
     *
     * If no parameter "userId" was found then call the getAllUsers
     * from service to get all users.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("userId")) {
            service.getUser(req, resp);
        } else {
            service.getAllUsers(req, resp);
        }
    }

    /**
     * The servlet method that performs POST operation.
     * Receives a request from the endpoint and call insertUser
     * from service.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        service.insertUser(req, resp);
    }

    /**
     * The servlet method that performs PUT operation.
     * Receives a request from the endpoint and call updateUser
     * from service.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        service.updateUser(req, resp);
    }

    /**
     * The servlet method that performs DELETE operation.
     * Receives a request from the endpoint.
     *
     * Check if the request have a parameter "userId" then
     * call the deleteUser from service to delete a single User.
     *
     * If no parameter "userId" was found then call the truncateUser
     * from service to truncate the table in database.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("userId")) {
            service.deleteUser(req, resp);
        } else {
            service.truncateUser(req, resp);
        }
    }
}