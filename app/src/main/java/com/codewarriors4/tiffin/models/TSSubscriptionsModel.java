package com.codewarriors4.tiffin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TSSubscriptionsModel implements Parcelable {

    private String id;
    private int hmID,packID,subID;
    private String hmName, packTitle, packDesc,subStartDate, subEndDate;
    private Double packageCost;
    private float ratingCount;
    private String driverName;
    private String driverPhone;
    private String driverUID;
    public TSSubscriptionsModel(String id, int hmID, int packID, int subID, String hmName,
                                String packTitle, String packDesc, String subStartDate,
                                String subEndDate, Double packageCost, float ratingCount,
                                String driverName, String driverPhone, String driverUID) {
        this.id = id;
        this.hmID = hmID;
        this.packID = packID;
        this.subID = subID;
        this.hmName = hmName;
        this.packTitle = packTitle;
        this.packDesc = packDesc;
        this.subStartDate = subStartDate;
        this.subEndDate = subEndDate;
        this.packageCost = packageCost;
        this.ratingCount = ratingCount;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverUID = driverUID;
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

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
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

    public Double getPackageCost() {
        return packageCost;
    }

    public void setPackageCost(Double packageCost) {
        this.packageCost = packageCost;
    }

    public float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverUID() {
        return driverUID;
    }

    public void setDriverUID(String driverUID) {
        this.driverUID = driverUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.hmID);
        dest.writeInt(this.packID);
        dest.writeInt(this.subID);
        dest.writeString(this.hmName);
        dest.writeString(this.packTitle);
        dest.writeString(this.packDesc);
        dest.writeString(this.subStartDate);
        dest.writeString(this.subEndDate);
        dest.writeValue(this.packageCost);
        dest.writeFloat(this.ratingCount);
        dest.writeString(this.driverName);
        dest.writeString(this.driverPhone);
        dest.writeString(this.driverUID);
    }

    protected TSSubscriptionsModel(Parcel in) {
        this.id = in.readString();
        this.hmID = in.readInt();
        this.packID = in.readInt();
        this.subID = in.readInt();
        this.hmName = in.readString();
        this.packTitle = in.readString();
        this.packDesc = in.readString();
        this.subStartDate = in.readString();
        this.subEndDate = in.readString();
        this.packageCost = (Double) in.readValue(Double.class.getClassLoader());
        this.ratingCount = in.readFloat();
        this.driverName = in.readString();
        this.driverPhone = in.readString();
        this.driverUID = in.readString();
    }

    public static final Parcelable.Creator<TSSubscriptionsModel> CREATOR = new Parcelable.Creator<TSSubscriptionsModel>() {
        @Override
        public TSSubscriptionsModel createFromParcel(Parcel source) {
            return new TSSubscriptionsModel(source);
        }

        @Override
        public TSSubscriptionsModel[] newArray(int size) {
            return new TSSubscriptionsModel[size];
        }
    };
}
