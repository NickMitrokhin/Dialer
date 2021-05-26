package com.mitrokhin.nick.dialer.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.mitrokhin.nick.dialer.mocks.ContextMock;
import com.mitrokhin.nick.dialer.mocks.ContentResolverMock;
import com.mitrokhin.nick.dialer.mocks.CursorMock;
import com.mitrokhin.nick.dialer.models.ContactItem;

import java.util.List;


public class DataProviderTest {
    @Mock
    Context context;
    @Mock
    ContentResolver contentResolver;

    @BeforeEach
    public void setUp() {
        context = mock(ContextMock.class);
        contentResolver = mock(ContentResolverMock.class);
    }

    @Test
    public void getContactsTest() {
        Object[][] dataItems = {
            { "1", "Contact1", "1" }, { "2", "Contact2", "0" }, { "3", "Contact3", "2" }
        };
        String[] dataFields = {"_id", "display_name", "has_phone_number"};
        Cursor cursor = new CursorMock(dataItems, dataFields);

        when(context.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.query(eq(ContactsContract.Contacts.CONTENT_URI), eq(null), eq(null), eq(null), anyString()))
                .thenReturn(cursor);

        DataProvider provider = new DataProvider(context);
        List<ContactItem> items = provider.getContacts();

        assertEquals(2, items.size());
        assertEquals("1", items.get(0).getId());
        assertEquals("Contact1", items.get(0).getName());
        assertEquals("3", items.get(1).getId());
        assertEquals("Contact3", items.get(1).getName());
    }

    @Test
    public void getPhonesByContactIDTest() {
        Object[][] dataItems = {
            { "123456" }, { "(12)-34-56" }, { "789102" }
        };
        String[] dataFields = {"data1"};
        Cursor cursor = new CursorMock(dataItems, dataFields);

        when(context.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.query(eq(ContactsContract.CommonDataKinds.Phone.CONTENT_URI), eq(null), anyString(), any(String[].class), eq(null)))
                .thenReturn(cursor);

        DataProvider provider = new DataProvider(context);
        List<String> items = provider.getPhonesByContactID("");

        assertEquals(2, items.size());
        assertEquals("123456", items.get(0));
        assertEquals("789102", items.get(1));
    }
}
