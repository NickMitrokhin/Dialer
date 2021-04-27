package com.mitrokhin.nick.dialer.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.mitrokhin.nick.dialer.infrastructure.DataProvider;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Contacts extends AndroidViewModel {
    private MutableLiveData<List<ContactItem>> contacts;

    public Contacts(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ContactItem>> getContacts() {
        if(contacts == null) {
            contacts = new MutableLiveData<>();
            loadContacts();
        }

        return contacts;
    }

    private void loadContacts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            DataProvider provider = new DataProvider(getApplication());
            List<ContactItem> items = provider.getContacts();
            contacts.postValue(items);
        });
        executor.shutdown();
    }
}
