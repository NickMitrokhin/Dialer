package com.mitrokhin.nick.dialer.stores;

import android.os.Bundle;

public abstract class BundleStore<T> implements IStore<T> {
    protected Bundle bundle;

    public BundleStore(Bundle bundle) {
        this.bundle = bundle;
    }
}
