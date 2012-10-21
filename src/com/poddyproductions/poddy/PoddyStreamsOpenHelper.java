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

	private static final int DATABASE_VERSION            = 1;
	private static final String STREAMS_TABLE_NAME       = "poddy_streams";
	private static final String PODCAST_ITEMS_TABLE_NAME = "poddy_podcast_items";
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String STREAMS_TABLE_CREATE =
			"CREATE TABLE " + STREAMS_TABLE_NAME + " (" +
				"id              INTEGER PRIMARY KEY ASC, " +
				"feedUrl         TEXT, " +
				"lastUpdated     DATETIME, " +
				"title           TEXT, " +
				"description     TEXT, " +
				"link            TEXT, " +
				"language        TEXT, " +
				"copyright       TEXT, " +
				"lastBuildDate   TEXT, " +
				"pubDate         TEXT, " +
				"docs            TEXT, " +
				"webMaster       TEXT, " +
				"itunes_subtitle TEXT, " +
				"itunes_summary  TEXT, " +
				"itunes_image_ln TEXT, " +
				"itunes_image    BLOB );";

	
	private static final String PODCAST_ITEMS_TABLE_CREATE =
			"CREATE TABLE " + PODCAST_ITEMS_TABLE_NAME + " (" +
					"podcastId       INTEGER," +
					"title           TEXT, " +
					"link            TEXT, " +
					"guid            TEXT, " +
					"description     TEXT, " +
					"category        TEXT, " +
					"pubDate         DATETIME, " +
					"itunes_subtitle TEXT, " +
					"itunes_summary  TEXT, " +
					"itunes_duration TEXT, " +
					"filename        TEXT );";

	public PoddyStreamsOpenHelper(Context context, String databaseName) {
		super(context, databaseName, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(STREAMS_TABLE_CREATE);
		db.execSQL(PODCAST_ITEMS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Implement
	}

	
	public void add(Podcast entry) {
		ContentValues values = new ContentValues();
		values.put("feedUrl", entry.feedUrl);
		values.put("lastUpdated", dateFormat.format(entry.lastUpdated));
		values.put("title", entry.title);
		values.put("description", entry.description);
		values.put("link", entry.link);
		values.put("language", entry.language);
		values.put("copyright", entry.copyright);
		values.put("lastBuildDate", entry.lastBuildDate);
		values.put("pubDate", entry.pubDate);
		values.put("docs", entry.docs);
		values.put("webMaster", entry.webMaster);
		values.put("itunes_subtitle", entry.itunes_subtitle);
		values.put("itunes_summary", entry.itunes_summary);
		values.put("itunes_image_ln", entry.itunes_image);
		//values.put("itunes_image", entry.itunes_image);
		long rowId = getWritableDatabase().insert(STREAMS_TABLE_NAME, null, values);

		for(int i = 0; i < entry.podcastItems.size(); i++) {
			ContentValues itemValues = new ContentValues();
			Podcast.PodcastItem item = entry.podcastItems.get(i);
			itemValues.put("podcastId", rowId);
			itemValues.put("title", item.title);
			itemValues.put("link", item.link);
			itemValues.put("guid", item.guid);
			itemValues.put("description", item.description);
			itemValues.put("category", item.category);
			itemValues.put("pubDate", item.pubDate);
			itemValues.put("itunes_subtitle", item.itunes_subtitle);
			itemValues.put("itunes_summary", item.itunes_summary);
			itemValues.put("itunes_duration", item.itunes_duration);
			itemValues.put("filename", "");
			
			getWritableDatabase().insert(PODCAST_ITEMS_TABLE_NAME, null, itemValues);
		}
		
	}

	public ArrayList<Podcast> getAllPodcasts() {
		return  getPodcastsImpl(null);
	}
	
	public Podcast getAllPodcastWithTitle(String title) {
		ArrayList<Podcast> ret =getPodcastsImpl(title);
		if(ret.size() > 0) {
			return ret.get(0);
		}
		return null;
	}

	private ArrayList<Podcast> getPodcastsImpl(String title) {
		ArrayList<Podcast> ret = new ArrayList<Podcast>();

		String selection = title;
		if (selection != null) {
			selection = "title='" + title + "'";
		}

		Cursor cur = getReadableDatabase().query(STREAMS_TABLE_NAME, null, selection, null, null, null, null, null);

		while(cur.moveToNext()) {
			Date date;
			try {
				date = dateFormat.parse(cur.getString(2));
			} catch(ParseException ex) {
				date = new Date();
			}
			
			Podcast pc = new Podcast();
			pc.id              = cur.getLong(0);
			pc.feedUrl         = cur.getString(1);
			pc.lastUpdated     = date;
			pc.title           = cur.getString(3);
			pc.description     = cur.getString(4);
			pc.link            = cur.getString(5);
			pc.language        = cur.getString(6);
			pc.copyright       = cur.getString(7);
			pc.lastBuildDate   = cur.getString(8);
			pc.pubDate         = cur.getString(9);
			pc.docs            = cur.getString(10);
			pc.webMaster       = cur.getString(11);
			pc.itunes_subtitle = cur.getString(12);
			pc.itunes_summary  = cur.getString(13);
			pc.itunes_image    = cur.getString(14);

			Cursor cur2 = getReadableDatabase().query(PODCAST_ITEMS_TABLE_NAME, null, "podcastID=" + pc.id, null, null, null, null, null);
			while(cur2.moveToNext()) {
				Podcast.PodcastItem pcItem = pc.new PodcastItem();
				pcItem.title            = cur2.getString(1);
				pcItem.link             = cur2.getString(2);
				pcItem.guid             = cur2.getString(3);
				pcItem.description      = cur2.getString(4);
				pcItem.category         = cur2.getString(5);
				pcItem.pubDate          = cur2.getString(6);
				pcItem.itunes_subtitle  = cur2.getString(7);
				pcItem.itunes_summary   = cur2.getString(8);
				pcItem.itunes_duration  = cur2.getString(9);
				pcItem.filename         = cur2.getString(10);
				pc.podcastItems.add(pcItem);
			}
			ret.add(pc);
			cur2.close();
		}
		cur.close();

		return ret;
	}
}
