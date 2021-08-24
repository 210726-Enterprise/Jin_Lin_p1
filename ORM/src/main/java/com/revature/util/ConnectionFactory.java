package com.revature.util;


import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionFactory class that generate a connection to PostgreSQL database
 */
public class ConnectionFactory {
    private static final Logger logger = Logger.getLogger(ConnectionFactory.class);
    // Connection Object
    private static Connection connection;

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;


    public ConnectionFactory(String url, String username, String password) {
        URL = url;
        USERNAME = username;
        PASSWORD = password;

        PatternLayout layout = new PatternLayout("%p : %d{yyyy-MM-dd HH:mm:ss} : %m%n");
        FileAppender fileAppender = new FileAppender();

        fileAppender.setThreshold(Level.INFO);
        fileAppender.setLayout(layout);
        fileAppender.setFile("src/main/logs/log.txt");
        fileAppender.activateOptions();

        Logger.getRootLogger().addAppender(fileAppender);
    }


    /**
     * Get a connection with database
     * @return returns a connection object
     */
    public Connection getConnection() {

        // Wrap around in try/catch block to cat SQLException
        try {
            Class.forName("org.postgresql.Driver");

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