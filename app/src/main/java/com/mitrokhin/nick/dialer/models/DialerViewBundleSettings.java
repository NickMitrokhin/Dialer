package com.mitrokhin.nick.dialer.models;

public class DialerViewBundleSettings {
    private int dialCount;
    private long dialTimeout;
    private String dialStatus;

    public DialerViewBundleSettings(int dialCount, long dialTimeout, String dialStatus) {
        this.dialCount = dialCount;
        this.dialTimeout = dialTimeout;
        this.dialStatus = dialStatus;
    }

    public int getDialCount() {
        return dialCount;
    }
    public void setDialCount(int value) { dialCount = value; }

    public long getDialTimeout() {
        return dialTimeout;
    }
    public void setDialTimeout(long value) { dialTimeout = value; }

    public String getDialStatus() {
        return dialStatus;
    }
    public void setDialStatus(String value) { dialStatus = value; }
}
