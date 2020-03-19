package com.example.groceryshop.Beans;

import android.os.Parcel;
import android.os.Parcelable;

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
    private int Prix;
    private String size;
    private int Qte;
    private String CodeProd;

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

    public int getPrix() {
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



    public produitCommand(String nom, String marque, int prix, String size, int qte,String codeProd) {
        Nom = nom;
        Marque = marque;
        Prix = prix;
        this.size = size;
        Qte = qte;
        this.CodeProd=codeProd;
    }
    protected produitCommand(Parcel in){
        Nom=in.readString();
        Marque=in.readString();
        Prix=in.readInt();
        size=in.readString();
        Qte=in.readInt();
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
        dest.writeInt(this.Prix);
        dest.writeInt(this.Qte);
    }
}
