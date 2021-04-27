package com.mitrokhin.nick.dialer.views;

import com.mitrokhin.nick.dialer.models.ContactItem;
import com.mitrokhin.nick.dialer.models.MainViewSettings;
import java.util.List;

public interface IMainView extends IExtView<MainViewSettings> {
    void setAppVersion(String version);
    void toggleProgressVisibility(boolean visible);
    void setListItems(List<ContactItem> items);
    void filterList(String filterValue);
    void closeNavigationMenu();
    void updateSearchEditor();
}
