package com.sampleapplicationone.saubhattacharya.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sampleapplicationone.saubhattacharya.adapters.ContactAdapter;
import com.sampleapplicationone.saubhattacharya.db.AndroidOpenDbHelper;
import com.sampleapplicationone.saubhattacharya.helpers.ListRowItem;
import com.sampleapplicationone.saubhattacharya.R;

import java.util.ArrayList;

public class ContactFragment extends Fragment
{
    ArrayList<ListRowItem> contact_list = new ArrayList<>();
    ListView contact_listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_contacts, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        contact_list = (ArrayList<ListRowItem>) bundle.getSerializable("contact_list");
        final Boolean from_menu = bundle.getBoolean("from_menu");

        ContactAdapter baseAdapter = new ContactAdapter(getActivity(),contact_list);
        contact_listview = (ListView)view.findViewById(R.id.fragment_listview);
        contact_listview.setAdapter(baseAdapter);

        contact_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                if(!from_menu) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.user_msg_1))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    hideContacts(i);
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                return false;
            }
        });
    }

    public void hideContacts(int pos)
    {
        AndroidOpenDbHelper openDbHelperObj = new AndroidOpenDbHelper(getActivity());
        SQLiteDatabase sqLiteWritableDatabase = openDbHelperObj.getWritableDatabase();

        int selected_item = pos + 1;

        String SQL_UPD_HIDDEN_FLG = "UPDATE "
                +AndroidOpenDbHelper.CONTACT_LIST_TABLE+
                " SET "+AndroidOpenDbHelper.COLUMN_NAME_HIDDEN_FLAG+" = 'Y' WHERE "
                +AndroidOpenDbHelper._ID+" = "+selected_item;

        sqLiteWritableDatabase.execSQL(SQL_UPD_HIDDEN_FLG);

        contact_list.remove(pos);
        ContactAdapter baseAdapter = new ContactAdapter(getActivity(),contact_list);
        baseAdapter.notifyDataSetChanged();
        contact_listview.setAdapter(baseAdapter);
    }
}
