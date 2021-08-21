package com.revature.models;

import java.util.List;

/**
 * Account Model
 */
public class Account{
    // States of an Account object
    private int accountId;
    private String accountType;
    private String username;
    private String password;
    private double balance;

    public Account() {

    }

    public Account(List<Object> obj) {
        this.accountId = (int) obj.get(0);
        this.accountType = (String) obj.get(1);
        this.username = (String) obj.get(2);
        this.password = (String) obj.get(3);
        this.balance = (double) obj.get(4);
    }

    /**
     * Constructor
     * @param accountId unique primary key
     * @param accountType account type ('Saving', 'Checking')
     * @param username username for login
     * @param password password for login
     * @param balance amount in the account
     */
    public Account(int accountId, String accountType, String username, String password, double balance) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    /**
     * Getter for Account ID
     * @return integer value of Account ID
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * Getter for Account Type
     * @return String value of Account Type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Getter for username
     * @return String value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for password
     * @return String value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for balance
     * @return double value of balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Setter for balance
     * @param balance new balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountType='" + accountType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}