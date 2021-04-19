package com.mitrokhin.nick.dialer.infrastructure;

import android.text.Spanned;
import android.text.SpannedString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class InputFilterMinMaxTest {
    private InputFilterMinMax input = new InputFilterMinMax(5, 10);
    private Spanned dest = mock(SpannedString.class);

    @Test
    public void passedFilterTest() {
        when(dest.toString()).thenReturn("");
        when(dest.length()).thenReturn(0);

        for(short i = 5; i <= 10; i++) {
            String source = Short.toString(i);
            assertNull(input.filter(source, 0, source.length(), dest, 0, dest.length()));
        }
    }

    @Test
    public void failedFilterLessTest() {
        when(dest.toString()).thenReturn("");
        when(dest.length()).thenReturn(0);

        for(short i = 0; i < 5; i++) {
            String source = Short.toString(i);
            assertEquals("", input.filter(source, 0, source.length(), dest, 0, dest.length()));
        }
    }

    @Test
    public void failedFilterGreaterTest() {
        when(dest.toString()).thenReturn("1");
        when(dest.length()).thenReturn(1);

        for(short i = 1; i < 5; i++) {
            String source = Short.toString(i);
            assertEquals("", input.filter(source, 0, source.length(), dest, 0, dest.length()));
        }
    }

}
