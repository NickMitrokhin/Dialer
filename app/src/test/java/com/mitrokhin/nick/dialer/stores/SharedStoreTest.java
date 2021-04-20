package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;

import com.mitrokhin.nick.dialer.mocks.SharedPrefInfo;
import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SharedStoreTest {
    private SharedPrefInfo info;

    @BeforeEach
    public void initInfo() {
        info = new SharedPrefInfo();
    }

    @Test
    public void ContactPhonesStoreGetSettingsTest() {
        SharedPreferences sharedPref = info.getSharedPref();
        when(sharedPref.getString("currentContactID", null)).thenReturn("id");
        when(sharedPref.getString("currentContactName", null)).thenReturn("name");

        ContactPhonesStore store = new ContactPhonesStore(sharedPref);
        ContactPhonesSettings settings = store.getSettings();

        assertEquals("id", settings.getContactID());
        assertEquals("name", settings.getContactName());
    }

    @Test
    public void ContactPhonesStoreSetSettingsTest() {
        SharedPreferences sharedPref = info.getSharedPref();
        SharedPreferences.Editor editor = info.getEditor();
        ContactPhonesStore store = new ContactPhonesStore(sharedPref);
        ContactPhonesSettings settings = new ContactPhonesSettings("newId", "newName");
        store.setSettings(settings);

        verify(sharedPref, times(1)).edit();
        verify(editor, times(1)).putString("currentContactID", settings.getContactID());
        verify(editor, times(1)).putString("currentContactName", settings.getContactName());
        verify(editor, times(1)).commit();
    }
}
