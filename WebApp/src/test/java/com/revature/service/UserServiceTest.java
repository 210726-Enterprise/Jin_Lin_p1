package com.revature.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.dao.GenericDAO;
import com.revature.models.User;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserService service;
    GenericDAO daoMock;
    ObjectMapper mapperMock;
    ObjectWriter writerMock;
    HttpServletRequest requestMock;
    HttpServletResponse responseMock;
    List<Object> usersList;
    ServletOutputStream outputStreamMock;
    String json;

    @Before
    public void setUp() {
        daoMock = Mockito.mock(GenericDAO.class);
        mapperMock = Mockito.mock(ObjectMapper.class);
        writerMock = Mockito.mock(ObjectWriter.class);
        requestMock = Mockito.mock(HttpServletRequest.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
        outputStreamMock = Mockito.mock(ServletOutputStream.class);

        service = new UserService(daoMock, mapperMock);

        // Empty the list before each
        usersList = new ArrayList<>();
        json = "test json";

    }

    @Test
    public void getUserByIdTest() throws IOException {
        String id = "149";
        User u = new User(123, "Mock", "Test");

        when(requestMock.getParameter("userId")).thenReturn(id);
        when(daoMock.select(User.class, u)).thenReturn(Optional.of(u));
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(u)).thenReturn(json);
        when(responseMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getUser(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void truncateUserTableTest() {
        when(daoMock.truncate(User.class)).thenReturn(true);
        service.truncateUser(requestMock, responseMock);
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    public void getAllUsersTest() throws IOException {
        usersList.add(new User());
        Mockito.when(daoMock.selectAll(User.class)).thenReturn(Optional.of(usersList));
        Mockito.when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        Mockito.when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(usersList)).thenReturn(json);
        Mockito.when(responseMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getAllUsers(requestMock, responseMock);

        // verify acts as an assertion confirming that the response code was added successfully
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(outputStreamMock).print(json);
    }

    @Test
    public void insertUserTest() throws IOException {
        User u = new User();
        String s = "test";
        BufferedReader buffer = mock(BufferedReader.class);
        when(requestMock.getReader()).thenReturn(buffer);
        when(mapperMock.readValue(s, User.class)).thenReturn(u);
        when(daoMock.insert(User.class, u)).thenReturn(1);

        service.insertUser(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void updateUserTest() throws IOException {
        User u = new User();
        String s = "test";
        BufferedReader buffer = mock(BufferedReader.class);
        when(requestMock.getReader()).thenReturn(buffer);
        when(mapperMock.readValue(s, User.class)).thenReturn(u);
        when(daoMock.update(User.class, u)).thenReturn(true);

        service.updateUser(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void deleteUserTest() {
        String id = "149";
        when(requestMock.getParameter("userId")).thenReturn(id);
        when(daoMock.delete(User.class, id)).thenReturn(true);

        service.deleteUser(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}