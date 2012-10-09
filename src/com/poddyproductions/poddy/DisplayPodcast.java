package com.poddyproductions.poddy;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class DisplayPodcast extends ListActivity {

	private static final String TAG = "DisplayPodcast";
	public  static final String EXTRA_PODCASTEPISODE = "com.poddyproductions.podcast_episode";
	
	public static Podcast currentPodcast;
	
	private PodcastAdapter adapter;
	private Podcast podcast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_podcast);

		Intent intent = getIntent();
		String title  = intent.getStringExtra(PoddyMainActivity.EXTRA_PODCASTURL);
		podcast       = PoddyMainActivity.dbOpenHelper.getAllPodcastWithTitle(title);

		adapter = new PodcastAdapter(this, podcast);
		getListView().setAdapter(adapter);

		setTitle(podcast.title);
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Object o = l.getItemAtPosition(position);
		Intent intent = new Intent(this, DisplayPodcastEpisode.class);
		intent.putExtra(EXTRA_PODCASTEPISODE, o.toString());
		Log.i(TAG, "Clicked on " + o.toString());
		currentPodcast = podcast;
		startActivity(intent);
	}
}
