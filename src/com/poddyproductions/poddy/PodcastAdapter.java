package com.poddyproductions.poddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class PodcastAdapter extends BaseAdapter {

	private Context context;
	private Podcast podcasts;
	
	PodcastAdapter(Context context, Podcast podcasts) {
		this.context  = context;
		this.podcasts = podcasts;
	}
	
	public int getCount() {
		return podcasts.podcastItems.size();
	}

	public Object getItem(int position) {
		return podcasts.podcastItems.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		 TwoLineListItem twoLineListItem;

	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            twoLineListItem = (TwoLineListItem) inflater.inflate(
	                    android.R.layout.simple_list_item_2, null);
	        } else {
	            twoLineListItem = (TwoLineListItem) convertView;
	        }

	        TextView text1 = twoLineListItem.getText1();
	        TextView text2 = twoLineListItem.getText2();

	        text1.setText(podcasts.podcastItems.get(position).toString() );
	        text2.setText("" + podcasts.podcastItems.get(position).mediatype);

	        return twoLineListItem;
	}

}
