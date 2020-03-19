package com.example.magasinadmin;

public class Type {
    private String name;

    //empty constructor
    public Type() {

    }

    //simple constructor
    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
