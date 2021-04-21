package com.mitrokhin.nick.dialer.stores;

import android.os.Bundle;

import com.mitrokhin.nick.dialer.models.DialerViewBundleSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BundleStoreTest {
    private Bundle bundle;

    @BeforeEach
    public void setupEach() {
        bundle = mock(Bundle.class);
    }

    @Test
    public void DialerBundleStoreGetSettingsNullTest() {
        DialerBundleStore store = new DialerBundleStore(null);
        DialerViewBundleSettings settings = store.getSettings();

        assertEquals(0, settings.getDialCount());
        assertEquals(0, settings.getDialTimeout());
        assertNull(settings.getDialStatus());
    }

    @Test
    public void DialerBundleStoreGetSettingsContainsAllTest() {
        when(bundle.containsKey(anyString())).thenReturn(true);
        when(bundle.getInt("currentDialerCount")).thenReturn(5);
        when(bundle.getLong("currentTimeout")).thenReturn(10L);
        when(bundle.getString("currentDialStatus")).thenReturn("test");

        DialerBundleStore store = new DialerBundleStore(bundle);
        DialerViewBundleSettings settings = store.getSettings();

        assertEquals(5, settings.getDialCount());
        assertEquals(10L, settings.getDialTimeout());
        assertEquals("test", settings.getDialStatus());
    }

    @Test
    public void DialerBundleStoreGetSettingsCountTest() {
        when(bundle.containsKey("currentDialerCount")).thenReturn(true);
        when(bundle.containsKey("currentTimeout")).thenReturn(false);
        when(bundle.containsKey("currentDialStatus")).thenReturn(false);
        when(bundle.getInt("currentDialerCount")).thenReturn(5);

        DialerBundleStore store = new DialerBundleStore(bundle);
        DialerViewBundleSettings settings = store.getSettings();

        assertEquals(5, settings.getDialCount());
        assertEquals(0, settings.getDialTimeout());
        assertNull(settings.getDialStatus());
    }

    @Test
    public void DialerBundleStoreGetSettingsTimeoutTest() {
        when(bundle.containsKey("currentDialerCount")).thenReturn(false);
        when(bundle.containsKey("currentTimeout")).thenReturn(true);
        when(bundle.containsKey("currentDialStatus")).thenReturn(false);
        when(bundle.getLong("currentTimeout")).thenReturn(5L);

        DialerBundleStore store = new DialerBundleStore(bundle);
        DialerViewBundleSettings settings = store.getSettings();

        assertEquals(0, settings.getDialCount());
        assertEquals(5L, settings.getDialTimeout());
        assertNull(settings.getDialStatus());
    }

    @Test
    public void DialerBundleStoreGetSettingsStatusTest() {
        when(bundle.containsKey("currentDialerCount")).thenReturn(false);
        when(bundle.containsKey("currentTimeout")).thenReturn(false);
        when(bundle.containsKey("currentDialStatus")).thenReturn(true);
        when(bundle.getString("currentDialStatus")).thenReturn("test");

        DialerBundleStore store = new DialerBundleStore(bundle);
        DialerViewBundleSettings settings = store.getSettings();

        assertEquals(0, settings.getDialCount());
        assertEquals(0, settings.getDialTimeout());
        assertEquals("test", settings.getDialStatus());
    }

    @Test
    public void DialerBundleStoreSetSettingsAllTest() {
        DialerBundleStore store = new DialerBundleStore(bundle);
        DialerViewBundleSettings settings = new DialerViewBundleSettings(5, 10, "test");

        store.setSettings(settings);

        verify(bundle, times(1)).putInt("currentDialerCount", settings.getDialCount());
        verify(bundle, times(1)).putLong("currentTimeout", settings.getDialTimeout());
        verify(bundle, times(1)).putString("currentDialStatus", settings.getDialStatus());
    }

    @Test
    public void DialerBundleStoreSetSettingsRemoveTest() {
        DialerBundleStore store = new DialerBundleStore(bundle);
        DialerViewBundleSettings settings = new DialerViewBundleSettings(0, 0, "");

        store.setSettings(settings);

        verify(bundle, times(1)).remove("currentDialerCount");
        verify(bundle, times(1)).remove("currentTimeout");
        verify(bundle, times(1)).remove("currentDialStatus");
    }
}
