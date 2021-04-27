package com.mitrokhin.nick.dialer.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.mitrokhin.nick.dialer.ContactPhonesActivity;
import com.mitrokhin.nick.dialer.R;
import com.mitrokhin.nick.dialer.SettingsActivity;
import com.mitrokhin.nick.dialer.models.ContactItem;
import com.mitrokhin.nick.dialer.models.Contacts;
import com.mitrokhin.nick.dialer.models.MainViewSettings;
import com.mitrokhin.nick.dialer.views.IMainView;


public class MainPresenter extends Presenter<MainViewSettings, IMainView> {
    private Contacts model;

    public MainPresenter(IMainView view) {
        super(view, MainViewSettings.class);

        initModel();
        initPackageVersion();
    }

    private void initModel() {
        model = ViewModelProviders.of((FragmentActivity)view.getViewContext()).get(Contacts.class);
    }

    private String getPackageVersion() {
        String result = "";
        try {
            Context context = view.getViewContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            result = packageInfo.versionName + "." + Integer.toString(packageInfo.versionCode);

        } catch(Exception err) {
        }

        return result;
    }

    private void initPackageVersion() {
        String version = getPackageVersion();
        getView().setAppVersion(version);
    }

    public void listItemClick(ContactItem item) {
        Context context = getView().getViewContext();
        Intent intent = new Intent(context, ContactPhonesActivity.class);
        intent.putExtra(context.getString(R.string.par_id), item.getId());
        intent.putExtra(context.getString(R.string.par_name), item.getName());
        context.startActivity(intent);
    }

    public void contactListReady() {
        loadItems(true);
    }

    private void loadItems(boolean applySettings) {
        IMainView currentView = getView();

        currentView.toggleProgressVisibility(true);
        model.getContacts().observe((LifecycleOwner)view.getViewContext(), contacts -> {
            currentView.toggleProgressVisibility(false);
            currentView.setListItems(contacts);

            if(applySettings) {
                applySettings();
            }
        });
    }

    public void searchTextChanged(String value) {
        getView().filterList(value);
    }

    public boolean navigationItemSelected(int itemId) {
        IMainView currentView = getView();

        switch(itemId) {
            case R.id.nav_settings: {
                Context context = currentView.getViewContext();
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
                break;
            }
            case R.id.nav_exit: {
                currentView.exit();
                break;
            }
        }

        currentView.closeNavigationMenu();
        return true;
    }

    public void menuItemSelected(int itemId) {
        switch(itemId) {
            case R.id.search_contacts: {
                getView().updateSearchEditor();
                break;
            }
            case R.id.reload_contacts: {
                loadItems(false);
                break;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        model = null;
    }
}
