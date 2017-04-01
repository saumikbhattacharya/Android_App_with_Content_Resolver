package com.sampleapplicationone.saubhattacharya.helpers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.sampleapplicationone.saubhattacharya.MainActivity;
import com.sampleapplicationone.saubhattacharya.R;
import java.util.ArrayList;

public class FetchContactTask extends AsyncTask<Void,Void,ArrayList<ListRowItem>>
{
    ProgressDialog progressDialog;
    MainActivity mainActivity;
    Boolean menu;
    String SQL_QUERY;
    ArrayList<ListRowItem> contact_list;

    public FetchContactTask (MainActivity activity, Boolean from_menu, String SQL)
    {
        mainActivity = activity;
        menu = from_menu;
        SQL_QUERY = SQL;
        progressDialog = new ProgressDialog(mainActivity);
    }

    @Override
    protected void onPostExecute(ArrayList<ListRowItem> list)
    {
        super.onPostExecute(list);
        mainActivity.replaceFragment();
        progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog.setMessage(mainActivity.getResources().getString(R.string.Progress_Dialog));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected ArrayList<ListRowItem> doInBackground(Void... params)
    {
        if(menu)
        {
            contact_list = mainActivity.fetchSpecificContact(SQL_QUERY);
        }
        else {
            contact_list = mainActivity.checkContactCache();
        }
        return contact_list;
    }
}
