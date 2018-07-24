package com.codewarriors4.tiffin.models;

public class TSSubscriptionsModel {

    private String id;
    private int hmID,packID,subID;
    private String hmName, packTitle, subStartDate, subEndDate;
    private Double package_cost;
    private float ratingCount;


    public TSSubscriptionsModel(String id, int hmID, int subID, int packID, String hmName, String packTitle, String subStartDate, String subEndDate, Double package_cost,float ratingCount) {
        this.id = id;
        this.hmID = hmID;
        this.packID = packID;
        this.subID = subID;

        this.hmName = hmName;
        this.packTitle = packTitle;
        this.subStartDate = subStartDate;
        this.subEndDate = subEndDate;
        this.package_cost = package_cost;
        this.ratingCount = ratingCount;

    }

    public float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Double getPackage_cost() {
        return package_cost;
    }

    public void setPackage_cost(Double package_cost) {
        this.package_cost = package_cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHmID() {
        return hmID;
    }

    public void setHmID(int hmID) {
        this.hmID = hmID;
    }

    public int getPackID() {
        return packID;
    }

    public void setPackID(int packID) {
        this.packID = packID;
    }

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }

    public String getHmName() {
        return hmName;
    }

    public void setHmName(String hmName) {
        this.hmName = hmName;
    }

    public String getPackTitle() {
        return packTitle;
    }

    public void setPackTitle(String packTitle) {
        this.packTitle = packTitle;
    }

    public String getSubStartDate() {
        return subStartDate;
    }

    public void setSubStartDate(String subStartDate) {
        this.subStartDate = subStartDate;
    }

    public String getSubEndDate() {
        return subEndDate;
    }

    public void setSubEndDate(String subEndDate) {
        this.subEndDate = subEndDate;
    }


}
