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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("accountId")) {
            service.getAccount(req, resp);
        } else {
            service.getAllAccounts(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        service.insertAccount(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        service.updateAccount(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameterMap().containsKey("accountId")) {
            service.deleteAccount(req, resp);
        } else {
            service.truncateAccount(req, resp);
        }
    }
}
