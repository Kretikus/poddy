package com.poddyproductions.poddy;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class PoddyMainActivity extends ListActivity	{

	public static final String TAG = "PoddyMainActivity";
	public static final String EXTRA_PODCASTURL = "com.poddyproductions.podcasturl";
	public static PoddyStreamsOpenHelper dbOpenHelper;
	
	private ArrayList<Podcast> streamsArray;
	private ArrayAdapter<Podcast> adapter;


	String fetchHTTP(String urlString) {
		String ret = new String();
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(urlString);
			HttpResponse rp = hc.execute(get);
			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ret = EntityUtils.toString(rp.getEntity());
			}
		} catch(IOException e) {
			Log.e(TAG, "Could not do http request: " + e.toString() );
		}  
		return ret;
	}
	
	
	private class AddStreamTask extends AsyncTask<String, Void, String> {
		private String feedUrl;
		
		protected String doInBackground(String... urls) {
			feedUrl = urls[0];
			return fetchHTTP(urls[0]);
		}
		
		protected void onPostExecute(String result) {
			Podcast entry = new Podcast(feedUrl, result);
			streamsArray.add(entry);
			dbOpenHelper.add(entry);
			adapter.notifyDataSetChanged();
		}
	}

	protected void showAlert(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.invalid_url_message)
				.setTitle(R.string.invalid_url_title)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poddy_main);
		dbOpenHelper = new PoddyStreamsOpenHelper(this);

		streamsArray = dbOpenHelper.getAllPodcasts();
		adapter = new ArrayAdapter<Podcast>(this,android.R.layout.simple_list_item_1, streamsArray);
		getListView().setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {
		dbOpenHelper.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_poddy_main, menu);
		return true;
	}

	public void onAddUrl(View view) {
		EditText editText = (EditText) findViewById(R.id.enter_url);
		String url = editText.getText().toString();
		if (!url.isEmpty() && (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) ) {
			new AddStreamTask().execute(url);
		} else{
			showAlert(getString(R.string.invalid_url_title), getString(R.string.invalid_url_message));
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Object o = l.getItemAtPosition(position);
		Intent intent = new Intent(this, DisplayPodcast.class);
		intent.putExtra(EXTRA_PODCASTURL, o.toString());
		startActivity(intent);
	}

}
