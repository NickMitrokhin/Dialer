package com.mitrokhin.nick.dialer.views;

public interface IExtView<T> extends IView {
    T getSettings();
    void applySettings(T settings);
}
