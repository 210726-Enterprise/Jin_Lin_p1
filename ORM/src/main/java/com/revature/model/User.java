package com.revature.model;

import java.util.List;

/**
 * User Model
 */
public class User {
    // States of an User object
    private int userId;
    private String firstName;
    private String lastName;


    public User() {

    }
    public User(List<Object> obj) {
        this.userId = (int) obj.get(0);
        this.firstName = (String) obj.get(1);
        this.lastName = (String) obj.get(2);

    }
    /**
     * Constructor
     * @param userId unique primary key
     * @param firstName first name of the user
     * @param lastName last name of the user
     */
    public User(int userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Getter for userId
     * @return integer value of userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Getter for first name
     * @return String value of first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for first name
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for last name
     * @return String value of last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for last name
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
