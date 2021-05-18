package com.mitrokhin.nick.dialer.infrastructure;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitrokhin.nick.dialer.R;

import java.util.List;


public class PhoneAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final int layoutResourceId;

    public PhoneAdapter(Context context, int layoutResourceId, List<String> dataItems) {
        super(context, layoutResourceId, dataItems);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public  @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        PhoneItemHolder holder;
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PhoneItemHolder();
            holder.txtPhoneNumber = row.findViewById(R.id.txtPhoneNumber);
            holder.ivPhoneImage = row.findViewById(R.id.ivPhoneImage);
            row.setTag(holder);
        } else {
            holder = (PhoneItemHolder)row.getTag();
        }

        String phoneNo = getItem(position);
        holder.txtPhoneNumber.setText(phoneNo);
        holder.ivPhoneImage.setImageResource(R.drawable.phone);
        return row;
    }

    private static class PhoneItemHolder {
        TextView txtPhoneNumber;
        ImageView ivPhoneImage;
    }
}