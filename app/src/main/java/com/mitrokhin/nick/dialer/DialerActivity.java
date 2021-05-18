package com.mitrokhin.nick.dialer;

import android.content.Intent;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import android.view.View;

import com.mitrokhin.nick.dialer.databinding.ActivityDialerBinding;
import com.mitrokhin.nick.dialer.models.SettingsViewSettings;
import com.mitrokhin.nick.dialer.presenters.DialerPresenter;
import com.mitrokhin.nick.dialer.views.AppActivity;
import com.mitrokhin.nick.dialer.views.IDialerView;

public class DialerActivity extends AppActivity
        implements IDialerView {
    private ActivityDialerBinding binding;
    private DialerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initToolbar();
        initActionBar();
        initPresenter(savedInstanceState);
    }

    private void initBinding() {
        binding = ActivityDialerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar == null) {
            return;
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initPresenter(Bundle savedInstanceState) {
        presenter = new DialerPresenter(this);

        presenter.restoreCurrentSettings(savedInstanceState);
        presenter.ready();
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
    public PendingIntent getPendingIntent(int requestCode, Intent intent, int flags) {
        return createPendingResult(0, intent, 0);
    }

    @Override
    public Intent getViewIntent() {
        return getIntent();
    }

    @Override
    public SettingsViewSettings getSettings() {
        return null;
    }

    @Override
    public void applySettings(SettingsViewSettings settings) { }

    @Override
    public SharedPreferences getSharedStore() {
        return getSharedPreferences(getString(R.string.shared_storage), MODE_PRIVATE);
    }

    @Override
    public boolean isIndicatorTimeoutVisible() {
        return binding.content.indicatorTimeout.getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.start();
    }


    @Override
    protected void onStop() {
        presenter.stop();

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        presenter.saveCurrentSettings(savedInstanceState);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void initIndicatorTimeout(int maxTimeout, int timeout) {
        binding.content.indicatorTimeout.beginUpdate();
        binding.content.indicatorTimeout.setMax(maxTimeout);
        binding.content.indicatorTimeout.setValue(timeout);
        binding.content.indicatorTimeout.setVisibility(View.VISIBLE);
        binding.content.indicatorTimeout.endUpdate();
    }

    @Override
    public void updateAttempts(int attemptCount) {
        String txt = getString(R.string.text_attempts_prefix) + attemptCount;
        binding.content.txtAttemptCount.setText(txt);
    }

    @Override
    public void updateIndicatorTimeout(int timeout) {
        binding.content.indicatorTimeout.setValue(timeout);
    }

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.pendingResult();
    }
}
