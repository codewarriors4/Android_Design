package com.codewarriors4.tiffin.models;

public class HMPackagesModel {

    private String id;
    private int packID;
    private String packTitle, packDesc;
    private double packCost;



    public HMPackagesModel(String id, String packTitle, String packDesc, double packCost, int packID) {
        this.id = id;
        this.packTitle = packTitle;
        this.packDesc = packDesc;
        this.packCost = packCost;
        this.packID = packID;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPackID() {
        return packID;
    }

    public void setPackID(int packID) {
        this.packID = packID;
    }

    public String getPackTitle() {
        return packTitle;
    }

    public void setPackTitle(String packTitle) {
        this.packTitle = packTitle;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
    }

    public double getPackCost() {
        return packCost;
    }

    public void setPackCost(double packCost) {
        this.packCost = packCost;
    }
}
