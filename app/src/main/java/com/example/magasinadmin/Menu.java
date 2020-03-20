package com.example.magasinadmin;

public class Menu {
    private String name;
    private  String details;
    private long prix;
    private boolean choix;
    private int nbr_viande;

    public Menu() {
    }

    public String getName() {
        return name;
    }

    public boolean isChoix() {
        return choix;
    }

    public int getNbr_viande() {
        return nbr_viande;
    }

    public String getDetails() {
        return details;
    }


    public long getPrix() {
        return prix;
    }


    public Menu(String name, String details, long prix) {
        this.name = name;
        this.details = details;
        this.prix = prix;
    }
}
