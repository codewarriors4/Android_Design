package com.codewarriors4.tiffin.models;

public class HMPackages {

    private String id;
    private String packTitle, packDesc;
    private double packCost;

    public HMPackages(String id, String packTitle, String packDesc, double packCost) {
        this.id = id;
        this.packTitle = packTitle;
        this.packDesc = packDesc;
        this.packCost = packCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
