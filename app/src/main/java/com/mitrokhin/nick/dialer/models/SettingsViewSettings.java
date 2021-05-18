package com.mitrokhin.nick.dialer.models;


public class SettingsViewSettings {
    private final int attemptCount;
    private final int dialTimeout;

    public SettingsViewSettings(int attemptCount, int dialTimeout) {
        this.attemptCount = attemptCount;
        this.dialTimeout = dialTimeout;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public int getDialTimeout() {
        return dialTimeout;
    }
}
