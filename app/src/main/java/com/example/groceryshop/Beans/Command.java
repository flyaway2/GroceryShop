package com.example.groceryshop.Beans;

public class Command {
    private int NumCom,NumProd;
    private String Client,date,Disp,ModPay;
    private String Latitude,Longitude;
    private int Delivrer;

    public Command(int numCom, int numProd, String date, String disp, int delivrer) {
        NumCom = numCom;
        NumProd = numProd;
        this.date = date;
        Disp = disp;
        Delivrer = delivrer;
    }

    public int getNumCom() {
        return NumCom;
    }

    public int getNumProd() {
        return NumProd;
    }

    public String getClient() {
        return Client;
    }

    public String getDate() {
        return date;
    }

    public String getDisp() {
        return Disp;
    }

    public String getModPay() {
        return ModPay;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public int getDelivrer() {
        return Delivrer;
    }

    public void setNumCom(int numCom) {
        NumCom = numCom;
    }

    public void setNumProd(int numProd) {
        NumProd = numProd;
    }

    public void setClient(String client) {
        Client = client;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDisp(String disp) {
        Disp = disp;
    }

    public void setModPay(String modPay) {
        ModPay = modPay;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setDelivrer(int delivrer) {
        Delivrer = delivrer;
    }
}
