package com.mitrokhin.nick.dialer.infrastructure;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;


public class PhoneCallReceiver extends BroadcastReceiver {
    public interface ChangeListener {
        void onChange();
    }

    public enum PhoneCallStatus {
        IDLE, OUTGOING, INCOMING, OUT_ENDED, IN_ENDED, ACCEPTED
    }

    private String phoneNumber;
    private long startPhoneCallTime = 0;
    private PhoneCallStatus phoneCallStatus = PhoneCallStatus.IDLE;
    private Context context;
    private ChangeListener statusListener;

    public PhoneCallReceiver(String phoneNumber) {
        super();
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(this.context == null) {
            this.context = context;
        }

        receiveCore(intent);
    }

    public void setStatusListener(ChangeListener statusListener) {
        this.statusListener = statusListener;
    }

    protected void fireStatusChange() {
        if(statusListener != null) {
            statusListener.onChange();
        }
    }

    private void receiveCore(Intent intent) {
        String currentAction = intent.getAction();
        if(currentAction.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.i("dialer", "new outgoing");
            String callingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);//"*100#";
            if(callingNumber.equals(phoneNumber)) {
                Log.i("dialer", "outgoing number");
                phoneCallStatus = PhoneCallStatus.OUTGOING;
            }
            return;
        }
        if(currentAction.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.i("dialer", "ringing");
                String incomingNo = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if(phoneCallStatus != PhoneCallStatus.OUTGOING && incomingNo.equals(phoneNumber)) {
                    phoneCallStatus = PhoneCallStatus.INCOMING;
                    fireStatusChange();
                }
            } else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Log.i("dialer", "offhook");
                if(phoneCallStatus == PhoneCallStatus.INCOMING) {
                    phoneCallStatus = PhoneCallStatus.ACCEPTED;
                    fireStatusChange();
                }
            } else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if(phoneCallStatus == PhoneCallStatus.INCOMING) {
                    Log.i("dialer", "idle incoming");
                    phoneCallStatus = PhoneCallStatus.IN_ENDED;
                    fireStatusChange();
                } else if(phoneCallStatus == PhoneCallStatus.OUTGOING) {
                    Log.i("dialer", "idle outgoing");
                    phoneCallStatus = PhoneCallStatus.OUT_ENDED;
                    fireStatusChange();
                }
            }
        }
    }

    public void resetStatus() {
        if(phoneCallStatus != PhoneCallStatus.IDLE) {
            phoneCallStatus = PhoneCallStatus.IDLE;
            fireStatusChange();
        }
        startPhoneCallTime = new Date().getTime();
    }

    private boolean callWasAccepted() {
        boolean result = false;
        if(context != null) {
            PhoneCallInfoProvider provider = new PhoneCallInfoProvider(context, phoneNumber, phoneCallStatus, startPhoneCallTime);
            result = provider.getCallDuration() > 0;
        }
        return result;
    }

    public PhoneCallStatus getStatus() {
        PhoneCallStatus result = phoneCallStatus;
        if(result != PhoneCallStatus.ACCEPTED && callWasAccepted()) {
            result = PhoneCallStatus.ACCEPTED;
        }
        return result;
    }
}

class PhoneCallInfoProvider {
    private Context context;
    private String phoneNumber;
    private PhoneCallReceiver.PhoneCallStatus phoneCallStatus;
    private long startPhoneCallTime;

    PhoneCallInfoProvider(Context context, String phoneNumber, PhoneCallReceiver.PhoneCallStatus phoneCallStatus, long startPhoneCallTime) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.phoneCallStatus = phoneCallStatus;
        this.startPhoneCallTime = startPhoneCallTime;
    }

    private String getFilterExpression() {
        return CallLog.Calls.TYPE + "=" + (phoneCallStatus == PhoneCallReceiver.PhoneCallStatus.OUT_ENDED ? CallLog.Calls.OUTGOING_TYPE : CallLog.Calls.INCOMING_TYPE) +
                " and " + CallLog.Calls.NUMBER + "='" + phoneNumber + "'" +
                " and " + CallLog.Calls.DATE + ">=" + String.valueOf(startPhoneCallTime);
    }

    @SuppressLint("MissingPermission")
    int getCallDuration() {
        Cursor cursor = null;
        int result = 0;
        try {
            String filterExpression = getFilterExpression();
            cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                    filterExpression,
                    null, CallLog.Calls.DATE + " desc");
            if(cursor != null && cursor.moveToFirst()) {
                result = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            }
        } catch(Exception e) {
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}