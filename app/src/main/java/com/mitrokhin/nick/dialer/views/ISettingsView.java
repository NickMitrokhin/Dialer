package com.mitrokhin.nick.dialer.views;

import com.mitrokhin.nick.dialer.models.SettingsViewSettings;


public interface ISettingsView extends IExtView<SettingsViewSettings> {
    String getAttemptCountKey();
    String getDialCountKey();
}
