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


public class ContactPhones extends AndroidViewModel {
    private MutableLiveData<List<String>> phones;

    public ContactPhones(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getPhonesByContactID(String contactID) {
        if(phones == null) {
            phones = new MutableLiveData<>();
            loadPhones(contactID);
        }

        return phones;
    }

    private void loadPhones(String contactID) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            DataProvider provider = new DataProvider(getApplication());
            List<String> items = provider.getPhonesByContactID(contactID);
            phones.postValue(items);
        });
        executor.shutdown();
    }
}
