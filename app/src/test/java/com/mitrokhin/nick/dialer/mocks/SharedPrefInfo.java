package com.mitrokhin.nick.dialer.mocks;

import android.content.SharedPreferences;

import static org.mockito.Mockito.*;

public class SharedPrefInfo {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public SharedPrefInfo() {
        sharedPref = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);

        when(sharedPref.edit()).thenReturn(editor);
    }

    public SharedPreferences getSharedPref() {
        return sharedPref;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
