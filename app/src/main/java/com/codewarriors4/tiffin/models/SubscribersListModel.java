package com.codewarriors4.tiffin.models;

public class SubscribersListModel
{
    private String userName;
    private String userStreet;
    private String phoneNumber;

    public SubscribersListModel(String userName, String userStreet, String phoneNumber) {
        this.userName = userName;
        this.userStreet = userStreet;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
