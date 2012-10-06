package com.poddyproductions.poddy;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class DisplayPodcast extends ListActivity {

	private static final String TAG = "DisplayPodcast";

	private ArrayAdapter<Podcast.PodcastItem> adapter;
	private Podcast podcast;

	String fetchHTTP(String urlString) {
		String ret = new String();
		try
		{
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(urlString);
			HttpResponse rp = hc.execute(get);
			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				ret = EntityUtils.toString(rp.getEntity());
			}
		}catch(IOException e){
			Log.e(TAG, "Could not do http request: " + e.toString() );
		}  
		return ret;
	}
	
	
	private class GetPageTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return fetchHTTP(urls[0]);
		}
		
		protected void onPostExecute(String result) {
			//TODO: implement
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_podcast);

		//Intent intent = getIntent();
		//String urlString = intent.getStringExtra(PoddyMainActivity.EXTRA_PODCASTURL);
		//new GetPageTask().execute(urlString);
		podcast = new Podcast(ExampleXML.exampleXML);
		adapter = new ArrayAdapter<Podcast.PodcastItem>(this,android.R.layout.simple_list_item_1, podcast.podcastItems);
		getListView().setAdapter(adapter);
		
		setTitle(podcast.title);
	}
}
