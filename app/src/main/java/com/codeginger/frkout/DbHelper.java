package com.codeginger.frkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Base64;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

// Created by Pratik Mehta on 14-03-2016.

public class DbHelper extends SQLiteOpenHelper
{
    public String local_addressTemp;

    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "frkout_database";
    private static final String COMMON_ID = "_id";

    // Table
    private static final String LOCAL_BROADCAST_TABLE = "broadcast_table";

    // Column
    private static final String LOCAL_BROADCAST_TABLE_CAMPAIGN_NAME = "local_broadcast_table_campaign_name";
    private static final String LOCAL_BROADCAST_TABLE_CAMPAIGN_DESCRIPTION = "local_broadcast_table_campaign_description";
    private static final String LOCAL_BROADCAST_TABLE_CAMPAIGN_DATE = "local_broadcast_table_campaign_date";
    private static final String LOCAL_BROADCAST_TABLE_AUDIENCE_GENDER = "local_broadcast_table_audience_gender";
    private static final String LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRP = "local_broadcast_table_audience_agegrp";
    private static final String LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRPFROM = "local_broadcast_table_audience_agegrpfrom";
    private static final String LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRPTO = "local_broadcast_table_audience_agegrpto";

    // Create Table
    private static final String CREATE_TABLE_BROADCAST = "CREATE TABLE "
            + LOCAL_BROADCAST_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_BROADCAST_TABLE_CAMPAIGN_NAME + " TEXT,"
            + LOCAL_BROADCAST_TABLE_CAMPAIGN_DESCRIPTION + " TEXT,"
            + LOCAL_BROADCAST_TABLE_CAMPAIGN_DATE + " TEXT,"
            + LOCAL_BROADCAST_TABLE_AUDIENCE_GENDER + " TEXT,"
            + LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRP + " TEXT,"
            + LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRPFROM + " TEXT,"
            + LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRPTO + " TEXT);";

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_BROADCAST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_BROADCAST_TABLE);
            onCreate(db);
        }
    }

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open()
    {
        db = this.getWritableDatabase();
    }

    public void close()
    {
        db.close();
    }

    // Insert Brodcast Campaign
    public long insertBrodCamp(String name, String description, String date)
    {
        long row;

        open();
        ContentValues values = new ContentValues();
        values.put(LOCAL_BROADCAST_TABLE_CAMPAIGN_NAME, name);
        values.put(LOCAL_BROADCAST_TABLE_CAMPAIGN_DESCRIPTION, description);
        values.put(LOCAL_BROADCAST_TABLE_CAMPAIGN_DATE, date);
        row = db.insert(LOCAL_BROADCAST_TABLE, null, values);
        close();

        return row;
    }

    // Update Brodcast Campaign
    public long updateBrodCamp(String name, String description, String date, long rowNo)
    {
        long row = rowNo;

        open();
        ContentValues values = new ContentValues();
        values.put(LOCAL_BROADCAST_TABLE_CAMPAIGN_NAME, name);
        values.put(LOCAL_BROADCAST_TABLE_CAMPAIGN_DESCRIPTION, description);
        values.put(LOCAL_BROADCAST_TABLE_CAMPAIGN_DATE, date);
        row = db.update(LOCAL_BROADCAST_TABLE, values, COMMON_ID + "=?", new String[]{rowNo + ""});
        close();

        return row;
    }

    // Update Brodcast Audience
    public long updateBrodAud(String gender, String ageGrp, String ageGrpFrom, String ageGrpTo, long rowNo)
    {
        long row = rowNo;

        open();
        ContentValues values = new ContentValues();
        values.put(LOCAL_BROADCAST_TABLE_AUDIENCE_GENDER, gender);
        values.put(LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRP, ageGrp);
        values.put(LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRPFROM, ageGrpFrom);
        values.put(LOCAL_BROADCAST_TABLE_AUDIENCE_AGEGRPTO, ageGrpTo);
        row = db.update(LOCAL_BROADCAST_TABLE, values, COMMON_ID + "=?", new String[]{rowNo + ""});
        close();

        return row;
    }

    // Delete Brodcast Campaign
    public void deleteBrodCamp()
    {
        open();
        db.execSQL("DROP TABLE IF EXISTS " + LOCAL_BROADCAST_TABLE);
        onCreate(db);
        close();
    }

}
