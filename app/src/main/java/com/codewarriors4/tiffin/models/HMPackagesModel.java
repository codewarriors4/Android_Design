package com.codewarriors4.tiffin.models;

public class HMPackagesModel {

    private String id;
    private int packID;
    private String packTitle, packDesc;

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

    public String getHmId() {
        return hmId;
    }

    public void setHmId(String hmId) {
        this.hmId = hmId;
    }

    private double packCost;
    private String hmId;

    public HMPackagesModel(String id, int packID, String packTitle, String packDesc, double packCost, String hmId) {
        this.id = id;
        this.packID = packID;
        this.packTitle = packTitle;
        this.packDesc = packDesc;
        this.packCost = packCost;
        this.hmId = hmId;
    }
}
