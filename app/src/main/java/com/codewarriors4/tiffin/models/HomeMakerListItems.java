package com.codewarriors4.tiffin.models;

public class HomeMakerListItems
{
    String homeMakerEmail;
    String homeMakerPostCode;
    String homeMakerName;
    int id;

    public int getHmId() {
        return hmId;
    }

    public void setHmId(int hmId) {
        this.hmId = hmId;
    }

    int hmId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HomeMakerListItems(int id, int hmId, String homeMakerEmail, String homeMakerPostCode, String homeMakerName) {
        this.id =  id;
        this.hmId = hmId;
        this.homeMakerEmail = homeMakerEmail;
        this.homeMakerPostCode = homeMakerPostCode;
        this.homeMakerName = homeMakerName;
    }

    public String getHomeMakerEmail() {
        return homeMakerEmail;
    }

    public void setHomeMakerEmail(String homeMakerEmail) {
        this.homeMakerEmail = homeMakerEmail;
    }

    public String getHomeMakerPostCode() {
        return homeMakerPostCode;
    }

    public void setHomeMakerPostCode(String homeMakerPostCode) {
        this.homeMakerPostCode = homeMakerPostCode;
    }

    public String getHomeMakerName() {
        return homeMakerName;
    }

    public void setHomeMakerName(String homeMakerName) {
        this.homeMakerName = homeMakerName;
    }
}
