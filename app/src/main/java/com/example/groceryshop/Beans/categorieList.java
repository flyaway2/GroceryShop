package com.example.groceryshop.Beans;

public class categorieList {
    private String Nom;
    private String image_url;


    private String query_url;

    public categorieList(String nom,String Image_url) {
        Nom = nom;
        image_url=Image_url;

    }
    public String getQuery_url() {
        return query_url;
    }

    public void setQuery_url(String query_url) {
        this.query_url = query_url;
    }

    public String getNom() {
        return Nom;
    }

    public String getUrl() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public void setNom(String nom) {
        Nom = nom;
    }









}
