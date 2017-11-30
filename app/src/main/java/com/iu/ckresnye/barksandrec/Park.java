package com.iu.ckresnye.barksandrec;

/**
 * Created by Cassie on 11/29/2017.
 */

public class Park {

    private String name;
    private String adminID;
    private boolean hasPhotos;
    private int currentDogs;
    private String hours;
    private double x,y;

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public int getCurrentDogs() {
        return currentDogs;
    }

    public void setCurrentDogs(int currentDogs) {
        this.currentDogs = currentDogs;
    }

    public Park()
    {
        this.hasPhotos = false;
    }

    public Park(String name, String adminID, boolean hasPhotos, int currentDogs,
                String hours, double x, double y) {
        this.name = name;
        this.adminID = adminID;
        this.hasPhotos = hasPhotos;
        this.currentDogs = currentDogs;
        this.hours = hours;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public boolean isHasPhotos() {
        return hasPhotos;
    }

    public void setHasPhotos(boolean hasPhotos) {
        this.hasPhotos = hasPhotos;
    }


}
