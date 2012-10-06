package com.poddyproductions.poddy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PoddyStreamsOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String STREAMS_TABLE_NAME = "poddy_streams";
	private static final String DATABASE_NAME      = "poddyDb";
	private static final String URL                = "url";
	private static final String LAST_UPDATED       = "lastUpdated";
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + STREAMS_TABLE_NAME + " (" +
            URL + " TEXT, " +
            LAST_UPDATED + " DATETIME);";
	
	PoddyStreamsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Implement
	}
	
	public ArrayList<StreamViewEntry> getAllKnownStreams() {
		ArrayList<StreamViewEntry> ret = new ArrayList<StreamViewEntry>();
		
		Cursor cur = getReadableDatabase().query(STREAMS_TABLE_NAME, null, null, null, null, null, null, null);
		
		while(cur.moveToNext()) {
			final String url = cur.getString(0);
			Date date;
			try {
				date = dateFormat.parse(cur.getString(1));
			} catch(ParseException ex) {
				date = new Date();
			}
			ret.add( new StreamViewEntry(url, date));
		}
		
		return ret;
	}

	public void add(StreamViewEntry entry) {
		ContentValues values = new ContentValues();
		values.put(URL,          entry.getUrl());
		values.put(LAST_UPDATED, dateFormat.format(entry.getLastUpdated()));
		getWritableDatabase().insert(STREAMS_TABLE_NAME, null, values);
	}

}
