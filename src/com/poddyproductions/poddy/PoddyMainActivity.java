package com.poddyproductions.poddy;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class PoddyMainActivity extends ListActivity	{

	public static final String EXTRA_PODCASTURL = "com.poddyproductions.podcasturl";

	private ArrayList<StreamViewEntry> streamsArray;
	private PoddyStreamsOpenHelper dbOpenHelper = new PoddyStreamsOpenHelper(this); 
	private ArrayAdapter<StreamViewEntry> adapter;

	
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

		streamsArray = dbOpenHelper.getAllKnownStreams();
		adapter = new ArrayAdapter<StreamViewEntry>(this,android.R.layout.simple_list_item_1, streamsArray);

		getListView().setAdapter(adapter);
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
			StreamViewEntry entry = new StreamViewEntry(url, new Date());
			streamsArray.add(entry);
			dbOpenHelper.add(entry);
			adapter.notifyDataSetChanged();
			
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
