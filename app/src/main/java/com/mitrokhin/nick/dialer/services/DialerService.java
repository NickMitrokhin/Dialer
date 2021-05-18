package com.mitrokhin.nick.dialer.services;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.mitrokhin.nick.dialer.R;
import com.mitrokhin.nick.dialer.infrastructure.PhoneCallReceiver;

public class DialerService extends Service
        implements PhoneCallReceiver.ChangeListener {
    public class ServiceBinder extends Binder {
        public DialerService getService() {
            return DialerService.this;
        }
    }

    private final IBinder binder;
    private PhoneCallReceiver receiver;
    private String phoneNumber;
    private PendingIntent persistentIntent;

    public DialerService() {
        binder = new ServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        loadParameters(intent);
        createReceiver();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unbindCore();
        return super.onUnbind(intent);
    }

    @Override
    public void onChange() {
        try {
            persistentIntent.send();
        } catch(Exception e) {
        }
    }

    private void loadParameters(@NonNull Intent intent) {
        phoneNumber = intent.getStringExtra(getString(R.string.calling_phone_no));
        persistentIntent = intent.getParcelableExtra(getString(R.string.pers_intent));
    }

    private void createReceiver() {
        receiver = new PhoneCallReceiver(phoneNumber);
        IntentFilter intFilter = new IntentFilter();
        intFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        intFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(receiver, intFilter);
    }

    private void unbindCore() {
        if(receiver != null) {
            persistentIntent = null;
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public void enableListeningStatus(boolean flag) {
        if(receiver != null) {
            receiver.setStatusListener(flag ? this : null);
        }
    }

    @SuppressLint("MissingPermission")
    public void dial(Context context) {
        receiver.resetStatus();

        Intent callIntent = new Intent(Intent.ACTION_CALL);

        callIntent.setData(Uri.parse(context.getString(R.string.phone_no_prefix) + phoneNumber));
        context.startActivity(callIntent);
    }

    public PhoneCallReceiver.PhoneCallStatus getCallStatus() {
        return (receiver != null) ? receiver.getStatus() : PhoneCallReceiver.PhoneCallStatus.IDLE;
    }
}
