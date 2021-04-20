package com.mitrokhin.nick.dialer;


import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mitrokhin.nick.dialer.databinding.ActivityContactPhonesBinding;
import com.mitrokhin.nick.dialer.infrastructure.PhoneAdapter;
import com.mitrokhin.nick.dialer.models.ContactPhonesSettings;
import com.mitrokhin.nick.dialer.presenters.ContactPhonesPresenter;
import com.mitrokhin.nick.dialer.views.AppActivity;
import com.mitrokhin.nick.dialer.views.IContactPhonesView;

import java.util.ArrayList;
import java.util.List;


public class ContactPhonesActivity extends AppActivity
        implements AdapterView.OnItemClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        IContactPhonesView {
    private ActivityContactPhonesBinding binding;
    private ContactPhonesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolbar();
        initActionBar();
        initPhoneList();
        initPresenter();
        phonesListReady();
    }

    private void initBinding() {
        binding = ActivityContactPhonesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initPhoneList() {
        PhoneAdapter adapter = new PhoneAdapter(this, R.layout.phone_listview_item, new ArrayList<>());
        binding.content.phonesList.setAdapter(adapter);
        binding.content.phonesList.setOnItemClickListener(this);
        registerForContextMenu(binding.content.phonesList);
    }

    private void initPresenter() {
        presenter = new ContactPhonesPresenter(this);
    }

    private void phonesListReady() {
        presenter.phonesListReady();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.phone_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        presenter.contextItemSelected(item.getItemId(), info.position);
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        presenter.saveSettings();
        presenter.dispose();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String item = (String)adapterView.getAdapter().getItem(position);

        presenter.listItemClick(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        presenter.requestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void applySettings(ContactPhonesSettings settings) {
    }

    @Override
    public ContactPhonesSettings getSettings() {
        Intent intent = getIntent();

        String contactID = intent.getStringExtra(getString(R.string.par_id));
        String contactName = intent.getStringExtra(getString(R.string.par_name));

        return new ContactPhonesSettings(contactID, contactName);
    }

    @Override
    public void toggleProgressVisibility(boolean visible) {
        binding.content.progress.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setListItems(List<String> items) {
        PhoneAdapter adapter = (PhoneAdapter)binding.content.phonesList.getAdapter();

        adapter.clear();
        adapter.addAll(items);
    }

    @Override
    public void setViewTitle(String title) {
        setTitle(title);
    }

    @Override
    public String getPhoneNumberByPosition(int position) {
        return (String)binding.content.phonesList.getAdapter().getItem(position);
    }

    @Override
    public int checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }

    @Override
    public void requestViewPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }
}