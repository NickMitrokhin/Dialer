package com.mitrokhin.nick.dialer.views;

import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;

import java.util.List;


public interface IContactPhonesView extends IExtView<ContactPhonesSettings> {
    void toggleProgressVisibility(boolean visible);
    void setListItems(List<String> items);
    void setViewTitle(String title);
    String getPhoneNumberByPosition(int position);
    int checkPermission(String permission);
    void requestViewPermissions(String[] permissions, int requestCode);
}
