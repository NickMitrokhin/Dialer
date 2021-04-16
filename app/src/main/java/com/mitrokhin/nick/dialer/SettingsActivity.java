package com.mitrokhin.nick.dialer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import android.text.InputFilter;

import com.mitrokhin.nick.dialer.databinding.ActivitySettingsBinding;
import com.mitrokhin.nick.dialer.infrastructure.InputFilterMinMax;
import com.mitrokhin.nick.dialer.models.SettingsViewSettings;
import com.mitrokhin.nick.dialer.presenters.SettingsPresenter;
import com.mitrokhin.nick.dialer.views.AppActivity;
import com.mitrokhin.nick.dialer.views.ISettingsView;


public class SettingsActivity extends AppActivity
        implements ISettingsView {
    private ActivitySettingsBinding binding;
    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolbar();
        initActionBar();
        initEditors();
        initPresenter();
    }

    private void initBinding() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initEditors() {
        binding.content.etDialCount.setFilters(new InputFilter[] { new InputFilterMinMax(1, 20) });
        binding.content.etTimeout.setFilters(new InputFilter[] { new InputFilterMinMax(1, 30) });
    }

    private void initPresenter() {
        presenter = new SettingsPresenter(this);
    }

    @Override
    public SharedPreferences getSharedStore() {
        return getSharedPreferences(getString(R.string.shared_storage), MODE_PRIVATE);
    }

    @Override
    public SettingsViewSettings getSettings() {
        int attemptsCount = Integer.parseInt(binding.content.etDialCount.getText().toString()),
            dialTimeout = Integer.parseInt(binding.content.etTimeout.getText().toString());

        return new SettingsViewSettings(attemptsCount, dialTimeout);
    }

    @Override
    public void applySettings(SettingsViewSettings settings) {
        binding.content.etDialCount.setText(Integer.toString(settings.getAttemptCount()));
        binding.content.etTimeout.setText(Integer.toString(settings.getDialTimeout()));
    }

    @Override
    public String getAttemptCountKey() {
        return getString(R.string.dial_attempt_count);
    }

    @Override
    public String getDialCountKey() {
        return getString(R.string.dial_timeout);
    }

    @Override
    public void onPause() {
        presenter.saveSettings();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }
}
