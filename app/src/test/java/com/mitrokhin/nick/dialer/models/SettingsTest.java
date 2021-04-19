package com.mitrokhin.nick.dialer.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SettingsTest {
    @Test
    public void ContactPhonesSettingsTetst() {
        ContactPhonesSettings settings = new ContactPhonesSettings("id", "name");

        assertEquals("id", settings.getContactID());
        assertEquals("name", settings.getContactName());
    }

    @Test
    public void DialerViewBundleSettingsTest() {
        DialerViewBundleSettings settings = new DialerViewBundleSettings(5, 100, "ready");

        assertEquals(5, settings.getDialCount());
        assertEquals(100, settings.getDialTimeout());
        assertEquals("ready", settings.getDialStatus());

        settings.setDialStatus("new");
        settings.setDialTimeout(200);
        settings.setDialCount(20);

        assertEquals(20, settings.getDialCount());
        assertEquals(200, settings.getDialTimeout());
        assertEquals("new", settings.getDialStatus());
    }

    @Test
    public void MainViewSettingsTest() {
        MainViewSettings settings = new MainViewSettings(10, "test", true);

        assertEquals(10, settings.getPosition());
        assertEquals("test", settings.getSearchValue());
        assertTrue(settings.getVisible());
    }

    @Test
    public void SettingsViewSettingsTest() {
        SettingsViewSettings settings = new SettingsViewSettings(25, 50);

        assertEquals(25, settings.getAttemptCount());
        assertEquals(50, settings.getDialTimeout());
    }
}
