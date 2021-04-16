package com.mitrokhin.nick.dialer.presenters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.mitrokhin.nick.dialer.DialerActivity;
import com.mitrokhin.nick.dialer.R;
import com.mitrokhin.nick.dialer.infrastructure.Permissions;
import com.mitrokhin.nick.dialer.models.ContactPhones;
import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;
import com.mitrokhin.nick.dialer.views.IContactPhonesView;


public class ContactPhonesPresenter extends Presenter<ContactPhonesSettings, IContactPhonesView> {
    private final String[] PHONE_CALL_PERMISSIONS = new String[] {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_CALL_LOG
    };
    private ContactPhones model;
    private String phoneNumber;

    public ContactPhonesPresenter(IContactPhonesView view) {
        super(view, ContactPhonesSettings.class);

        initModel();
        initViewTitle();
    }

    private void initModel() {
        model = ViewModelProviders.of((FragmentActivity)view.getViewContext()).get(ContactPhones.class);
    }

    private void initViewTitle() {
        ContactPhonesSettings settings = getSettings();

        getView().setViewTitle(settings.getContactName());
    }

    public void phonesListReady() {
        loadItems();
    }

    private ContactPhonesSettings getSettings() {
        ContactPhonesSettings settings = getView().getSettings();
        String contactID = settings.getContactID();
        String contactName = settings.getContactName();

        if(contactID == null || contactName == null) {
            settings = settingsStorage.getSettings();
        }

        return settings;
    }

    private void loadItems() {
        IContactPhonesView currentView = getView();

        currentView.toggleProgressVisibility(true);

        ContactPhonesSettings settings = getSettings();
        String contactID = settings.getContactID();

        model.getPhonesByContactID(contactID).observe((LifecycleOwner)view.getViewContext(), phones -> {
            currentView.toggleProgressVisibility(false);
            currentView.setListItems(phones);
        });
    }

    private void sendPhoneNumberViaSMS() {
        String bodySMS = String.format("%s: %s", getSettings().getContactName(), phoneNumber);
        Intent sendSMSIntent = new Intent(Intent.ACTION_VIEW);
        Context context = view.getViewContext();

        sendSMSIntent.putExtra("sms_body", bodySMS);
        sendSMSIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(sendSMSIntent);
    }

    public void contextItemSelected(int menuItemID, int listItemPosition) {
        IContactPhonesView currentView = getView();

        if(menuItemID == R.id.send_sms) {
            phoneNumber = currentView.getPhoneNumberByPosition(listItemPosition);
            if(permissionsGranted(new String[] { Manifest.permission.SEND_SMS })) {
                sendPhoneNumberViaSMS();
            } else {
                currentView.requestViewPermissions(new String[] { Manifest.permission.SEND_SMS }, Permissions.SEND_SMS);
            }
        }
    }

    private boolean permissionsGranted(String[] permissions) {
        boolean result = true;

        for(int i = 0; i < permissions.length; i++) {
            if(getView().checkPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }

        return result;
    }

    private boolean isRequestPermissionGranted(int[] grantResults) {
        boolean result = true;

        for(int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }

        return result;
    }

    private void startCalling() {
        Context context = getView().getViewContext();
        Intent intent = new Intent(context, DialerActivity.class);

        intent.putExtra(context.getString(R.string.par_id), phoneNumber);
        context.startActivity(intent);
    }

    public void requestPermissionsResult(int requestCode, int[] grantResults) {
        switch(requestCode) {
            case Permissions.SEND_SMS: {
                if(isRequestPermissionGranted(grantResults)) {
                    sendPhoneNumberViaSMS();
                }
                break;
            }
            case Permissions.CALL_PHONE: {
                if(isRequestPermissionGranted(grantResults)) {
                    startCalling();
                }
                break;
            }
        }
    }

    public void listItemClick(String item) {
        phoneNumber = item;
        if(permissionsGranted(PHONE_CALL_PERMISSIONS)) {
            startCalling();
        } else {
            getView().requestViewPermissions(PHONE_CALL_PERMISSIONS, Permissions.CALL_PHONE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        model = null;
    }
}
