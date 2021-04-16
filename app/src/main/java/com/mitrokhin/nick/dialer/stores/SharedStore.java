package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;


public abstract class SharedStore<T> implements IStore<T> {
    protected SharedPreferences sharedPref;

    public SharedStore(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }
}
