package com.mitrokhin.nick.dialer.infrastructure;

import android.os.CountDownTimer;


public class Timer extends CountDownTimer {
    public interface TimerListener {
        void onTimer(long secUntilFinished);

        void onTimerFinished();
    }

    private TimerListener listener;
    private long startTime;

    public Timer(long startTimeSec, long intervalSec, TimerListener listener) {
        super(startTimeSec * 1000, intervalSec * 1000);
        this.startTime = startTimeSec;
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        listener.onTimer(millisUntilFinished / 1000);
    }

    @Override
    public void onFinish() {
        listener.onTimerFinished();
    }

    public long getStartTime() {
        return startTime;
    }
}
