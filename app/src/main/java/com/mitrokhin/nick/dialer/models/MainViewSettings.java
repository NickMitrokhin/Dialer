package com.mitrokhin.nick.dialer.models;


public class MainViewSettings {
    private int position;
    private String searchValue;
    private boolean visible;

    public MainViewSettings(int position, String searchValue, boolean visible) {
        this.position = position;
        this.searchValue = searchValue;
        this.visible = visible;
    }

    public int getPosition() {
        return position;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public boolean getVisible() {
        return visible;
    }
}
