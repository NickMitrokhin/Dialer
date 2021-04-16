package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.mitrokhin.nick.dialer.models.SettingsViewSettings;


public class SettingsStore extends SharedStore<SettingsViewSettings> {
    private String attemptCountKey;
    private String dialTimeoutKey;

    public SettingsStore(SharedPreferences sharedPref, @NonNull String... args) {
        super(sharedPref);

        this.attemptCountKey = args[0];
        this.dialTimeoutKey = args[1];
    }

    public SettingsViewSettings getSettings() {
        int attemptCount = sharedPref.getInt(attemptCountKey, 1),
                dialTimeout = sharedPref.getInt(dialTimeoutKey, 1);

        return new SettingsViewSettings(attemptCount, dialTimeout);
    }

    public void setSettings(SettingsViewSettings settings) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(attemptCountKey, settings.getAttemptCount())
                .putInt(dialTimeoutKey, settings.getDialTimeout())
                .commit();
    }
}
