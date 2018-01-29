package com.example.sheheryar.propertydodo1;

/**
 * Created by Sheheryar on 1/7/2018.
 */

public class Country {


    private String name, lastName;
    private String id;

    public Country(String name, String lastName, String id) {
        this.name = name;
        this.lastName = lastName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }
}

