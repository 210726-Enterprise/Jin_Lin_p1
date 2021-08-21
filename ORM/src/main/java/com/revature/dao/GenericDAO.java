package com.revature.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO {
    //boolean create(Class<?> clazz);
    void truncate(Class<?> clazz);
    int insert(Class<?> clazz, Object obj);
    boolean delete(Class<?> clazz, Object primaryKey);
    boolean update(Class<?> clazz, Object obj);
    Optional<List<Object>> selectAll(Class<?> clazz);
    Optional<Object> select(Class<?> clazz, Object primaryKey);
}
