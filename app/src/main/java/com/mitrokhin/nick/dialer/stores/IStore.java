package com.mitrokhin.nick.dialer.stores;

public interface IStore<T> {
    T getSettings();
    void setSettings(T settings);
}
