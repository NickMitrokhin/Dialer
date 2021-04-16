package com.mitrokhin.nick.dialer.presenters;

import com.mitrokhin.nick.dialer.stores.IStore;
import com.mitrokhin.nick.dialer.stores.StoreFactory;
import com.mitrokhin.nick.dialer.views.IExtView;


public abstract class Presenter<T, U extends IExtView<T>> {
    protected IExtView<T> view;
    protected IStore<T> settingsStorage;

    public Presenter(IExtView<T> view, Class<T> settingsClass) {
        attachView(view);
        initStateStorage(settingsClass);
    }

    protected String[] getSettingsStorageExtraArgs() {
        return null;
    }

    protected U getView() {
        return (U)view;
    }

    private void initStateStorage(Class<T> settingsClass) {
        settingsStorage = StoreFactory.createShared(settingsClass, view.getSharedStore(), getSettingsStorageExtraArgs());
    }

    public void attachView(IExtView<T> view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void saveSettings() {
        T settings = view.getSettings();
        settingsStorage.setSettings(settings);
    }

    protected void applySettings() {
        T settings = settingsStorage.getSettings();
        view.applySettings(settings);
    }

    public void dispose() {
        settingsStorage = null;
        detachView();
    }
}
