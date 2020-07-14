package com.example.groceryshop.Beans;

public class produitList {

    private String CodeProd;
    private String Nom;
    private String Marque;
    private float Prix;
    private String Qte;
    private String NomAr;
    private String Categorie;
    private String MarqueAr;
    private String TailleAr;
    private String Taille;
    private String Img;
    private float Promotion;
    private float RemisePrix;
    private int QteRemise;

    public String getCodeProd() {
        return CodeProd;
    }

    public void setCodeProd(String codeProd) {
        CodeProd = codeProd;
    }

    public produitList(String nom, String marque, int prix, String Taille,String CodeProd,String NomAr,String
                       Categorie,String MarqueAr,String TailleAr,String Img,float Promotion,float
                       RemisePrix,int QteRemise) {
        Nom = nom;
        Marque = marque;
        Prix = prix;
        this.Taille = Taille;
        this.CodeProd=CodeProd;
        this.NomAr=NomAr;
        this.Categorie=Categorie;
        this.MarqueAr=MarqueAr;
        this.TailleAr=TailleAr;
        this.Img=Img;
        this.Promotion=Promotion;
        this.RemisePrix=RemisePrix;
        this.QteRemise=QteRemise;
        this.Taille=Taille;

    }

    public void setPrix(float prix) {
        Prix = prix;
    }

    public String getTaille() {
        return Taille;
    }

    public void setTaille(String taille) {
        Taille = taille;
    }

    public void setNomAr(String nomAr) {
        NomAr = nomAr;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public void setMarqueAr(String marqueAr) {
        MarqueAr = marqueAr;
    }

    public void setTailleAr(String tailleAr) {
        TailleAr = tailleAr;
    }

    public void setImg(String img) {
        Img = img;
    }

    public void setPromotion(float promotion) {
        Promotion = promotion;
    }

    public void setRemisePrix(float remisePrix) {
        RemisePrix = remisePrix;
    }

    public void setQteRemise(int qteRemise) {
        QteRemise = qteRemise;
    }

    public String getNomAr() {
        return NomAr;
    }

    public String getCategorie() {
        return Categorie;
    }

    public String getMarqueAr() {
        return MarqueAr;
    }

    public String getTailleAr() {
        return TailleAr;
    }

    public String getImg() {
        return Img;
    }

    public float getPromotion() {
        return Promotion;
    }

    public float getRemisePrix() {
        return RemisePrix;
    }

    public int getQteRemise() {
        return QteRemise;
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

    public float getPrix() {
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
