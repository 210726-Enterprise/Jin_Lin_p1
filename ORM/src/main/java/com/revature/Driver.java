package com.revature;

import com.revature.dao.GenericDAO;
import com.revature.dao.GenericDaoImpl;

import com.revature.model.User;
import java.lang.reflect.InvocationTargetException;


public class Driver {
    private static Object User;

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {

        GenericDAO dao = new GenericDaoImpl();

        User u = new User(0, "JintaoTest", "LinTest");
        dao.truncate(u.getClass());
        dao.insert(u.getClass(), u);

    }
}
