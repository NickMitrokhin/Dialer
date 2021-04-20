package com.mitrokhin.nick.dialer.infrastructure;

import android.text.Spanned;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.mitrokhin.nick.dialer.mocks.SpannedStringMock;



public class InputFilterMinMaxTest {
    private InputFilterMinMax input = new InputFilterMinMax(5, 10);

    @Test
    public void passedFilterTest() {
        Spanned dest = SpannedStringMock.create("");

        for(short i = 5; i <= 10; i++) {
            String source = Short.toString(i);
            assertNull(input.filter(source, 0, source.length(), dest, 0, dest.length()));
        }
    }

    @Test
    public void failedFilterLessTest() {
        Spanned dest = SpannedStringMock.create("");

        for(short i = 0; i < 5; i++) {
            String source = Short.toString(i);
            assertEquals("", input.filter(source, 0, source.length(), dest, 0, dest.length()));
        }
    }

    @Test
    public void failedFilterGreaterTest() {
        Spanned dest = SpannedStringMock.create("1");

        for(short i = 1; i < 5; i++) {
            String source = Short.toString(i);
            assertEquals("", input.filter(source, 0, source.length(), dest, 0, dest.length()));
        }
    }
}
