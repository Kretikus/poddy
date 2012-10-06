package com.poddyproductions.poddy;

import java.util.Date;

public class StreamViewEntry {

	private String url;
	private Date   lastUpdated; 
	
	StreamViewEntry(String urlArg, Date date) {
		url  = urlArg;
		lastUpdated = date;
	}
	
	public String toString()     { return url;         }
	public String getUrl()       { return url;         }
	public Date getLastUpdated() { return lastUpdated; }
}
