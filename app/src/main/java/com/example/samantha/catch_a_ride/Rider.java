package com.example.samantha.catch_a_ride;

public class Rider {

    private String riderID;
    private String name;
    private String phoneNum;
    private String start;
    private String dest;
    private String extra;

    public Rider() {}

    public Rider(String id, String name, String phoneNum) {
        this.riderID = id;
        this.name = name;
        this.phoneNum = phoneNum;
        this.start = "";
        this.dest = "";
        this.extra = "";
    }

    public void setStart(String start) { this.start = start; }

    public void setDest(String dest) { this.dest = dest; }

    public void setExtra(String extra) { this.extra = extra; }

    public String getRiderID() {
        return riderID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getStart() {
        return start;
    }

    public String getDest() {
        return dest;
    }

    public String getExtra() {
        return extra;
    }
}
