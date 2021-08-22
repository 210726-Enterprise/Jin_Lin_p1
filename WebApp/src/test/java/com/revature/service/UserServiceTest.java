package com.revature.service;

import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UserServiceTest {

    private UserService userService;
    private String body;

    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;

    @Before
    public void setUp() throws Exception {
        userService = new UserService();
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();

    }

    @Test
    public void getUserByIdTest() throws IOException {
        mockRequest.setParameter("userId", "135");
        userService.getUser(mockRequest, mockResponse);
        Assertions.assertEquals(200, mockResponse.getStatus());

        mockRequest.setParameter("userId", "19845");
        userService.getUser(mockRequest, mockResponse);
        Assertions.assertEquals(404, mockResponse.getStatus());
    }

//    @Test
//    public void truncateUserTableTest() {
//        userService.truncateUser(mockRequest, mockResponse);
//        Assertions.assertEquals(200, mockResponse.getStatus());
//    }

    @Test
    public void getAllUsersTest() {
        userService.getAllUsers(mockRequest, mockResponse);
        Assertions.assertEquals(200, mockResponse.getStatus());
    }

    @Test
    public void insertUserTest() {
        body = "{\n" +
                "  \"userId\" : 117,\n" +
                "  \"firstName\" : \"Insert\",\n" +
                "  \"lastName\" : \"Test\"\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        userService.insertUser(mockRequest, mockResponse);
        Assertions.assertEquals(201, mockResponse.getStatus());

        body = "{\n" +
                "  \"userId\" : 0,\n" +
                "  \"firstName\" : \"Insert\",\n" +
                "  \"lastName\" : \"Test\"\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        userService.insertUser(mockRequest, mockResponse);
        Assertions.assertEquals(201, mockResponse.getStatus());

        body = "{\n" +
                "  \"userId\" : 1350,\n" +
                "  \"firstName\" : \"Insert\",\n" +
                "  \"lastName\" : \"Test\"\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        userService.insertUser(mockRequest, mockResponse);
        Assertions.assertEquals(201, mockResponse.getStatus());

        body = "{\n" +
                "  \"userId\" : 1350,\n" +
                "  \"firstName\" : \"Insert\",\n" +
                "  \"lastName\" : \"Test\"\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        userService.insertUser(mockRequest, mockResponse);
        Assertions.assertEquals(400, mockResponse.getStatus());

    }

    @Test
    public void updateUserTest() {
        body = "{\n" +
                "  \"userId\" : 135,\n" +
                "  \"firstName\" : \"Update\",\n" +
                "  \"lastName\" : \"Test\"\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        userService.updateUser(mockRequest, mockResponse);
        Assertions.assertEquals(202, mockResponse.getStatus());

        body = "{\n" +
                "  \"userId\" : 11501,\n" +
                "  \"firstName\" : \"Update\",\n" +
                "  \"lastName\" : \"Test\"\n" +
                "}";

        mockRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        userService.updateUser(mockRequest, mockResponse);
        Assertions.assertEquals(409, mockResponse.getStatus());
    }

    @Test
    public void deleteUserTest() {
        mockRequest.setParameter("userId", "117");
        userService.deleteUser(mockRequest, mockResponse);
        Assertions.assertEquals(200, mockResponse.getStatus());

        mockRequest.setParameter("userId", "1350");
        userService.deleteUser(mockRequest, mockResponse);
        Assertions.assertEquals(200, mockResponse.getStatus());

        mockRequest.setParameter("userId", "111");
        userService.deleteUser(mockRequest, mockResponse);
        Assertions.assertEquals(400, mockResponse.getStatus());
    }
}