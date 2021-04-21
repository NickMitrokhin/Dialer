package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;

import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;
import com.mitrokhin.nick.dialer.models.MainViewSettings;
import com.mitrokhin.nick.dialer.models.SettingsViewSettings;

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
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor);
    }

    @Test
    public void ContactPhonesStoreGetSettingsTest() {
        when(sharedPref.getString("currentContactID", null)).thenReturn("id");
        when(sharedPref.getString("currentContactName", null)).thenReturn("name");

        ContactPhonesStore store = new ContactPhonesStore(sharedPref);
        ContactPhonesSettings settings = store.getSettings();

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

    @Test
    public void MainStoreGetSettingsTest() {
        when(sharedPref.getInt("contactPosition", 0)).thenReturn(10);
        when(sharedPref.getString("searchExpr", "")).thenReturn("test");
        when(sharedPref.getBoolean("searchVisible", false)).thenReturn(true);

        MainStore store = new MainStore(sharedPref);
        MainViewSettings settings = store.getSettings();

        assertEquals(10, settings.getPosition());
        assertEquals("test", settings.getSearchValue());
        assertTrue(settings.getVisible());
    }

    @Test
    public void MainStoreSetSettingsTest() {
        MainStore store = new MainStore(sharedPref);
        MainViewSettings settings = new MainViewSettings(5, "test", false);

        store.setSettings(settings);

        verify(sharedPref, times(1)).edit();
        verify(editor, times(1)).putInt("contactPosition", settings.getPosition());
        verify(editor, times(1)).putString("searchExpr", settings.getSearchValue());
        verify(editor, times(1)).putBoolean("searchVisible", settings.getVisible());
        verify(editor, times(1)).commit();
    }

    @Test
    public void SettingsStoreGetSettingsTest() {
        String[] keys = { "a", "b" };

        when(sharedPref.getInt(keys[0], 1)).thenReturn(3);
        when(sharedPref.getInt(keys[1], 1)).thenReturn(5);

        SettingsStore store = new SettingsStore(sharedPref, keys);
        SettingsViewSettings settings = store.getSettings();

        verify(sharedPref, times(1)).getInt(keys[0], 1);
        verify(sharedPref, times(1)).getInt(keys[1], 1);
        assertEquals(3, settings.getAttemptCount());
        assertEquals(5, settings.getDialTimeout());
    }

    @Test
    public void SettingsStoreSetSettingsTest() {
        String[] keys = { "a", "b" };
        SettingsStore store = new SettingsStore(sharedPref, keys);
        SettingsViewSettings settings = new SettingsViewSettings(7, 9);

        store.setSettings(settings);

        verify(sharedPref, times(1)).edit();
        verify(editor, times(1)).putInt(keys[0], settings.getAttemptCount());
        verify(editor, times(1)).putInt(keys[1], settings.getDialTimeout());
        verify(editor, times(1)).commit();
    }
}
