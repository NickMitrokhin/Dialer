package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;
import android.os.Bundle;


public class StoreFactory {
    @SuppressWarnings("unchecked")
    public static <T> IStore<T> createShared(Class<T> settingsClass, SharedPreferences sharedPref, String... args) {
        String className = settingsClass.getSimpleName();
        IStore<T> result = null;

        switch(className) {
            case "MainViewSettings": {
                result = (IStore<T>)(new MainStore(sharedPref));
                break;
            }

            case "SettingsViewSettings": {
                result = (IStore<T>)(new SettingsStore(sharedPref, args));
                break;
            }

            case "ContactPhonesSettings": {
                result = (IStore<T>)(new ContactPhonesStore(sharedPref));
                break;
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static<T> IStore<T> createBundle(Class<T> settingsClass, Bundle bundle) {
        String className = settingsClass.getSimpleName();
        IStore<T> result = null;

        if(className.equals("DialerViewBundleSettings")) {
            result = (IStore<T>)(new DialerBundleStore(bundle));
        }

        return result;
    }
}
