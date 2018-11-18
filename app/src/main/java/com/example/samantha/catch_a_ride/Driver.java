package com.example.samantha.catch_a_ride;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Val Lapensée-Rankine
 *
 * Driver
 * Driver object created when a driver is made available.
 * Information is added and the driver pushed to the database.
 */
public class Driver {

    private String driverID;
    private String driverName;
    private String driverPaymentType;
    private String driverPhoneNumber;
    private List<Rider> potentialRiders = new ArrayList<>();
    private Rider currentRider;

    public Driver() {}

    public Driver(String driverID, String driverName, String driverPaymentType, String driverPhoneNumber) {
        this.driverID = driverID;
        this.driverName = driverName;
        this.driverPaymentType = driverPaymentType;
        this.driverPhoneNumber = driverPhoneNumber;
        currentRider = null;
    }

    public String getDriverID() { return driverID; }

    public String getDriverName() { return driverName; }

    public String getDriverPaymentType() { return driverPaymentType; }

    public String getDriverPhoneNumber() { return driverPhoneNumber; }

    public Rider getCurrentRider() { return currentRider; }

    public void deleteCurrentRider() { currentRider = null; }

    public void setCurrentRider(Rider currentRider) { this.currentRider = currentRider; }

    public List<Rider> getPotentialRiders() { return potentialRiders; }

    public void addPotentialRider(Rider rider) { potentialRiders.add(rider); }

    public void deletePotentialRider(Rider rider) { potentialRiders.remove(rider); }

    public void clearPotentialRiders() { potentialRiders.clear(); }
}