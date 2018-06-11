package com.codewarriors4.tiffin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RegistrationModel implements Parcelable {
    String emailID;
    String password;
    int  userType;
    String confirmPassword;

    public RegistrationModel(String emailID, String password, int userType, String confirmPassword) {
        this.emailID = emailID;
        this.password = password;
        this.userType = userType;
        this.confirmPassword = confirmPassword;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.emailID);
        dest.writeString(this.password);
        dest.writeInt(this.userType);
        dest.writeString(this.confirmPassword);
    }

    protected RegistrationModel(Parcel in) {
        this.emailID = in.readString();
        this.password = in.readString();
        this.userType = in.readInt();
        this.confirmPassword = in.readString();
    }

    public static final Parcelable.Creator<RegistrationModel> CREATOR = new Parcelable.Creator<RegistrationModel>() {
        @Override
        public RegistrationModel createFromParcel(Parcel source) {
            return new RegistrationModel(source);
        }

        @Override
        public RegistrationModel[] newArray(int size) {
            return new RegistrationModel[size];
        }
    };
}
