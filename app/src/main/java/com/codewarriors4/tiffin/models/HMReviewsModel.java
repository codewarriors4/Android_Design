package com.codewarriors4.tiffin.models;

public class HMReviewsModel {

    private String id,hmID;

    private String hmName, tsName, ratingDesc, reviewUpdateDate;
    private float ratingCount;

    public HMReviewsModel(String id, String hmID, String hmName, String tsName, String ratingDesc, String reviewUpdateDate, float ratingCount) {
        this.id = id;
        this.hmID = hmID;

        this.hmName = hmName;
        this.tsName = tsName;
        this.ratingDesc = ratingDesc;
        this.reviewUpdateDate = reviewUpdateDate;
        this.ratingCount = ratingCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHmID() {
        return hmID;
    }

    public void setHmID(String hmID) {
        this.hmID = hmID;
    }



    public String getHmName() {
        return hmName;
    }

    public void setHmName(String hmName) {
        this.hmName = hmName;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getRatingDesc() {
        return ratingDesc;
    }

    public void setRatingDesc(String ratingDesc) {
        this.ratingDesc = ratingDesc;
    }

    public String getReviewUpdateDate() {
        return reviewUpdateDate;
    }

    public void setReviewUpdateDate(String reviewUpdateDate) {
        this.reviewUpdateDate = reviewUpdateDate;
    }

    public float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }
}
