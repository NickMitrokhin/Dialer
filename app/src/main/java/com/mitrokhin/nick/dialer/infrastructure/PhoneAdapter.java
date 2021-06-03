package com.mitrokhin.nick.dialer.infrastructure;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitrokhin.nick.dialer.R;

import java.util.List;


public class PhoneAdapter extends BaseAdapter<String> {
    public PhoneAdapter(Context context, int layoutResourceId, List<String> dataItems) {
        super(context, layoutResourceId, dataItems);
    }

    @Override
    protected void bindDataToView(int position, @NonNull View view) {
        String phoneNo = getItem(position);

        if(phoneNo == null) {
            return;
        }

        TextView txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        ImageView ivPhoneImage = view.findViewById(R.id.ivPhoneImage);

        txtPhoneNumber.setText(phoneNo);
        ivPhoneImage.setImageResource(R.drawable.phone);
    }
}