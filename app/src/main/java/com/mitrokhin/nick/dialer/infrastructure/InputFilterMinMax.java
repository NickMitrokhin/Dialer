package com.mitrokhin.nick.dialer.infrastructure;

import android.text.InputFilter;
import android.text.Spanned;


public class InputFilterMinMax implements InputFilter {
    private int min, max;

    public InputFilterMinMax(int min, int max) {
        if(min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if(isInRange(input)) {
                return null;
            }
        } catch(NumberFormatException e) {
        }
        return "";
    }

    private boolean isInRange(int input) {
        return input >= min && input <= max;
    }
}
