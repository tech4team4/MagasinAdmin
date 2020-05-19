package com.example.magasinadmin;

public class Model_Category {
    private String name;
    private Model_Category(){
    }

    public Model_Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
