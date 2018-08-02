package com.codewarriors4.tiffin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeMakerListItems implements Parcelable {

    String homeMakerFirstName;
    String homeMakerLastName;
    String homeMakerStreet;
    String homeMakerCity;
    String homeMakerZipCode;
    String homeMakerPhone;
    String homeMakerRating;
    int id;
    int hmID;



    public HomeMakerListItems(String homeMakerFirstName, String homeMakerLastName,
                              String homeMakerStreet, String homeMakerCity, String homeMakerZipCode,
                              String homeMakerPhone, String homeMakerRating, int id, int hmID) {
        this.homeMakerFirstName = homeMakerFirstName;
        this.homeMakerLastName = homeMakerLastName;
        this.homeMakerStreet = homeMakerStreet;
        this.homeMakerCity = homeMakerCity;
        this.homeMakerZipCode = homeMakerZipCode;
        this.homeMakerPhone = homeMakerPhone;
        this.homeMakerRating = homeMakerRating;
        this.id = id;
        this.hmID = hmID;
    }

    public String getHomeMakerFirstName() {
        return homeMakerFirstName;
    }

    public void setHomeMakerFirstName(String homeMakerFirstName) {
        this.homeMakerFirstName = homeMakerFirstName;
    }

    public String getHomeMakerLastName() {
        return homeMakerLastName;
    }

    public void setHomeMakerLastName(String homeMakerLastName) {
        this.homeMakerLastName = homeMakerLastName;
    }

    public String getHomeMakerStreet() {
        return homeMakerStreet;
    }

    public void setHomeMakerStreet(String homeMakerStreet) {
        this.homeMakerStreet = homeMakerStreet;
    }

    public String getHomeMakerCity() {
        return homeMakerCity;
    }

    public void setHomeMakerCity(String homeMakerCity) {
        this.homeMakerCity = homeMakerCity;
    }

    public String getHomeMakerZipCode() {
        return homeMakerZipCode;
    }

    public void setHomeMakerZipCode(String homeMakerZipCode) {
        this.homeMakerZipCode = homeMakerZipCode;
    }

    public String getHomeMakerPhone() {
        return homeMakerPhone;
    }

    public void setHomeMakerPhone(String homeMakerPhone) {
        this.homeMakerPhone = homeMakerPhone;
    }

    public String getHomeMakerRating() {
        return homeMakerRating;
    }

    public void setHomeMakerRating(String homeMakerRating) {
        this.homeMakerRating = homeMakerRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHmID() {
        return hmID;
    }

    public void setHmID(int hmID) {
        this.hmID = hmID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.homeMakerFirstName);
        dest.writeString(this.homeMakerLastName);
        dest.writeString(this.homeMakerStreet);
        dest.writeString(this.homeMakerCity);
        dest.writeString(this.homeMakerZipCode);
        dest.writeString(this.homeMakerPhone);
        dest.writeString(this.homeMakerRating);
        dest.writeInt(this.id);
        dest.writeInt(this.hmID);
    }

    protected HomeMakerListItems(Parcel in) {
        this.homeMakerFirstName = in.readString();
        this.homeMakerLastName = in.readString();
        this.homeMakerStreet = in.readString();
        this.homeMakerCity = in.readString();
        this.homeMakerZipCode = in.readString();
        this.homeMakerPhone = in.readString();
        this.homeMakerRating = in.readString();
        this.id = in.readInt();
        this.hmID = in.readInt();
    }

    public static final Parcelable.Creator<HomeMakerListItems> CREATOR = new Parcelable.Creator<HomeMakerListItems>() {
        @Override
        public HomeMakerListItems createFromParcel(Parcel source) {
            return new HomeMakerListItems(source);
        }

        @Override
        public HomeMakerListItems[] newArray(int size) {
            return new HomeMakerListItems[size];
        }
    };
}
