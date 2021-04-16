package com.mitrokhin.nick.dialer.views;

import android.content.Context;
import android.content.SharedPreferences;

public interface IView {
    Context getViewContext();
    SharedPreferences getSharedStore();
    void exit();
}
