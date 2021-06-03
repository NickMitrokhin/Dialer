package com.mitrokhin.nick.dialer.infrastructure;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class BaseAdapter<T> extends ArrayAdapter<T> {
    protected final Context context;
    protected final int layoutResourceId;

    public BaseAdapter(Context context, int layoutResourceId, List<T> dataItems) {
        super(context, layoutResourceId, dataItems);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    protected View createView(ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        return inflater.inflate(layoutResourceId, parent, false);
    }

    protected void bindDataToView(int position, @NonNull View view) {
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = createView(parent);
        }

        bindDataToView(position, view);
        return view;
    }
}
