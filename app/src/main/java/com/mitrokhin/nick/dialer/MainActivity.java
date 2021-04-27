package com.mitrokhin.nick.dialer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mitrokhin.nick.dialer.models.MainViewSettings;
import com.mitrokhin.nick.dialer.views.AppActivity;
import com.mitrokhin.nick.dialer.databinding.ActivityMainBinding;
import com.mitrokhin.nick.dialer.infrastructure.ContactAdapter;
import com.mitrokhin.nick.dialer.models.ContactItem;
import com.mitrokhin.nick.dialer.infrastructure.Permissions;
import com.mitrokhin.nick.dialer.presenters.MainPresenter;
import com.mitrokhin.nick.dialer.views.IMainView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            TextWatcher,
            IMainView,
            ActivityCompat.OnRequestPermissionsResultCallback {
    private ActivityMainBinding binding;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolbar();
        initDrawer();
        initNavigationView();
        initSearch();
        initContactListView();
        initPresenter();
        contactListReady();
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initToolbar() {
        setSupportActionBar(binding.appBar.toolbar);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void initContactListView() {
        ContactAdapter adapter = new ContactAdapter(this, R.layout.contact_listview_item, new ArrayList<>());
        binding.appBar.content.contactList.setAdapter(adapter);
        binding.appBar.content.contactList.setOnItemClickListener((adapterView, view, position, id) -> {
            ContactItem item = (ContactItem)adapterView.getAdapter().getItem(position);
            presenter.listItemClick(item);
        });
    }

    private void contactListReady() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            presenter.contactListReady();
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_CONTACTS }, Permissions.READ_CONTACTS);
        }
    }

    private void initSearch() {
        binding.appBar.content.etSearch.addTextChangedListener(this);
    }

    private void initPresenter() {
        presenter = new MainPresenter(this);
    }

    @Override
    public void setAppVersion(String version) {
        TextView titleView = binding.navView.getHeaderView(0).findViewById(R.id.titleView);
        titleView.setText(titleView.getText() + "  v" + version);
    }

    @Override
    public void applySettings(MainViewSettings settings) {
        binding.appBar.content.contactList.setSelection(settings.getPosition());

        EditText etSearch = binding.appBar.content.etSearch;
        etSearch.setText(settings.getSearchValue());
        etSearch.setVisibility(settings.getVisible() ? View.VISIBLE : View.GONE);
    }

    @Override
    public MainViewSettings getSettings() {
        int position = binding.appBar.content.contactList.getFirstVisiblePosition();

        EditText etSearch = binding.appBar.content.etSearch;
        String searchValue = etSearch.getText().toString();
        boolean visible = etSearch.getVisibility() == View.VISIBLE;

        return new MainViewSettings(position, searchValue, visible);
    }

    @Override
    protected void onDestroy() {
        presenter.saveSettings();
        presenter.dispose();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavigationMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        presenter.menuItemSelected(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateSearchEditor() {
        EditText etSearch = binding.appBar.content.etSearch;
        if(etSearch.getVisibility() == View.GONE) {
            etSearch.setVisibility(View.VISIBLE);
        } else {
            etSearch.setText(null);
            etSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        return presenter.navigationItemSelected(id);
    }

    @Override
    public void closeNavigationMenu() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void toggleProgressVisibility(boolean visible) {
        binding.appBar.content.progress.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setListItems(List<ContactItem> items) {
        ContactAdapter adapter = (ContactAdapter)binding.appBar.content.contactList.getAdapter();
        adapter.clearAll();
        adapter.addAll(items);
    }

    @Override
    public void filterList(String filterValue) {
        ContactAdapter adapter = (ContactAdapter)binding.appBar.content.contactList.getAdapter();
        adapter.getFilter().filter(filterValue);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.searchTextChanged(s.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == Permissions.READ_CONTACTS) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.contactListReady();
            }
        }
    }
}
