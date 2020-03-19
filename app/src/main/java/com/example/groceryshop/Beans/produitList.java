package com.example.groceryshop.Beans;

public class produitList {

    private String CodeProd;

    public String getCodeProd() {
        return CodeProd;
    }

    public void setCodeProd(String codeProd) {
        CodeProd = codeProd;
    }

    private String Nom;
    private String Marque;
    private int Prix;
    private String Qte;

    public produitList(String nom, String marque, int prix, String qte,String CodeProd) {
        Nom = nom;
        Marque = marque;
        Prix = prix;
        Qte = qte;
        this.CodeProd=CodeProd;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getMarque() {
        return Marque;
    }

    public void setMarque(String marque) {
        Marque = marque;
    }

    public int getPrix() {
        return Prix;
    }

    public void setPrix(int prix) {
        Prix = prix;
    }

    public String getQte() {
        return Qte;
    }

    public void setQte(String qte) {
        Qte = qte;
    }
}
