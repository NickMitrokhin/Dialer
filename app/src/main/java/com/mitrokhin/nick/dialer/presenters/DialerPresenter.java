package com.mitrokhin.nick.dialer.presenters;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.mitrokhin.nick.dialer.R;
import com.mitrokhin.nick.dialer.infrastructure.PhoneCallReceiver;
import com.mitrokhin.nick.dialer.infrastructure.Timer;
import com.mitrokhin.nick.dialer.models.DialerViewBundleSettings;
import com.mitrokhin.nick.dialer.models.SettingsViewSettings;
import com.mitrokhin.nick.dialer.services.DialerService;
import com.mitrokhin.nick.dialer.stores.DialerBundleStore;
import com.mitrokhin.nick.dialer.stores.StoreFactory;
import com.mitrokhin.nick.dialer.views.IDialerView;


public class DialerPresenter extends Presenter<SettingsViewSettings, IDialerView>
        implements ServiceConnection, Timer.TimerListener  {
    private SettingsViewSettings settings;
    private DialerViewBundleSettings currentSettings;
    private DialerService dialerService;
    private Timer countDownTimer;
    private String phoneNumber;

    public DialerPresenter(IDialerView view) {
        super(view, SettingsViewSettings.class);

        loadSettings();
    }

    private void loadSettings() {
        Context context = getView().getViewContext();

        phoneNumber = getView().getViewIntent().getStringExtra(context.getString(R.string.par_id));
        settings = settingsStorage.getSettings();
    }

    public void restoreCurrentSettings(Bundle bundle) {
        DialerBundleStore store = (DialerBundleStore)StoreFactory.createBundle(DialerViewBundleSettings.class, bundle);

        currentSettings = store.getSettings();
    }

    public void saveCurrentSettings(Bundle bundle) {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        if(dialerService != null) {
            dialerService.enableListeningStatus(false);
        }

        DialerBundleStore store = (DialerBundleStore)StoreFactory.createBundle(DialerViewBundleSettings.class, bundle);

        if(dialerService != null) {
            currentSettings.setDialStatus(dialerService.getCallStatus().toString());
        }
        store.setSettings(currentSettings);
    }

    public void ready() {
        if(currentSettings.getDialCount() == 0) {
            currentSettings.setDialCount(settings.getAttemptCount());
        }

        getView().updateAttempts(currentSettings.getDialCount());
        startService();
    }

    public void start() {
        checkServiceStatus();
        if(countDownTimer == null && getView().isIndicatorTimeoutVisible()) {
            restartTimer();
        }
    }

    public void stop() {
        resetTimer();
    }

    private void reduceCurrentDialCount() {
        int currentDialCount = currentSettings.getDialCount();

        if(dialerService != null && currentSettings.getDialTimeout() == 0 && currentDialCount > 0) {
            currentSettings.setDialCount(--currentDialCount);
        }
    }

    private void startService() {
        if(dialerService == null) {
            Context context = getView().getViewContext();
            Intent intent = new Intent(context, DialerService.class);
            PendingIntent pi = getView().getPendingIntent(0, intent, 0);
            intent.putExtra(context.getString(R.string.calling_phone_no), phoneNumber)
                    .putExtra(context.getString(R.string.pers_intent), pi);
            context.bindService(intent, this, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected String[] getSettingsStorageExtraArgs() {
        return new String[] { getView().getAttemptCountKey(), getView().getDialCountKey() };
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
        dialerService = ((DialerService.ServiceBinder)binder).getService();
        serviceConnectedCore();
    }

    private void serviceConnectedCore() {
        long currentDialTimeout = currentSettings.getDialTimeout();
        String currentDialStatus = currentSettings.getDialStatus();

        if(currentDialTimeout == 0 && currentDialStatus == null) {
            reduceCurrentDialCount();
            startPhoneCall();
        } else {
            //Log.v("MyDialer", "currentTimeout = " + Long.toString(currentTimeout) + ", currentDialStatus = " + currentDialStatus.toString());
            boolean isOutgoingCall = PhoneCallReceiver.PhoneCallStatus.valueOf(currentDialStatus) == PhoneCallReceiver.PhoneCallStatus.OUTGOING;
            boolean isIncomingCall = PhoneCallReceiver.PhoneCallStatus.valueOf(currentDialStatus) == PhoneCallReceiver.PhoneCallStatus.INCOMING;
            if(currentDialTimeout == 0 && (isOutgoingCall || isIncomingCall)) {
                dialerService.enableListeningStatus(true);
            } else {
                restartTimer();
            }
        }
    }

    private void startPhoneCall() {
        if(dialerService != null) {
            dialerService.dial(getView().getViewContext());
        }
    }

    private void checkServiceStatus() {
        if(dialerService != null) {
            PhoneCallReceiver.PhoneCallStatus callResult = dialerService.getCallStatus();
            if(callResult == PhoneCallReceiver.PhoneCallStatus.ACCEPTED) {
                getView().exit();
            } else if(callResult == PhoneCallReceiver.PhoneCallStatus.OUT_ENDED
                    || callResult == PhoneCallReceiver.PhoneCallStatus.IN_ENDED) {
                restartTimer();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName arg) {
        dialerService = null;
    }

    private void initTimer() {
        long currentDialTimeout = currentSettings.getDialTimeout();
        int dialTimeout = settings.getDialTimeout();
        long timeoutDial = currentDialTimeout == 0 ? dialTimeout : currentDialTimeout;
        getView().initIndicatorTimeout(dialTimeout, (int)timeoutDial);
        countDownTimer = new Timer(timeoutDial, 1, this);
        countDownTimer.start();
    }

    private void restartTimer() {
        if(countDownTimer == null) {
            int currentDialCount = currentSettings.getDialCount();

            if(currentDialCount > 0) {
                getView().updateAttempts(currentDialCount);
                initTimer();
            } else {
                getView().exit();
            }
        }
    }

    private void resetTimer() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onTimer(long secFinished) {
        currentSettings.setDialTimeout(secFinished);
        getView().updateIndicatorTimeout((int)secFinished);
    }

    @Override
    public void onTimerFinished() {
        currentSettings.setDialTimeout(0);
        getView().updateIndicatorTimeout((int)currentSettings.getDialTimeout());
        resetTimer();
        reduceCurrentDialCount();
        startPhoneCall();
    }

    public void pendingResult() {
        checkServiceStatus();
    }

    @Override
    public void dispose() {
        Context context = getView().getViewContext();

        if(dialerService != null) {
            dialerService.enableListeningStatus(false);
            context.unbindService(this);
            dialerService = null;
        }
        resetTimer();
        currentSettings.setDialCount(0);
        currentSettings.setDialTimeout(0);
        phoneNumber = null;

        super.dispose();
    }
}
