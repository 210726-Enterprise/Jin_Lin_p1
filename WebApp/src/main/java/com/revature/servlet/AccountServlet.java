package com.revature.servlet;

import com.revature.service.AccountService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountServlet extends HttpServlet {

    AccountService service;

    public AccountServlet(AccountService accountService) {
        this.service = accountService;
    }

    /**
     * The servlet method that performs GET operation.
     * Receives a request from the endpoint.
     *
     * Check if the request have a parameter "accountId" then
     * call the getAccount from service to get single account.
     *
     * If no parameter "accountId" was found then call the getAllAccounts
     * from service to get all accounts.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("accountId")) {
            service.getAccount(req, resp);
        } else {
            service.getAllAccounts(req, resp);
        }
    }

    /**
     * The servlet method that performs POST operation.
     * Receives a request from the endpoint and call insertAccount
     * from service.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        service.insertAccount(req, resp);
    }

    /**
     * The servlet method that performs PUT operation.
     * Receives a request from the endpoint and call updateAccount
     * from service.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        service.updateAccount(req, resp);
    }

    /**
     * The servlet method that performs DELETE operation.
     * Receives a request from the endpoint.
     *
     * Check if the request have a parameter "accountId" then
     * call the deleteAccount from service to delete a single account.
     *
     * If no parameter "accountId" was found then call the truncateAccount
     * from service to truncate the table in database.
     *
     * @param req HttpServletRequest Object
     * @param resp HttpServletResponse Object
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("accountId")) {
            service.deleteAccount(req, resp);
        } else {
            service.truncateAccount(req, resp);
        }
    }
}
