package com.iu.ckresnye.barksandrec;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Cassie on 11/20/2017.
 */

public class Pet
{
    private String name;
    private String breed;
    private Date bday;
    //private Uri pic;

    public Pet(String name, String breed, Date bday)
    {
        this.name = name;
        this.breed = breed;
        this.bday  = bday;

    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public Date getBday() {
        return bday;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setBday(Date bday) {
        this.bday = bday;
    }
}
