package com.mitrokhin.nick.dialer.stores;

import android.os.Bundle;

import com.mitrokhin.nick.dialer.models.DialerViewBundleSettings;


public class DialerBundleStore extends BundleStore<DialerViewBundleSettings> {
    private final String CURRENT_DIALER_COUNT = "currentDialerCount";
    private final String CURRENT_TIMEOUT = "currentTimeout";
    private final String CURRENT_DIAL_STATUS = "currentDialStatus";

    public DialerBundleStore(Bundle bundle) {
        super(bundle);
    }

    @Override
    public DialerViewBundleSettings getSettings() {
        int dialerCount = 0;
        long dialTimeout = 0;
        String dialStatus = null;

        if(bundle != null) {
            if(bundle.containsKey(CURRENT_DIALER_COUNT)) {
                dialerCount = bundle.getInt(CURRENT_DIALER_COUNT);
            }
            if(bundle.containsKey(CURRENT_TIMEOUT)) {
                dialTimeout = bundle.getLong(CURRENT_TIMEOUT);
            }
            if(bundle.containsKey(CURRENT_DIAL_STATUS)) {
                dialStatus = bundle.getString(CURRENT_DIAL_STATUS);
            }
        }

        return new DialerViewBundleSettings(dialerCount, dialTimeout, dialStatus);
    }

    @Override
    public void setSettings(DialerViewBundleSettings settings) {
        if(bundle == null) {
            return;
        }

        if(settings.getDialCount() > 0) {
            bundle.putInt(CURRENT_DIALER_COUNT, settings.getDialCount());
        } else {
            bundle.remove(CURRENT_DIALER_COUNT);
        }

        if(settings.getDialTimeout() > 0) {
            bundle.putLong(CURRENT_TIMEOUT, settings.getDialTimeout());
        } else {
            bundle.remove(CURRENT_TIMEOUT);
        }

        if(settings.getDialStatus() == null || settings.getDialStatus().isEmpty()) {
            bundle.remove(CURRENT_DIAL_STATUS);
        } else {
            bundle.putString(CURRENT_DIAL_STATUS, settings.getDialStatus());
        }
    }
}
