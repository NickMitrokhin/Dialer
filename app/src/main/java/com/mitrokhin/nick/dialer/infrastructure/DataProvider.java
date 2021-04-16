package com.mitrokhin.nick.dialer.infrastructure;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;


public final class DataProvider {
    private Context context;

    public DataProvider(@NonNull Context context) {
        this.context = context;
    }

    public List<ContactItem> getContacts() {
        ContentResolver crContacts = context.getContentResolver();
        Cursor curContacts = crContacts.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        ArrayList<ContactItem> result = new ArrayList<>();
        if(curContacts.getCount() > 0) {
            while(curContacts.moveToNext()) {
                String contactID = curContacts.getString(curContacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = curContacts.getString(curContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                boolean contactHasPhoneNumbers = Integer.parseInt(curContacts.getString(curContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0;
                if(contactHasPhoneNumbers) {
                    result.add(new ContactItem(contactID, contactName));
                }
            }
        }
        curContacts.close();
        return result;
    }

    public Bitmap getPhotoByContactID(String contactID) {
        Bitmap result = null;
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactID));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        ContentResolver crContacts = context.getContentResolver();
        Cursor curPhotos = crContacts.query(photoUri,
                new String[] { ContactsContract.CommonDataKinds.Photo.PHOTO },
                null, null, null);
        if(curPhotos != null) {
            try {
                if(curPhotos.moveToNext()) {
                    byte[] photoBytes = curPhotos.getBlob(0);
                    if(photoBytes != null) {
                        result = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                    }
                }
            } finally {
                curPhotos.close();
            }
        }
        return result;
    }

    private static String getSanitizedPhoneNumber(String phoneNo) {
        return phoneNo != null ? phoneNo.replaceAll("[()-]", "").replace(" ", "") : phoneNo;
    }

    public List<String> getPhonesByContactID(String contactID) {
        ArrayList<String> result = new ArrayList<>();
        ContentResolver crContacts = context.getContentResolver();
        Cursor curPhones = crContacts.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[] { contactID }, null);
        while(curPhones.moveToNext()) {
            String phoneNo = getSanitizedPhoneNumber(curPhones.getString(curPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            if(!result.contains(phoneNo)) {
                result.add(phoneNo);
            }
        }
        curPhones.close();
        return result;
    }
}
