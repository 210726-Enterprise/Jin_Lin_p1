package com.revature.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;

public class AccountServiceTest {

    private AccountService accountService;
    private String body;

    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;


    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
    }
    // Test run order causing failures when trying to access data after truncate
    @Test
    public void truncateAccount() {
        accountService.truncateAccount(mockRequest, mockResponse);
        Assertions.assertEquals(200, mockResponse.getStatus());
    }

    @Test
    public void getAccountByIdTest() {
        mockRequest.setParameter("accountId", "67");
        Assertions.assertEquals(200, mockResponse.getStatus());
    }

    @Test
    public void getAllAccountsTest() {
        accountService.getAllAccounts(mockRequest, mockResponse);
        Assertions.assertEquals(200, mockResponse.getStatus());
    }

    @Test
    public void insertAccountTest() {

        body = "{\n" +
                "  \"accountId\" : 123456,\n" +
                "  \"accountType\" : \"saving\",\n" +
                "  \"username\" : \"Mock\",\n" +
                "  \"password\" : \"Test\",\n" +
                "  \"balance\" : 123.0\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        accountService.insertAccount(mockRequest, mockResponse);
        Assertions.assertEquals(201, mockResponse.getStatus());

        body = "{\n" +
                "  \"accountId\" : 0,\n" +
                "  \"accountType\" : \"saving\",\n" +
                "  \"username\" : \"Mock\",\n" +
                "  \"password\" : \"Test\",\n" +
                "  \"balance\" : 123.0\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        accountService.insertAccount(mockRequest, mockResponse);
        Assertions.assertEquals(201, mockResponse.getStatus());

        body = "{\n" +
                "  \"accountId\" : 123456,\n" +
                "  \"accountType\" : \"saving\",\n" +
                "  \"username\" : \"Mock\",\n" +
                "  \"password\" : \"Test\",\n" +
                "  \"balance\" : 123.0\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        accountService.insertAccount(mockRequest, mockResponse);
        Assertions.assertEquals(400, mockResponse.getStatus());
    }

    @Test
    public void updateAccountTest() {
        body = "{\n" +
                "  \"accountId\" : 123456,\n" +
                "  \"accountType\" : \"saving\",\n" +
                "  \"username\" : \"Mock\",\n" +
                "  \"password\" : \"UpdateTest\",\n" +
                "  \"balance\" : 123.0\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        accountService.updateAccount(mockRequest, mockResponse);
        Assertions.assertEquals(202, mockResponse.getStatus());

        body = "{\n" +
                "  \"accountId\" : 54870,\n" +
                "  \"accountType\" : \"saving\",\n" +
                "  \"username\" : \"Mock\",\n" +
                "  \"password\" : \"UpdateTest\",\n" +
                "  \"balance\" : 123.0\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        accountService.updateAccount(mockRequest, mockResponse);
        Assertions.assertEquals(409, mockResponse.getStatus());
    }

    @Test
    public void deleteAccountTest() {
        mockRequest.setParameter("accountId", "123456");
        accountService.deleteAccount(mockRequest, mockResponse);
        Assertions.assertEquals(400, mockResponse.getStatus());

        mockRequest.setParameter("accountId", "7854");
        accountService.deleteAccount(mockRequest, mockResponse);
        Assertions.assertEquals(400, mockResponse.getStatus());
    }
}