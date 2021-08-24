package com.revature.dao;

import com.revature.util.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class GenericDaoImpl implements GenericDAO{
    Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
//    @Override
//    public boolean create(Class<?> clazz) {
//        int success = 0;
//        Field[] fields = clazz.getDeclaredFields();
//
//        StringBuilder sql = new StringBuilder();
//        sql.append("CREATE TABLE \"").append(clazz.getSimpleName()).append("\" ");
//
//        /*
//        Map each field to corresponding data type in database
//        e.g. (int)      userId      ->      (int4)      userId
//             (String)   firstName   ->      (varchar)   firstName
//             (String)   lastName    ->      (varchar)   lastName
//
//        Then append each field name and its type to sql string
//         */
//
//        return success == 1;
//
//    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean truncate(Class<?> clazz) {
        // SQL statement
        String sql = "TRUNCATE TABLE \"" + clazz.getSimpleName() + "\" CASCADE;";

        // try/resource block that close the connection automatically
        try {

            // PreparedStatement to avoid SQL Injection
            PreparedStatement ps = connection.prepareStatement(sql);

            // Execute query
            boolean result = ps.execute();
            ps.close();
            return result;
            // catch SQLException
        } catch (SQLException e) {
            logger.warn(String.valueOf(e));
        }
        return false;
    }

    @Override
    public int insert(Class<?> clazz, Object obj) {

        // Get all fields in an unsorted array
        Field[] fields = clazz.getDeclaredFields();
        // Get all getter methods in an unsorted array
        Method[] getters = Arrays.stream(clazz.getMethods())
                .filter((m) -> m.getName().startsWith("get")).toArray(Method[]::new);

        Map<String, Method> map = new HashMap<>();
        ArrayList<Method> methodsToInvoke = new ArrayList<>();

        // Map each getter methods to its corresponding field
        for (Field field : fields) {
            for (Method getter : getters) {
                if (getter.getName().toLowerCase().contains(field.getName().toLowerCase())) {
                    map.put(field.getName(), getter);
                }
            }
        }
        // Sort the methods in the same sequence of class fields
        for(Field f:fields) {
            methodsToInvoke.add(map.get(f.getName()));
        }

        try {
            /*
             * Build SQL Statement using StringBuilder without a primary key in the parameters
             */
            StringBuilder builder = new StringBuilder();

            builder.append("INSERT INTO \"").append(clazz.getSimpleName()).append("\" (");

            PreparedStatement ps;
            if(methodsToInvoke.get(0).invoke(obj).equals(0)) {
                for(int x = 1; x < fields.length; x++) {
                    builder.append(fields[x].getName()).append((", "));
                }
                builder.delete(builder.length() - 2, builder.length());
                builder.append(") VALUES(");

                for(int x = 1; x < fields.length; x++) {
                    builder.append("'").append(methodsToInvoke.get(x).invoke(obj)).append("', ");
                }
                builder.delete(builder.length() - 2, builder.length());
                builder.append(");");

                String sql = builder.toString();
                ps = connection.prepareStatement(sql);

            } else {
                /*
                 * Build SQL Statement using StringBuilder with a primary key in the parameters
                 */
                for (Field field : fields) {
                    builder.append(field.getName()).append((", "));
                }
                builder.delete(builder.length() - 2, builder.length());
                builder.append(") VALUES(");

                for(int x = 0; x < fields.length; x++) {
                    builder.append("'").append(methodsToInvoke.get(x).invoke(obj)).append("', ");
                }
                builder.delete(builder.length() - 2, builder.length());
                builder.append(");");

                String sql = builder.toString();
                ps = connection.prepareStatement(sql);
            }
            int result = ps.executeUpdate();
            ps.close();
            return result;

        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            logger.warn(String.valueOf(e));
        }
        return 0;
    }

    @Override
    public boolean delete(Class<?> clazz, Object primaryKey) {
        int success = 0;

        // Get all declared fields of clazz
        Field[] fields = clazz.getDeclaredFields();

        // try/resource block that close the connection automatically
        try {
            assert connection != null;
            // SQL Statement
            String sql = "DELETE FROM \"" + clazz.getSimpleName() +
                    "\" WHERE " + fields[0].getName() + " = " + primaryKey + ";";
            // PreparedStatement to avoid SQL Injection
            PreparedStatement ps = connection.prepareStatement(sql);

            // Execute query
            success = ps.executeUpdate();
            ps.close();

            // catch SQLException
        } catch (SQLException e) {
            logger.warn(String.valueOf(e));
        }
        return success == 1;

    }

    @Override
    public boolean update(Class<?> clazz, Object obj) {
        int success = 0;
        // Get all fields in an unsorted array
        Field[] fields = clazz.getDeclaredFields();
        // Get all getter methods in an unsorted array
        Method[] getters = Arrays.stream(clazz.getMethods())
                .filter((m) -> m.getName().startsWith("get")).toArray(Method[]::new);

        Map<String, Method> map = new HashMap<>();
        ArrayList<Method> methodsToInvoke = new ArrayList<>();

        // Map each getter methods to its corresponding field
        for (Field field : fields) {
            for (Method getter : getters) {
                if (getter.getName().toLowerCase().contains(field.getName().toLowerCase())) {
                    map.put(field.getName(), getter);
                }
            }
        }
        // Sort the methods in the same sequence of class fields
        for(Field f:fields) {
            methodsToInvoke.add(map.get(f.getName()));
        }

        try{
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE \"")
                    .append(clazz.getSimpleName())
                    .append("\" ")
                    .append("SET ");

            for(int x = 1; x < fields.length; x++) {
                builder.append(fields[x].getName())
                        .append(" = '")
                        .append(methodsToInvoke.get(x).invoke(obj))
                        .append("',");
            }

            builder.delete(builder.length() - 1, builder.length());
            builder.append(" WHERE ")
                    .append(fields[0].getName())
                    .append(" = ")
                    .append(methodsToInvoke.get(0).invoke(obj))
                    .append(";");

            String sql = builder.toString();
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement(sql);

            success = ps.executeUpdate();
            ps.close();

        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            logger.warn(String.valueOf(e));
        }

        return success == 1;
    }

    @Override
    public Optional<List<Object>> selectAll(Class<?> clazz) {
        // Initialize List of objects
        List<Object> list = new ArrayList<>();

        //Get all fields of clazz
        Field[] fields = clazz.getDeclaredFields();
        //Get all Constructors of clazz
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        // SQL Statement
        String sql = "SELECT * FROM \"" + clazz.getSimpleName() + "\";";

        // try/resource block that close the connection automatically
        try {

            // Use Statement since will be executing a static query
            PreparedStatement ps = connection.prepareStatement(sql);

            // Execute and save result
            ResultSet rs = ps.executeQuery();

            // List of parameters for constructor
            ArrayList<Object> parameters = new ArrayList<>();

            // Get the values for every record in the database
            while(rs.next()) {
                int column = 1;
                // Get the column value for every column in the database
                while (column != fields.length + 1) {
                    parameters.add(rs.getObject(column));
                    column++;
                }
                // Create Object with Constructor that takes in a List of Objects
                Object object = Arrays.stream(constructors)
                        .filter(c -> c.getParameterCount() == 1) // Constructor that takes in Object[]
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .newInstance(parameters);
                // Add the new object to a list
                list.add(object);
                // Clear the parameter list to taking the next record
                parameters.clear();
            }
            ps.close();
            rs.close();
        // Catch Exceptions
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.warn(String.valueOf(e));
        }
        // return the list of objects
        return Optional.of(list);
    }

    @Override
    public Optional<Object> select(Class<?> clazz, Object primaryKey) {
        // Get all declared fields of clazz
        Field[] fields = clazz.getDeclaredFields();
        //Get all Constructors of clazz
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        try {
            assert connection != null;

            String sql = "SELECT * FROM \"" +
                    clazz.getSimpleName() +
                    "\" WHERE " +
                    fields[0].getName() +
                    " = " +
                    primaryKey +
                    ";";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ArrayList<Object> parameters = new ArrayList<>();
                int column = 1;
                // Get the column value for every column in the database
                while (column != fields.length + 1) {
                    parameters.add(rs.getObject(column));
                    column++;
                }
                // Create an instance of clazz
                Object object = Arrays.stream(constructors)
                        .filter(c -> c.getParameterCount() == 1) // Constructor that takes in Object[]
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .newInstance(parameters);
                return Optional.of(object);
            }
            ps.close();
            rs.close();

        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.warn(String.valueOf(e));
        }

        return Optional.empty();
    }
}
