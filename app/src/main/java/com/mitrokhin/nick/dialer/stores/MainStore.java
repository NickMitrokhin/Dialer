package com.mitrokhin.nick.dialer.stores;

import android.content.SharedPreferences;
import com.mitrokhin.nick.dialer.models.MainViewSettings;


public class MainStore extends SharedStore<MainViewSettings> {
    private final String SEARCH_EXPRESSION = "searchExpr";
    private final String CONTACT_LIST_POSITION = "contactPosition";
    private final String SEARCH_VISIBLE = "searchVisible";

    public MainStore(SharedPreferences sharedPref) {
        super(sharedPref);
    }

    @Override
    public MainViewSettings getSettings() {
        int position = sharedPref.getInt(CONTACT_LIST_POSITION, 0);
        String searchValue = sharedPref.getString(SEARCH_EXPRESSION, "");
        boolean visible = sharedPref.getBoolean(SEARCH_VISIBLE, false);

        return new MainViewSettings(position, searchValue, visible);
    }

    @Override
    public void setSettings(MainViewSettings settings) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CONTACT_LIST_POSITION, settings.getPosition())
                .putString(SEARCH_EXPRESSION, settings.getSearchValue())
                .putBoolean(SEARCH_VISIBLE, settings.getVisible())
                .commit();
    }
}
