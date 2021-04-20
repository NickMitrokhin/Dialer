package com.mitrokhin.nick.dialer.mocks;

import android.text.SpannedString;
import static org.mockito.Mockito.*;

public class SpannedStringMock {
    public static SpannedString create(String value) {
        SpannedString spanned = mock(SpannedString.class);

        when(spanned.toString()).thenReturn(value);
        when(spanned.length()).thenReturn(value.length());

        return spanned;
    }
}
