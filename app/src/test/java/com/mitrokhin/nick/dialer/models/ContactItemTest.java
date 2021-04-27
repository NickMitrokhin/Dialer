package com.mitrokhin.nick.dialer.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ContactItemTest {
    @Test
    public void ContactItemMethodsTest() {
        ContactItem item = new ContactItem("tid", "tname");

        assertEquals("tid", item.getId());
        assertEquals("tname", item.getName());
    }
}
