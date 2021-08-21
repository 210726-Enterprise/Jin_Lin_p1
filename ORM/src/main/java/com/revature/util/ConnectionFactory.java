package com.revature.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionFactory class that generate a connection to PostgreSQL database
 */
public class ConnectionFactory {
    // "jdbc:postgresql://[ENDPOINT]/[DATABASE]"

    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
    // Connection Object
    private static Connection connection;

    /**
     * Get a connection with database
     * @return returns a connection object
     */
    public static Connection getConnection() {

        // Wrap around in try/catch block to cat SQLException
        try {
            Class.forName("org.postgresql.Driver");

            String URL = "jdbc:postgresql://database-1.cz4piyzhcf32.us-east-2.rds.amazonaws.com/postgres?currentSchema=mockdb";
            String USERNAME = "jinlin";
            String PASSWORD = "jinlin226";

            // Initiate connection
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            logger.info("Database connection established...");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("Failed to establish a connection to Database...");
        }
        // return connection object
        return connection;
    }
}