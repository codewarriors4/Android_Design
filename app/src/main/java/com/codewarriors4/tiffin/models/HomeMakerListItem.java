package com.codewarriors4.tiffin.models;

public class HomeMakerListItem
{
    private String homeMakerListItemTitle;
    private String homeMakerListItemContent;

    public HomeMakerListItem(String homeMakerListItemTitle, String homeMakerListItemContent) {
        this.homeMakerListItemTitle = homeMakerListItemTitle;
        this.homeMakerListItemContent = homeMakerListItemContent;
    }

    public String getHomeMakerListItemTitle() {
        return homeMakerListItemTitle;
    }

    public void setHomeMakerListItemTitle(String homeMakerListItemTitle) {
        this.homeMakerListItemTitle = homeMakerListItemTitle;
    }

    public String getHomeMakerListItemContent() {
        return homeMakerListItemContent;
    }

    public void setHomeMakerListItemContent(String homeMakerListItemContent) {
        this.homeMakerListItemContent = homeMakerListItemContent;
    }
}
