package com.revature.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

            FileInputStream fis = new FileInputStream("C:\\Users\\jtlin\\Desktop\\Project1\\WebApp\\src\\main\\resources\\application.properties");
            Properties p = new Properties();
            p.load(fis);
            String URL = (String) p.get("URL");
            String USERNAME = (String) p.get("USERNAME");
            String PASSWORD = (String) p.get("PASSWORD");

            // Initiate connection
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            logger.info("Database connection established...");
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            logger.error("Failed to establish a connection to Database...");
        }
        // return connection object
        return connection;
    }
}