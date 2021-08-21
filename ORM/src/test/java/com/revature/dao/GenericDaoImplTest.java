package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import java.sql.Connection;
import java.util.List;

public class GenericDaoImplTest {

    GenericDAO dao;
    @Before
    public void setUp() {
        dao = new GenericDaoImpl();
    }

    @Test
    public void create() {
    }

    @Test
    public void insertUserTest() {
        User u = new User(0,"unit","test");
        User u2 = new User(465,"unit","test2");
        User u3 = new User(1237,"unit","test3");
        Assertions.assertEquals(1, dao.insert(u.getClass(), u));
        Assertions.assertEquals(1, dao.insert(u2.getClass(), u2));
        Assertions.assertEquals(1, dao.insert(u3.getClass(), u3));
    }
    @Test
    public void insertAccountTest() {
        Account a = new Account(0,"saving", "unit", "test", 123);
        Account a2 = new Account(30,"checking", "unit", "test2", 1213);
        Account a3 = new Account(45,"saving", "unit", "test3", 2123);
        Assertions.assertEquals(1, dao.insert(a.getClass(), a));
        Assertions.assertEquals(1, dao.insert(a2.getClass(), a2));
        Assertions.assertEquals(1, dao.insert(a3.getClass(), a3));
    }

    @Test
    public void selectUserByIdTest() {
        Assertions.assertTrue(dao.select(User.class, 66).isPresent());
    }

    @Test
    public void selectAccountByIdTest() {
        Assertions.assertTrue(dao.select(Account.class, 30).isPresent());
    }

    @Test
    public void selectAllAccountTest() {
        List account = dao.selectAll(Account.class).orElseThrow(RuntimeException::new);
        Assertions.assertNotEquals(0, account.size());
    }
    @Test
    public void selectAllUserTest() {
        List user = dao.selectAll(User.class).orElseThrow(RuntimeException::new);
        Assertions.assertNotEquals(0, user.size());
    }

    @Test
    public void updateUserTest() {
        User u2 = new User(465,"Update","test2");
        User u3 = new User(1237,"Update","test3");
        dao.update(User.class, u2);
        dao.update(User.class, u3);
    }
    @Test
    public void updateAccountTest() {
        Account a2 = new Account(30,"saving", "Update", "test2", 1213);
        Account a3 = new Account(45,"saving", "Update", "test3", 2123);
        dao.update(Account.class, a2);
        dao.update(Account.class, a3);
    }

    @Test
    public void deleteUserByIdTest() {
        Assertions.assertTrue(dao.delete(User.class, 465));
        Assertions.assertTrue(dao.delete(User.class, 1237));
    }
    @Test
    public void deleteAccountByIdTest() {
        Assertions.assertTrue(dao.delete(Account.class, 30));
        Assertions.assertTrue(dao.delete(Account.class, 45));
    }

    //Run into problem when truncate all records then select all records
//    @Test
//    public void truncateUserTableTest() {
//        dao.truncate(User.class);
//        Assertions.assertEquals(0, ((List)this.dao.select(User.class).orElseThrow(RuntimeException::new)).size());
//
//    }
//    @Test
//    public void truncateAccountTableTest() {
//        dao.truncate(Account.class);
//        Assertions.assertEquals(0, ((List)this.dao.select(Account.class).orElseThrow(RuntimeException::new)).size());
//
//    }

}