package com.mitrokhin.nick.dialer.models;


public class ContactPhonesSettings {
    private String contactID;
    private String contactName;

    public ContactPhonesSettings(String contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    public String getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }
}
