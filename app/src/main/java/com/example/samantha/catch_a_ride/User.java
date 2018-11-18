package com.example.samantha.catch_a_ride;

/*
 * Author: Val Lapensée-Rankine
 *
 * User
 * User object created when a new user signs up,
 * information is added and then the object is
 * pushed to the Firebase database to store user data.
 */
public class User {
    private String userID;
    private String userName;
    private String userPaymentType;
    private String userPhoneNumber;

    public User() {}

    public User(String userID, String userName, String userPaymentType, String userPhoneNumber) {
        this.userID = userID;
        this.userName = userName;
        this.userPaymentType = userPaymentType;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserID() { return userID; }

    public String getUserName() {
        return userName;
    }

    public String getUserPaymentType() {
        return userPaymentType;
    }

    public String getUserPhoneNumber() { return userPhoneNumber;}
}
