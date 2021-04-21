package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;

import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SharedStoreTest {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @BeforeEach
    public void setupEach() {
        sharedPref = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        when(sharedPref.edit()).thenReturn(editor);
    }

    @Test
    public void ContactPhonesStoreGetSettingsTest() {
        when(sharedPref.getString("currentContactID", null)).thenReturn("id");
        when(sharedPref.getString("currentContactName", null)).thenReturn("name");

        ContactPhonesStore store = new ContactPhonesStore(sharedPref);
        ContactPhonesSettings settings = store.getSettings();

        verify(sharedPref, times(1)).getString("currentContactID", null);
        verify(sharedPref, times(1)).getString("currentContactName", null);
        assertEquals("id", settings.getContactID());
        assertEquals("name", settings.getContactName());
    }

    @Test
    public void ContactPhonesStoreSetSettingsTest() {
        ContactPhonesStore store = new ContactPhonesStore(sharedPref);
        ContactPhonesSettings settings = new ContactPhonesSettings("newId", "newName");
        store.setSettings(settings);

        verify(sharedPref, times(1)).edit();
        verify(editor, times(1)).putString("currentContactID", settings.getContactID());
        verify(editor, times(1)).putString("currentContactName", settings.getContactName());
        verify(editor, times(1)).commit();
    }
}
