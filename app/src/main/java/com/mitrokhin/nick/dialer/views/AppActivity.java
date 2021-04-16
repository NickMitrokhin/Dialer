package com.mitrokhin.nick.dialer.views;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class AppActivity extends AppCompatActivity
        implements IView {
    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public SharedPreferences getSharedStore() {
        return getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public void exit() {
        finish();
    }
}
