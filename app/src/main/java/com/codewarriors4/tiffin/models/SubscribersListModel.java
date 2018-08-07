package com.codewarriors4.tiffin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class SubscribersListModel implements Parcelable {
    private String userName;
    private String usereEmail;
    private String userStreet;
    private String phoneNumber;
    private String packageCost;
    private String packageName;
    private String packageDesc;
    private String startDate;
    private String endDate;

    public SubscribersListModel(String userName, String usereEmail, String userStreet,
                                String phoneNumber, String packageCost, String packageName,
                                String packageDesc, String startDate, String endDate) {
        this.userName = userName;
        this.usereEmail = usereEmail;
        this.userStreet = userStreet;
        this.phoneNumber = phoneNumber;
        this.packageCost = packageCost;
        this.packageName = packageName;
        this.packageDesc = packageDesc;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsereEmail() {
        return usereEmail;
    }

    public void setUsereEmail(String usereEmail) {
        this.usereEmail = usereEmail;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public void setUserStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPackageCost() {
        return packageCost;
    }

    public void setPackageCost(String packageCost) {
        this.packageCost = packageCost;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.usereEmail);
        dest.writeString(this.userStreet);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.packageCost);
        dest.writeString(this.packageName);
        dest.writeString(this.packageDesc);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
    }

    protected SubscribersListModel(Parcel in) {
        this.userName = in.readString();
        this.usereEmail = in.readString();
        this.userStreet = in.readString();
        this.phoneNumber = in.readString();
        this.packageCost = in.readString();
        this.packageName = in.readString();
        this.packageDesc = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
    }

    public static final Parcelable.Creator<SubscribersListModel> CREATOR = new Parcelable.Creator<SubscribersListModel>() {
        @Override
        public SubscribersListModel createFromParcel(Parcel source) {
            return new SubscribersListModel(source);
        }

        @Override
        public SubscribersListModel[] newArray(int size) {
            return new SubscribersListModel[size];
        }
    };
}
