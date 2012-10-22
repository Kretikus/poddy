package com.poddyproductions.poddy.test;

import java.util.Date;

import android.test.AndroidTestCase;

import com.poddyproductions.poddy.Podcast;
import com.poddyproductions.poddy.PoddyStreamsOpenHelper;

public class DatabaseTest extends AndroidTestCase {

	private PoddyStreamsOpenHelper dbOpenHelper; 
	private Podcast podcast;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dbOpenHelper = new PoddyStreamsOpenHelper(this.getContext(), "poddyTestDb");
		podcast = new Podcast("http://feed.example.com/", ExampleXML.exampleXML);
	}

	public void testPodcastFromXML() {
		assertTrue(podcast.title.compareTo("Raumzeit") == 0);
		assertTrue(podcast.link.compareTo("http://raumzeit-podcast.de") == 0);
		assertTrue(podcast.description.compareTo("Der Podcast �ber Raumfahrt von ESA und DLR - mit Tim Pritlove") == 0);
		assertTrue(podcast.language.compareTo("de") == 0 );
		assertTrue(podcast.copyright.compareTo("http://creativecommons.org/licenses/by-nc-nd/3.0/de/") == 0 );
		assertTrue(podcast.lastBuildDate.compareTo("Fri, 05 Oct 2012 10:45:13 +0000") == 0 );
		assertTrue(podcast.pubDate.compareTo("Fri, 05 Oct 2012 08:00:56 +0000") == 0 );
		assertTrue(podcast.docs.compareTo("") == 0 );
		assertTrue(podcast.webMaster.compareTo("") == 0 );
		assertTrue(podcast.itunes_subtitle.compareTo("Der Podcast �ber Raumfahrt von ESA und DLR � mit Tim Pritlove") == 0 );
		assertTrue(podcast.itunes_summary.compareTo("Raumzeit ist eine Serie von Gespr�chen mit Wissenschaftlern, Ingenieuren und anderen Machern von DLR und ESA �ber Raumfahrt. Jede Episode r�ckt einen Themenbereich in den Fokus und diskutiert ausf�hrlich alle Aspekte und Details.") == 0 );
		assertTrue(podcast.itunes_image.compareTo("http://meta.metaebene.me/media/raumzeit/raumzeit_icon_600px_2.0.png") == 0 );
	}
	
	public void testPodcastToAndFromDatabase() {
		dbOpenHelper.add(podcast);

		Podcast podcast2 = dbOpenHelper.getAllPodcastWithTitle(podcast.title);

		assertTrue(podcast2.title.compareTo("Raumzeit") == 0);
		assertTrue(podcast2.link.compareTo("http://raumzeit-podcast.de") == 0);
		assertTrue(podcast2.description.compareTo("Der Podcast �ber Raumfahrt von ESA und DLR - mit Tim Pritlove") == 0);
		assertTrue(podcast2.language.compareTo("de") == 0 );
		assertTrue(podcast2.copyright.compareTo("http://creativecommons.org/licenses/by-nc-nd/3.0/de/") == 0 );
		assertTrue(podcast2.lastBuildDate.compareTo("Fri, 05 Oct 2012 10:45:13 +0000") == 0 );
		assertTrue(podcast2.pubDate.compareTo("Fri, 05 Oct 2012 08:00:56 +0000") == 0 );
		assertTrue(podcast2.docs.compareTo("") == 0 );
		assertTrue(podcast2.webMaster.compareTo("") == 0 );
		assertTrue(podcast2.itunes_subtitle.compareTo("Der Podcast �ber Raumfahrt von ESA und DLR � mit Tim Pritlove") == 0 );
		assertTrue(podcast2.itunes_summary.compareTo("Raumzeit ist eine Serie von Gespr�chen mit Wissenschaftlern, Ingenieuren und anderen Machern von DLR und ESA �ber Raumfahrt. Jede Episode r�ckt einen Themenbereich in den Fokus und diskutiert ausf�hrlich alle Aspekte und Details.") == 0 );
		assertTrue(podcast2.itunes_image.compareTo("http://meta.metaebene.me/media/raumzeit/raumzeit_icon_600px_2.0.png") == 0 );
	}

}
