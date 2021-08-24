package com.revature.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.dao.GenericDAO;
import com.revature.models.Account;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class AccountServiceTest {

    AccountService service;
    GenericDAO daoMock;
    ObjectMapper mapperMock;
    ObjectWriter writerMock;
    HttpServletRequest requestMock;
    HttpServletResponse responseMock;
    List<Object> accountsList;
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

        service = new AccountService(daoMock, mapperMock);

        // Empty the list before each
        accountsList = new ArrayList<>();
        json = "test json";

    }

    @Test
    public void getAccountByIdTest() throws IOException {
        String id = "149";
        Account u = new Account(123, "Mock", "Test", "Mockito", 456);

        when(requestMock.getParameter("accountId")).thenReturn(id);
        when(daoMock.select(Account.class, u)).thenReturn(Optional.of(u));
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(u)).thenReturn(json);
        when(responseMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void truncateAccountTableTest() {
        when(daoMock.truncate(Account.class)).thenReturn(true);
        service.truncateAccount(requestMock, responseMock);
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    public void getAllAccountsTest() throws IOException {
        accountsList.add(new Account());
        Mockito.when(daoMock.selectAll(Account.class)).thenReturn(Optional.of(accountsList));
        Mockito.when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        Mockito.when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(accountsList)).thenReturn(json);
        Mockito.when(responseMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getAllAccounts(requestMock, responseMock);

        // verify acts as an assertion confirming that the response code was added successfully
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(outputStreamMock).print(json);
    }

    @Test
    public void insertAccountTest() throws IOException {
        Account u = new Account();
        String s = "test";
        BufferedReader buffer = mock(BufferedReader.class);
        when(requestMock.getReader()).thenReturn(buffer);
        when(mapperMock.readValue(s, Account.class)).thenReturn(u);
        when(daoMock.insert(Account.class, u)).thenReturn(1);

        service.insertAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void updateAccountTest() throws IOException {
        Account u = new Account();
        String s = "test";
        BufferedReader buffer = mock(BufferedReader.class);
        when(requestMock.getReader()).thenReturn(buffer);
        when(mapperMock.readValue(s, Account.class)).thenReturn(u);
        when(daoMock.update(Account.class, u)).thenReturn(true);

        service.updateAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void deleteAccountTest() {
        String id = "149";
        when(requestMock.getParameter("accountId")).thenReturn(id);
        when(daoMock.delete(Account.class, id)).thenReturn(true);

        service.deleteAccount(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}