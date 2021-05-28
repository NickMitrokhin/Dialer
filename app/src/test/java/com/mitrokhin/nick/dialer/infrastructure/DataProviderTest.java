package com.mitrokhin.nick.dialer.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.mitrokhin.nick.dialer.mocks.ContextMock;
import com.mitrokhin.nick.dialer.mocks.ContentResolverMock;
import com.mitrokhin.nick.dialer.mocks.CursorMock;
import com.mitrokhin.nick.dialer.models.ContactItem;

import java.util.List;


public class DataProviderTest {
    private Context context;
    private ContentResolver contentResolver;
    private Cursor cursor;
    private Object[][] dataItems;
    private String[] dataFields;

    @BeforeEach
    public void setUp() {
        context = mock(ContextMock.class);
        contentResolver = mock(ContentResolverMock.class);

        cursor = new CursorMock();

        when(context.getContentResolver()).thenReturn(contentResolver);
        when(contentResolver.query(nullable(Uri.class), nullable(String[].class), nullable(String.class), nullable(String[].class), nullable(String.class)))
                .thenReturn(cursor);
    }

    @Test
    public void getContactsTest() {
        dataItems = new Object[][] {
            { "1", "Contact1", "1" }, { "2", "Contact2", "0" }, { "3", "Contact3", "2" }
        };
        dataFields = new String[] {"_id", "display_name", "has_phone_number"};
        ((CursorMock)cursor).setUp(dataItems, dataFields);

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
        dataItems = new Object[][] {
            { "123456" }, { "(12)-34-56" }, { "789102" }
        };
        dataFields = new String[]{"data1"};
        ((CursorMock)cursor).setUp(dataItems, dataFields);

        DataProvider provider = new DataProvider(context);
        List<String> items = provider.getPhonesByContactID("");

        assertEquals(2, items.size());
        assertEquals("123456", items.get(0));
        assertEquals("789102", items.get(1));
    }

    @Test
    public void getPhotoByContactIDTest() {
        byte[] bytesPhoto = { 1, 2, 3 };
        dataItems = new Object[][] {
                { bytesPhoto }
        };
        dataFields = new String[] {"photo"};
        ((CursorMock)cursor).setUp(dataItems, dataFields);
        Bitmap mockPhoto = mock(Bitmap.class);

        when(mockPhoto.getWidth()).thenReturn(bytesPhoto.length);

        try (
                MockedStatic<ContentUris> contentUris = mockStatic(ContentUris.class);
                MockedStatic<Uri> uri = mockStatic(Uri.class);
                MockedStatic<BitmapFactory> bitmapFactory = mockStatic(BitmapFactory.class)
                ) {
            contentUris.when(() -> ContentUris.withAppendedId(any(Uri.class), anyLong())).thenReturn(null);
            uri.when(() -> Uri.withAppendedPath(nullable(Uri.class), anyString())). thenReturn(null);
            bitmapFactory.when(() -> BitmapFactory.decodeByteArray(bytesPhoto, 0, bytesPhoto.length)).thenReturn(mockPhoto);

            DataProvider provider = new DataProvider(context);
            Bitmap photo = provider.getPhotoByContactID("1");

            assertNotNull(photo);
            assertEquals(bytesPhoto.length, photo.getWidth());
        }
    }

    @Test
    public void getPhotoByContactIDEmptyTest() {
        dataItems = new Object[][] {};
        dataFields = new String[] {"photo"};
        ((CursorMock)cursor).setUp(dataItems, dataFields);

        try (
                MockedStatic<ContentUris> contentUris = mockStatic(ContentUris.class);
                MockedStatic<Uri> uri = mockStatic(Uri.class)
        ) {
            contentUris.when(() -> ContentUris.withAppendedId(any(Uri.class), anyLong())).thenReturn(null);
            uri.when(() -> Uri.withAppendedPath(nullable(Uri.class), anyString())). thenReturn(null);

            DataProvider provider = new DataProvider(context);
            Bitmap photo = provider.getPhotoByContactID("1");

            assertNull(photo);
        }
    }
}
