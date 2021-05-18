package com.mitrokhin.nick.dialer.presenters;

import com.mitrokhin.nick.dialer.models.SettingsViewSettings;
import com.mitrokhin.nick.dialer.views.ISettingsView;


public class SettingsPresenter extends Presenter<SettingsViewSettings, ISettingsView> {
    public SettingsPresenter(ISettingsView view) {
        super(view, SettingsViewSettings.class);

        applySettings();
    }

    @Override
    protected String[] getSettingsStorageExtraArgs() {
        ISettingsView currentView = getView();

        return new String[] { currentView.getAttemptCountKey(), currentView.getDialCountKey() };
    }
}
