package com.mitrokhin.nick.dialer.mocks;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.List;


public class CursorMock implements Cursor {
    private final Object[][] dataItems;
    private final String[] dataFields;
    private int position = -1;

    public CursorMock(@NonNull Object[][] dataItems, @NonNull String[] dataFields) {
        this.dataItems = dataItems;
        this.dataFields = dataFields;
    }

    @Override
    public boolean moveToFirst() {
        boolean result = false;

        if(dataItems.length > 0) {
            position = 0;
            result = true;
        }

        return result;
    }

    @Override
    public boolean moveToNext() {
        boolean result = false;

        if(dataItems.length > 0 && position < dataItems.length - 1) {
            position++;
            result = true;
        }

        return result;
    }

    @Override
    public String getString(int columnIndex) {
        Object[] row = dataItems[position];

        return (String)row[columnIndex];
    }

    @Override
    public int getColumnIndex(String columnName) {
        int result = -1;

        for(int i = 0; i < dataFields.length; i++) {
            if(dataFields[i].compareTo(columnName) == 0) {
                result = i;
                break;
            }
        }

        return result;
    }

    @Override
    public void close() {
    }

    @Override
    public int getColumnCount() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public String getColumnName(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public String[] getColumnNames() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isNull(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getInt(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public long getLong(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public short getShort(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public float getFloat(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public double getDouble(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setExtras(Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public Bundle getExtras() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getPosition() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isAfterLast() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isBeforeFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isFirst() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isLast() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean move(int offset) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean moveToLast() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean moveToPrevious() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean moveToPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void deactivate() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean isClosed() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean requery() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public Bundle respond(Bundle extras) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void setNotificationUris(ContentResolver cr, List<Uri> uris) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public Uri getNotificationUri() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public List<Uri> getNotificationUris() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getType(int columnIndex) {
        throw new RuntimeException("Stub!");
    }
}
