package com.sampleapplicationone.saubhattacharya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sampleapplicationone.saubhattacharya.helpers.ListRowItem;
import com.sampleapplicationone.saubhattacharya.R;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter{

    Context context;
    ArrayList<ListRowItem> list = new ArrayList<>();
    ListRowItem listRowItem;

    public ContactAdapter(Context con, ArrayList<ListRowItem> con_list)
    {
        super();
        list = con_list;
        context = con;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            vi = inflater.inflate(R.layout.each_list_item, viewGroup, false);

        listRowItem = list.get(i);

        TextView textView_name = (TextView)vi.findViewById(R.id.textView);
        textView_name.setText(listRowItem.getName());

        TextView textView_phone = (TextView)vi.findViewById(R.id.textView2);
        textView_phone.setText(listRowItem.getPhone_number().replace(" ",""));

        TextView textView_email = (TextView)vi.findViewById(R.id.textView3);
        textView_email.setText(listRowItem.getEmail());

        return vi;
    }
}
