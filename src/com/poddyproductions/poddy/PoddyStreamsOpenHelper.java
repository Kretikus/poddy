package com.poddyproductions.poddy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

public class PoddyStreamsOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "SQLiteOpenHelper";
	
	private static final int DATABASE_VERSION            = 1;
	private static final String STREAMS_TABLE_NAME       = "poddy_streams";
	private static final String PODCAST_ITEMS_TABLE_NAME = "poddy_podcast_items";

	private static final SimpleDateFormat dateFormat     = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Date getDateFromDb(String str) {
		Date date;
		try {
			date = dateFormat.parse(str);
		} catch(ParseException ex) {
			date = new Date();
		}
		return date;
	}


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
	
	private static final String STREAMS_INDEX_CREATE =
			"CREATE INDEX STREMS_INDEX ON " + STREAMS_TABLE_NAME + " (title);";
	
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
					"mediaUrl        TEXT, " +
					"mediaLength     TEXT, " +
					"mediaType       TEXT, " +
					"filename        TEXT, " +
					"PRIMARY KEY (podcastId, title));";
	
	private static final String PODCAST_ITEMS_INDEX_CREATE = 
			"CREATE INDEX ITEM_INDEX ON " + PODCAST_ITEMS_TABLE_NAME + " (pubDate);";

	public static String compress(String str) {

		try {
		    byte[] blockcopy = ByteBuffer
		            .allocate(4)
		            .order(java.nio.ByteOrder.LITTLE_ENDIAN)
		            .putInt(str.length())
		            .array();
		    ByteArrayOutputStream os = new ByteArrayOutputStream(str.length());
		    GZIPOutputStream gos = new GZIPOutputStream(os);
		    gos.write(str.getBytes());
		    gos.close();
		    os.close();
		    byte[] compressed = new byte[4 + os.toByteArray().length];
		    System.arraycopy(blockcopy, 0, compressed, 0, 4);
		    System.arraycopy(os.toByteArray(), 0, compressed, 4,
		            os.toByteArray().length);
		    return Base64.encodeToString(compressed, Base64.DEFAULT);
		} catch (IOException ex) {
			return new String();
		}
	}

	public static String decompress(String zipText) {

		try {
		    byte[] compressed = Base64.decode(zipText, Base64.DEFAULT);
		    if (compressed.length > 4)
		    {
		        GZIPInputStream gzipInputStream = new GZIPInputStream(
		                new ByteArrayInputStream(compressed, 4,
		                        compressed.length - 4));
	
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        for (int value = 0; value != -1;) {
		            value = gzipInputStream.read();
		            if (value != -1) {
		                baos.write(value);
		            }
		        }
		        gzipInputStream.close();
		        baos.close();
		        String sReturn = new String(baos.toByteArray(), "UTF-8");
		        return sReturn;
		    }
		    else
		    {
		        return "";
		    }
		} catch (IOException ex) {
			return new String();
		}
	}


	public PoddyStreamsOpenHelper(Context context, String databaseName) {
		super(context, databaseName, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(STREAMS_TABLE_CREATE);
		db.execSQL(STREAMS_INDEX_CREATE);

		db.execSQL(PODCAST_ITEMS_TABLE_CREATE);
		db.execSQL(PODCAST_ITEMS_INDEX_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Implement
	}

	public void dropTables() {
		getWritableDatabase().delete(PODCAST_ITEMS_TABLE_NAME, null, null);
		getWritableDatabase().delete(STREAMS_TABLE_NAME, null, null);
	}

	private void addItem(long podcastId, Podcast.PodcastItem item) {
		ContentValues itemValues = new ContentValues();
		
		//String s = compress(item.itunes_summary);
		//Log.e("TEST", "Compressed length: " + s.length() + "Uncompressed length: "+ item.itunes_summary.length());
		
		itemValues.put("podcastId", podcastId);
		itemValues.put("title", item.title);
		itemValues.put("link", item.link);
		itemValues.put("guid", item.guid);
		itemValues.put("description", item.description);
		itemValues.put("category", item.category);
		itemValues.put("pubDate", dateFormat.format(item.pubDate));
		itemValues.put("itunes_subtitle", item.itunes_subtitle);
		itemValues.put("itunes_summary", item.itunes_summary);
		itemValues.put("itunes_duration", item.itunes_duration);
		itemValues.put("mediaUrl", item.mediaUrl);
		itemValues.put("mediaLength", item.mediaLength);
		itemValues.put("mediaType", item.mediaType);

		itemValues.put("filename", "");
		
		getWritableDatabase().insert(PODCAST_ITEMS_TABLE_NAME, null, itemValues);

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
		values.put("docs", entry.docs);
		values.put("webMaster", entry.webMaster);
		values.put("itunes_subtitle", entry.itunes_subtitle);
		values.put("itunes_summary", entry.itunes_summary);
		values.put("itunes_image_ln", entry.itunes_image);
		//values.put("itunes_image", entry.itunes_image);
		long rowId = getWritableDatabase().insert(STREAMS_TABLE_NAME, null, values);

		for(int i = 0; i < entry.podcastItems.size(); i++) {
			addItem(rowId, entry.podcastItems.get(i));
		}
	}

	public void updatePodcast(Podcast entry) {
		String selection = "title='" + entry.title + "'";
		Cursor cur = getReadableDatabase().query(STREAMS_TABLE_NAME, null, selection, null, null, null, null, null);
		if (cur.getCount() == 0) {
			add(entry);
			return;
		}
		
		cur.moveToNext();
		long id = cur.getLong(0);
		
		Cursor cur2 = getReadableDatabase().query(PODCAST_ITEMS_TABLE_NAME, null, "podcastID=" + id, null, null, null, "pubDate DESC", "1");
		if (!cur2.moveToNext()) { Log.e(TAG, "Podcast without any episodes in database"); return; }
		Date newestEntryInDB = getDateFromDb(cur2.getString(2));
		
		for (int i = entry.podcastItems.size()-1; i >= 0; i--) {
			Podcast.PodcastItem item = entry.podcastItems.get(i);
			if (item.pubDate.compareTo(newestEntryInDB) > 0) {
				addItem(id, item);
			}
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

			Podcast pc = new Podcast();
			pc.id              = cur.getLong(0);
			pc.feedUrl         = cur.getString(1);
			pc.lastUpdated     = getDateFromDb(cur.getString(2));
			pc.title           = cur.getString(3);
			pc.description     = cur.getString(4);
			pc.link            = cur.getString(5);
			pc.language        = cur.getString(6);
			pc.copyright       = cur.getString(7);
			pc.lastBuildDate   = cur.getString(8);
			pc.docs            = cur.getString(10);
			pc.webMaster       = cur.getString(11);
			pc.itunes_subtitle = cur.getString(12);
			pc.itunes_summary  = cur.getString(13);
			pc.itunes_image    = cur.getString(14);

			Cursor cur2 = getReadableDatabase().query(PODCAST_ITEMS_TABLE_NAME, null, "podcastID=" + pc.id, 
													null, null, null, "pubDate DESC", null);
			while(cur2.moveToNext()) {
				Podcast.PodcastItem pcItem = pc.new PodcastItem();
				pcItem.title            = cur2.getString(1);
				pcItem.link             = cur2.getString(2);
				pcItem.guid             = cur2.getString(3);
				pcItem.description      = cur2.getString(4);
				pcItem.category         = cur2.getString(5);
				pcItem.pubDate          = getDateFromDb(cur2.getString(6));
				pcItem.itunes_subtitle  = cur2.getString(7);
				pcItem.itunes_summary   = cur2.getString(8);
				pcItem.itunes_duration  = cur2.getString(9);
				pcItem.mediaUrl         = cur2.getString(10);
				pcItem.mediaLength      = cur2.getString(11);
				pcItem.mediaType        = cur2.getString(12);
				pcItem.filename         = cur2.getString(13);
				pc.podcastItems.add(pcItem);
			}
			ret.add(pc);
			cur2.close();
		}
		cur.close();

		return ret;
	}
}
