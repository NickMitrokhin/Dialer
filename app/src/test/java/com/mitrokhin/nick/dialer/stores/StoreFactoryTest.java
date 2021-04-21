package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;
import com.mitrokhin.nick.dialer.models.DialerViewBundleSettings;
import com.mitrokhin.nick.dialer.models.MainViewSettings;
import com.mitrokhin.nick.dialer.models.SettingsViewSettings;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class StoreFactoryTest {
    private SharedPreferences sharedPref;
    private Bundle bundle;

    @BeforeEach
    public void setupEach() {
        sharedPref = mock(SharedPreferences.class);
        bundle = mock(Bundle.class);
    }

    @Test
    public void CreateMainStoreTest() {
        Object store = StoreFactory.createShared(MainViewSettings.class, sharedPref);

        assertTrue(store instanceof MainStore);
    }

    @Test
    public void CreateSettingsStoreTest() {
        String[] keys = {"a", "b"};
        Object store = StoreFactory.createShared(SettingsViewSettings.class, sharedPref, keys);

        assertTrue(store instanceof SettingsStore);
    }

    @Test
    public void CreateContactPhonesStoreTest() {
        Object store = StoreFactory.createShared(ContactPhonesSettings.class, sharedPref);

        assertTrue(store instanceof ContactPhonesStore);
    }

    @Test
    public void CreateSharedNullTest() {
        Object store = StoreFactory.createShared(DialerViewBundleSettings.class, sharedPref);

        assertNull(store);
    }

    @Test
    public void CreateDialerBundleStoreTest() {
        Object store = StoreFactory.createBundle(DialerViewBundleSettings.class, bundle);

        assertTrue(store instanceof DialerBundleStore);
    }

    @Test
    public void CreateBundleNullTest() {
        Object store = StoreFactory.createBundle(ContactPhonesSettings.class, bundle);

        assertNull(store);
    }
}
