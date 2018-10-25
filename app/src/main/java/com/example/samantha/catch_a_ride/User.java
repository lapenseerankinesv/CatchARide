package com.example.samantha.catch_a_ride;

public class User {
    String userID;
    String userName;
    String userPaymentType;
    String userPhoneNumber;
    boolean isDriving;

    public User() {

    }

    public User(String userID, String userName, String userPaymentType, String userPhoneNumber) {
        this.userID = userID;
        this.userName = userName;
        this.userPaymentType = userPaymentType;
        this.userPhoneNumber = userPhoneNumber;
        isDriving = false;
    }

    public String getUserID() { return userID; }

    public String getUserName() {
        return userName;
    }

    public String getUserPaymentType() {
        return userPaymentType;
    }

    public String getUserPhoneNumber() { return userPhoneNumber;}

    public boolean getIsDriving() { return isDriving;}

    public void changeIsDriving() {
        isDriving = !isDriving;
    }
}
