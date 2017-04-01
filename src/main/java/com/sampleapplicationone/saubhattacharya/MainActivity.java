package com.sampleapplicationone.saubhattacharya;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sampleapplicationone.saubhattacharya.adapters.NavigationAdapter;
import com.sampleapplicationone.saubhattacharya.db.AndroidOpenDbHelper;
import com.sampleapplicationone.saubhattacharya.fragments.ContactFragment;
import com.sampleapplicationone.saubhattacharya.fragments.ImageFragment;
import com.sampleapplicationone.saubhattacharya.fragments.MusicFragment;
import com.sampleapplicationone.saubhattacharya.fragments.NotesFragment;
import com.sampleapplicationone.saubhattacharya.helpers.FetchContactTask;
import com.sampleapplicationone.saubhattacharya.helpers.ListRowItem;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    ArrayList<ListRowItem> contact_list = new ArrayList<>();
    ArrayList<String> image_list = new ArrayList<>();
    AndroidOpenDbHelper openDbHelperObj;
    SQLiteDatabase sqLiteWritableDatabase;
    SQLiteDatabase sqLiteReadableDatabase;
    ContentValues contentValues;
    String SQL_FETCH_CONTACT = "";
    String[] nav_drawer_icons,nav_drawer_items;

    public static final String SQL_SELECT_QUERY = "SELECT * FROM "
            +AndroidOpenDbHelper.CONTACT_LIST_TABLE+
            " WHERE "+AndroidOpenDbHelper.COLUMN_NAME_HIDDEN_FLAG+" = 'N'";

    String name,phone_num,email,id;
    public static final String SQL_DLT_OLD_ENTRIES = "DELETE FROM "+AndroidOpenDbHelper.CONTACT_LIST_TABLE;
    final private int REQUEST_CONTACT_PERMISSIONS = 998;
    final private int REQUEST_IMAGE_PERMISSIONS = 999;
    FetchContactTask fetchContactTask;
    Boolean from_menu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        nav_drawer_icons = getResources().getStringArray(R.array.nav_drawer_icons);
        nav_drawer_items = getResources().getStringArray(R.array.nav_drawer_items);

        final ListView nav_icon_list = (ListView)findViewById(R.id.navigation_content);
        NavigationAdapter myBaseAdapter = new NavigationAdapter(this,nav_drawer_icons,getPackageName());
        nav_icon_list.setAdapter(myBaseAdapter);

        openDbHelperObj = new AndroidOpenDbHelper(this);
        sqLiteWritableDatabase = openDbHelperObj.getWritableDatabase();
        sqLiteReadableDatabase = openDbHelperObj.getReadableDatabase();

        final FragmentManager fragmentManager = getFragmentManager();

        setTitle(getResources().getString(R.string.home));

        nav_icon_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (nav_drawer_items[i]) {
                    case "Contacts":
                        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},REQUEST_CONTACT_PERMISSIONS);
                            return;
                        }
                        from_menu = false;
                        FetchContactTask fetchContactTask = new FetchContactTask(MainActivity.this,false,"");
                        fetchContactTask.execute();

                        break;

                    case "Images":
                        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_IMAGE_PERMISSIONS);
                            return;
                        }
                        fetchImagesfromGallery();
                        Fragment ImageFragment = new ImageFragment();
                        Bundle ImageArgs = new Bundle();
                        ImageArgs.putSerializable("image_list", image_list);
                        ImageFragment.setArguments(ImageArgs);

                        fragmentManager.beginTransaction().replace(R.id.main_content_frame, ImageFragment).commit();
                        break;

                    case "Music":
                        Fragment MusicFragment = new MusicFragment();

                        fragmentManager.beginTransaction().replace(R.id.main_content_frame, MusicFragment).commit();
                        break;

                    case "Notes":
                        Fragment NotesFragment = new NotesFragment();

                        fragmentManager.beginTransaction().replace(R.id.main_content_frame, NotesFragment).commit();
                        break;
                }

                nav_icon_list.setItemChecked(i, true);
                setTitle(nav_drawer_items[i]);
                drawerLayout.closeDrawer(nav_icon_list);
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                return true;

            case R.id.action_show_only_number:
                from_menu = true;
                SQL_FETCH_CONTACT = "SELECT * FROM "+AndroidOpenDbHelper.CONTACT_LIST_TABLE+" WHERE "+AndroidOpenDbHelper.COLUMN_NAME_PHONE_NUMBER+" <> ''";
                fetchContactTask = new FetchContactTask(MainActivity.this,from_menu,SQL_FETCH_CONTACT);
                fetchContactTask.execute();
                return true;

            case R.id.action_show_only_email:
                from_menu = true;
                SQL_FETCH_CONTACT = "SELECT * FROM "+AndroidOpenDbHelper.CONTACT_LIST_TABLE+" WHERE "+AndroidOpenDbHelper.COLUMN_NAME_EMAIL+" <> ''";
                fetchContactTask = new FetchContactTask(MainActivity.this,from_menu,SQL_FETCH_CONTACT);
                fetchContactTask.execute();
                return true;

            case R.id.action_show_hidden_contacts:
                from_menu = true;
                SQL_FETCH_CONTACT = "SELECT * FROM "+AndroidOpenDbHelper.CONTACT_LIST_TABLE+" WHERE "+AndroidOpenDbHelper.COLUMN_NAME_HIDDEN_FLAG+" = 'Y'";
                fetchContactTask = new FetchContactTask(MainActivity.this,from_menu,SQL_FETCH_CONTACT);
                fetchContactTask.execute();
                return true;

            case R.id.action_refresh:
                from_menu = false;
                sqLiteReadableDatabase.execSQL(SQL_DLT_OLD_ENTRIES);
                fetchContactTask = new FetchContactTask(MainActivity.this,from_menu,"");
                fetchContactTask.execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void manageContact()
    {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    id = "";
                    name = "";
                    phone_num = "";
                    email = "";

                    id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                    Cursor phone_cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (phone_cursor != null) {
                        if (phone_cursor.moveToFirst()) {
                            do {
                                phone_num = phone_cursor.getString(phone_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            } while (phone_cursor.moveToNext());
                        }
                    }

                    Cursor email_cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (email_cursor != null) {
                        if (email_cursor.moveToFirst()) {
                            do {
                                email = email_cursor.getString(email_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            } while (email_cursor.moveToNext());
                        }
                    }

                    ListRowItem listRowItem = new ListRowItem();
                    listRowItem.setName(name);
                    listRowItem.setEmail(email);
                    listRowItem.setPhone_number(phone_num);

                    contact_list.add(listRowItem);

                    contentValues = new ContentValues();

                    contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_NAME,name);
                    contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_PHONE_NUMBER, phone_num);
                    contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_EMAIL, email);
                    contentValues.put(AndroidOpenDbHelper.COLUMN_NAME_HIDDEN_FLAG, "N");

                    long affectedRowId = sqLiteWritableDatabase.insert(AndroidOpenDbHelper.CONTACT_LIST_TABLE,null,contentValues);

                }while(cursor.moveToNext());
            }
        }
    }

    public ArrayList<ListRowItem> checkContactCache()
    {
        if(contact_list != null)
            contact_list.clear();

        Cursor cursor = sqLiteReadableDatabase.rawQuery(SQL_SELECT_QUERY, null);

        if(cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                phone_num = cursor.getString(cursor.getColumnIndex("Phone_Number"));
                email = cursor.getString(cursor.getColumnIndex("Email"));

                ListRowItem lr = new ListRowItem();
                lr.setName(name);
                lr.setPhone_number(phone_num);
                lr.setEmail(email);

                contact_list.add(lr);

            } while (cursor.moveToNext());
        }
        else
        {
            manageContact();
        }

        return contact_list;
    }

    public void replaceFragment()
    {
        Fragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact_list", contact_list);
        args.putBoolean("from_menu",from_menu);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit();
    }

    public void fetchImagesfromGallery()
    {
        if(image_list != null)
            image_list.clear();

        File[] listFile;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/Camera");
        if(file.isDirectory())
        {
            listFile = file.listFiles();

            for(int i = 0; i < listFile.length; i++)
            {
                image_list.add(listFile[i].getAbsolutePath());
            }
        }
    }

    public ArrayList<ListRowItem> fetchSpecificContact(String SQL_FETCH_CONTACT)
    {
        if(contact_list != null)
            contact_list.clear();

        Cursor cursor = sqLiteReadableDatabase.rawQuery(SQL_FETCH_CONTACT, null);

        if(cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("Name"));
                phone_num = cursor.getString(cursor.getColumnIndex("Phone_Number"));
                email = cursor.getString(cursor.getColumnIndex("Email"));

                ListRowItem lr = new ListRowItem();
                lr.setName(name);
                lr.setPhone_number(phone_num);
                lr.setEmail(email);

                contact_list.add(lr);

            } while (cursor.moveToNext());
        }
        return contact_list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CONTACT_PERMISSIONS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    checkContactCache();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Permission is not allowed to read Contacts!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case REQUEST_IMAGE_PERMISSIONS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    fetchImagesfromGallery();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Permission is not allowed to read Internal Storage!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
