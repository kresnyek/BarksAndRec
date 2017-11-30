package com.iu.ckresnye.barksandrec;

/**
 * Created by Cassie on 11/28/2017.
 */

public class User {

    private String fName;
    private String lName;
    private Boolean hasPet;
    private String parkId;
    private String token;

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public Boolean getHasPet() {
        return hasPet;
    }

    public void setHasPet(Boolean hasPet) {
        this.hasPet = hasPet;
    }


    public User()
    {
        this.fName = "Unloaded First Name";
        this.lName = "Unloaded Last Name";
        this.hasPet = false;

    }

    public User(String fName, String lName, Boolean hasPet, String token, String parkId)
    {
        this.fName = fName;
        this.lName = lName;
        this.hasPet = hasPet;
        this.token = token;
        this.parkId = parkId;
    }
}
