package com.example.groceryshop.Beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

public class produitCommand implements Parcelable {



    public static final Creator<produitCommand> CREATOR = new Creator<produitCommand>() {
        @Override
        public produitCommand createFromParcel(Parcel in) {
            return new produitCommand(in);
        }

        @Override
        public produitCommand[] newArray(int size) {
            return new produitCommand[size];
        }
    };

    private String Nom;
    private String Marque;
    private float Prix;
    private String size;
    private int Qte;
    private String CodeProd;
    private float PrixRemise;
    private int QteRemise;
    private float Promotion;
    private String Img;
    private String NomAr;
    private String MarqueAr;
    private String sizeAr;


    public String getCodeProd() {
        return CodeProd;
    }

    public void setCodeProd(String codeProd) {
        CodeProd = codeProd;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQte() {
        return Qte;
    }

    public void setQte(int qte) {
        Qte = qte;
    }



    public produitCommand(String nom, String marque, float prix, String size, int qte,String codeProd,int QteRemise,float
                          PrixRemise,float Promotion,String Img,String NomAr,String MarqueAr,String sizeAr) {
        this.Img= Img;
        Nom = nom;
        Marque = marque;
        Prix = prix;
        this.size = size;
        Qte = qte;
        this.CodeProd=codeProd;
        this.QteRemise=QteRemise;
        this.PrixRemise=PrixRemise;
        this.Promotion=Promotion;
        this.NomAr=NomAr;
        this.MarqueAr=MarqueAr;
        this.sizeAr=sizeAr;
    }
    protected produitCommand(Parcel in){
        Nom=in.readString();
        Marque=in.readString();
        Prix=in.readInt();
        size=in.readString();
        Qte=in.readInt();
        Promotion=in.readFloat();
        PrixRemise=in.readFloat();
        QteRemise=in.readInt();
        Img=in.readString();
        NomAr=in.readString();
        MarqueAr=in.readString();
        sizeAr=in.readString();
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getNomAr() {
        return NomAr;
    }

    public String getMarqueAr() {
        return MarqueAr;
    }

    public String getSizeAr() {
        return sizeAr;
    }

    public void setNomAr(String nomAr) {
        NomAr = nomAr;
    }

    public void setMarqueAr(String marqueAr) {
        MarqueAr = marqueAr;
    }

    public void setSizeAr(String sizeAr) {
        this.sizeAr = sizeAr;
    }

    public float getPrixRemise() {
        return PrixRemise;
    }

    public int getQteRemise() {
        return QteRemise;
    }

    public float getPromotion() {
        return Promotion;
    }

    public void setPrix(float prix) {
        Prix = prix;
    }

    public void setPrixRemise(float prixRemise) {
        PrixRemise = prixRemise;
    }

    public void setQteRemise(int qteRemise) {
        QteRemise = qteRemise;
    }

    public void setPromotion(float promotion) {
        Promotion = promotion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Nom);
        dest.writeString(this.Marque);
        dest.writeString(this.size);
        dest.writeFloat(this.Prix);
        dest.writeInt(this.Qte);
        dest.writeInt(this.QteRemise);
        dest.writeFloat(this.PrixRemise);
        dest.writeFloat(this.Promotion);
        dest.writeString(this.Img);
        dest.writeString(this.NomAr);
        dest.writeString(this.MarqueAr);
        dest.writeString(this.sizeAr);
    }
}
