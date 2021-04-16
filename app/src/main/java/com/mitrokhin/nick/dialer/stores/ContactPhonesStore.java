package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;

import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;


public class ContactPhonesStore extends SharedStore<ContactPhonesSettings> {
    private final String CURRENT_CONTACT_ID = "currentContactID";
    private final String CURRENT_CONTACT_NAME = "currentContactName";

    public ContactPhonesStore(SharedPreferences sharedPref) {
        super(sharedPref);
    }

    @Override
    public ContactPhonesSettings getSettings() {
        String currentContactID = sharedPref.getString(CURRENT_CONTACT_ID, null);
        String currentContactName = sharedPref.getString(CURRENT_CONTACT_NAME, null);

        return new ContactPhonesSettings(currentContactID, currentContactName);
    }

    @Override
    public void setSettings(ContactPhonesSettings settings) {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(CURRENT_CONTACT_ID, settings.getContactID());
        editor.putString(CURRENT_CONTACT_NAME, settings.getContactName());
        editor.commit();
    }
}
