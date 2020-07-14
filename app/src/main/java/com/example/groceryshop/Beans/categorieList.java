package com.example.groceryshop.Beans;

public class categorieList {
    private int ID;
    private String Nom;
    private String NomAr;
    private String image_url;


    private String query_url;

    public categorieList(int ID,String nom,String nomAr,String Image_url) {
        this.ID=ID;
        Nom = nom;
        image_url=Image_url;
        NomAr=nomAr;

    }

    public int getID() {
        return ID;
    }

    public String getNomAr() {
        return NomAr;
    }

    public void setNomAr(String nomAr) {
        NomAr = nomAr;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setID(int ID) {
        this.ID = ID;
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
