package com.mitrokhin.nick.dialer.views;

import android.app.PendingIntent;
import android.content.Intent;

import com.mitrokhin.nick.dialer.models.SettingsViewSettings;


public interface IDialerView extends ISettingsView {
    void updateAttempts(int attemptCount);
    PendingIntent getPendingIntent(int requestCode, Intent intent, int flags);
    void initIndicatorTimeout(int maxTimeout, int timeout);
    void updateIndicatorTimeout(int timeout);
    boolean isIndicatorTimeoutVisible();
    Intent getViewIntent();
}
