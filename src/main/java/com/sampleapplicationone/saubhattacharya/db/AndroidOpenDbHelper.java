package com.sampleapplicationone.saubhattacharya.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AndroidOpenDbHelper extends SQLiteOpenHelper implements BaseColumns{

    public static final String DB_NAME = "CONTACT_LIST_DB";
    public static final int DB_VERSION = 2;
    public static final String CONTACT_LIST_TABLE = "CONTACT_LIST_TABLE";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_PHONE_NUMBER = "Phone_Number";
    public static final String COLUMN_NAME_EMAIL = "Email";
    public static final String COLUMN_NAME_HIDDEN_FLAG = "Hidden_Flag";
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE "+AndroidOpenDbHelper.CONTACT_LIST_TABLE
            +" ("+AndroidOpenDbHelper._ID+" INTEGER PRIMARY KEY"+COMMA_SEP
            +AndroidOpenDbHelper.COLUMN_NAME_NAME+TEXT_TYPE+COMMA_SEP
            +AndroidOpenDbHelper.COLUMN_NAME_PHONE_NUMBER+TEXT_TYPE+COMMA_SEP
            +AndroidOpenDbHelper.COLUMN_NAME_EMAIL+TEXT_TYPE+COMMA_SEP
            +AndroidOpenDbHelper.COLUMN_NAME_HIDDEN_FLAG+TEXT_TYPE+")";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "+AndroidOpenDbHelper.CONTACT_LIST_TABLE;

    public AndroidOpenDbHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion)
    {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}
