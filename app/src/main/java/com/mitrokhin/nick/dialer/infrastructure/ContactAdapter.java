package com.mitrokhin.nick.dialer.infrastructure;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.mitrokhin.nick.dialer.R;
import com.mitrokhin.nick.dialer.models.ContactItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ContactAdapter extends BaseAdapter<ContactItem> {
    private final List<ContactItem> dataItems;
    private ContactFilter filter;
    private final DataProvider dataProvider;

    public ContactAdapter(Context context, int layoutResourceId, List<ContactItem> dataItems) {
        super(context, layoutResourceId, dataItems);
        this.dataItems = new ArrayList<>(dataItems);
        this.dataProvider = new DataProvider(context);
    }

    @Override
    protected void bindDataToView(int position, @NonNull View view) {
        ContactItem contact = getItem(position);

        if(contact == null) {
            return;
        }

        TextView txtContactName = view.findViewById(R.id.txtContactName);
        ImageView ivContactPhoto = view.findViewById(R.id.ivContactPhoto);

        txtContactName.setText(contact.getName());
        Bitmap photo = dataProvider.getPhotoByContactID(contact.getId());
        if(photo != null) {
            ivContactPhoto.setImageBitmap(photo);
        } else {
            ivContactPhoto.setImageResource(R.drawable.default_user);
        }
    }

    @Override
    public void addAll(@NonNull Collection<? extends ContactItem> collection) {
        super.addAll(collection);
        dataItems.addAll(new ArrayList<>(collection));
    }

    public void clearAll() {
        super.clear();
        dataItems.clear();
    }

    @Override
    public @NonNull Filter getFilter() {
        if(filter == null) {
            filter = new ContactFilter();
        }
        return filter;
    }

    private class ContactFilter extends Filter {
        private List<ContactItem> getFoundItems(String value) {
            String filterValue = value.toLowerCase();

            return dataItems.stream()
                    .filter(item -> item.getName().toLowerCase().contains(filterValue))
                    .collect(Collectors.toList());
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            String filterValue = constraint != null ? constraint.toString() : "";
            List<ContactItem> foundItems = filterValue.length() > 0 ? getFoundItems(filterValue) : dataItems;
            result.values = foundItems;
            result.count = foundItems.size();
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            for(ContactItem item : (List<ContactItem>)filterResults.values) {
                add(item);
            }
            notifyDataSetChanged();
        }
    }
}

