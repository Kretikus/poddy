package com.poddyproductions.poddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DisplayPodcastEpisode extends Activity {

	private final static String TAG = "DisplayPodcastEpisode";
	private Podcast podcast = null;
	private Podcast.PodcastItem podcastItem = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_podcast_episode);

		Intent intent = getIntent();
		String episode = intent.getStringExtra(DisplayPodcast.EXTRA_PODCASTEPISODE);
		setTitle(episode);

		podcast = DisplayPodcast.currentPodcast;
		
		for(int i = 0; i < podcast.podcastItems.size(); i++) {
			Podcast.PodcastItem item = podcast.podcastItems.get(i);
			if (episode.equals(item.toString())) {
				podcastItem = podcast.podcastItems.get(i);
				break;
			}
		}

		if (podcastItem == null) {
			Log.e(TAG, "No valid podcast item found! Looked for: " + episode);
			return;
		}

		TextView v = (TextView)findViewById(R.id.episode_info);
		v.setText(podcastItem.description);
		
	}
}
