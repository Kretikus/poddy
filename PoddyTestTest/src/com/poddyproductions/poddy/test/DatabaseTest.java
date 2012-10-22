package com.poddyproductions.poddy.test;

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
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		dbOpenHelper.dropTables();
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
		assertTrue(podcast.podcastItems.size() == 49);

		Podcast.PodcastItem item = podcast.podcastItems.get(0);
		assertTrue(item.title.compareTo("RZ047 Die Venus") == 0 );
		assertTrue(item.link.compareTo("http://feedproxy.google.com/~r/raumzeit-podcast/~3/1gKUvr85e6U/") == 0 );
		assertTrue(item.guid.compareTo("http://raumzeit-podcast.de/?p=1091") == 0 );
		assertTrue(item.description.compareTo("Nachdem wir in der letzten Raumzeit die Herausforderungen eines Fluges zur Venus besprochen haben, r�cken wir in dieser Ausgabe den Planeten selbst in den Mittelpunkt: die Venus. Der �h�llische Nachbar der Erde�, mit seiner t�dlichen Atmosph�re und umgedrehter Rotationsrichtung, gibt &#8230; <a href=\"http://raumzeit-podcast.de/2012/10/05/rz047-die-venus/\">Continue reading <span class=\"meta-nav\">&#8594;</span></a>") == 0 );
		assertTrue(item.category.compareTo("DLR") == 0 );
		assertTrue(item.pubDate.compareTo("Fri, 05 Oct 2012 08:00:56 +0000") == 0 );
		assertTrue(item.itunes_subtitle.compareTo("Nachdem wir in der letzten Raumzeit die Herausforderungen eines Fluges zur Venus besprochen haben, r�cken wir in dieser Ausgabe den Planeten selbst in den Mittelpunkt: die Venus. Der �h�llische Nachbar der Erde�,") == 0 );
		assertTrue(item.itunes_summary.compareTo("Nachdem wir in der letzten Raumzeit die Herausforderungen eines Fluges zur Venus besprochen haben, r�cken wir in dieser Ausgabe den Planeten selbst in den Mittelpunkt: die Venus. Der �h�llische Nachbar der Erde�, mit seiner t�dlichen Atmosph�re und umgedrehter Rotationsrichtung, gibt der Wissenschaft noch viele R�tsel auf.Obwohl die Venus au�erhalb der habitablen Zone liegt, mag der Planet einmal die Voraussetzungen f�r Leben geboten haben. Aus diesem Grund ist die Venus f�r die Forschung auch besonders interessant: k�nnen R�ckschl�sse auf eventuelle Folgen einer Erderw�rmung gezogen werden?Dem� Planeten fehlen verschiedene Schutzmechanismen und Voraussetzungen, die auf der Erde Leben erm�glichen. Die Oberfl�chentemperatur von ungef�hr 500 Grad Celsius und ein atmosph�rischer Druck von 92 bar machen es schwierig, sich dem Planeten zu n�hern. Bisher hat noch kein Landesystem mehr als ein paar Minuten auf der Vesus-Oberfl�che �berlebt.Im Gespr�ch mit Tim Pritlove berichtet J�rn Helbert vom Institut f�r Planetenforschung des DLR �ber den aktuellen Kenntnisstand zur Venus und dar�ber, welche Fragestellungen von der wissenschaftlichen Gemeinde derzeit in den Mittelpunkt ger�ckt werden.Au�erdem stellt er die Arbeit der Experimentellen Planetenphysik vor, die eng mit den Planetenmissionen der Raumfahrtagenturen verzahnt ist.Dauer:�01:09:46Aufnahme:�September 2012Download:�MP3,�MP4, Ogg VorbisLinks:\tWP: Venus\tWP: Morgenstern\tDLR: J�rn Helbert\tTwitter Account von J�rn Helbert\tDLR: Institut f�r Planetenforschung: Experimentelle Planetenphysik\tWP: Technische Universit�t Braunschweig\tWP: Cluster\tWP: Mariner\tWP: Venera-Mission\tWP: Viking\tWP: Magellan\tWP: Venus Express\tRaumzeit: RZ046 Venus Express\tWP: Habitable Zone\tWP: Schwefels�ure\tRaumzeit: RZ039 Der Mond\tWP: Vulkanismus\tWP: Seismograph\tWP: Akatsuki (Venus Climate Orbiter)\tJAXA | Japan Aerospace Exploration Agency") == 0 );
		assertTrue(item.itunes_duration.compareTo("1:09:46") == 0 );
		assertTrue(item.mediaUrl.compareTo("http://feedproxy.google.com/~r/raumzeit-podcast/~5/ciAeoMf56mo/rz047-die-venus.m4a") == 0 );
		assertTrue(item.mediaLength.compareTo("31137786") == 0 );
		assertTrue(item.mediaType.compareTo("audio/x-m4a") == 0 );
		//assertTrue(item.filename.compareTo("") == 0 );
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
		assertTrue(podcast2.podcastItems.size() == 49);
		
		Podcast.PodcastItem item = podcast2.podcastItems.get(0);
		assertTrue(item.title.compareTo("RZ047 Die Venus") == 0 );
		assertTrue(item.link.compareTo("http://feedproxy.google.com/~r/raumzeit-podcast/~3/1gKUvr85e6U/") == 0 );
		assertTrue(item.guid.compareTo("http://raumzeit-podcast.de/?p=1091") == 0 );
		assertTrue(item.description.compareTo("Nachdem wir in der letzten Raumzeit die Herausforderungen eines Fluges zur Venus besprochen haben, r�cken wir in dieser Ausgabe den Planeten selbst in den Mittelpunkt: die Venus. Der �h�llische Nachbar der Erde�, mit seiner t�dlichen Atmosph�re und umgedrehter Rotationsrichtung, gibt &#8230; <a href=\"http://raumzeit-podcast.de/2012/10/05/rz047-die-venus/\">Continue reading <span class=\"meta-nav\">&#8594;</span></a>") == 0 );
		assertTrue(item.category.compareTo("DLR") == 0 );
		assertTrue(item.pubDate.compareTo("Fri, 05 Oct 2012 08:00:56 +0000") == 0 );
		assertTrue(item.itunes_subtitle.compareTo("Nachdem wir in der letzten Raumzeit die Herausforderungen eines Fluges zur Venus besprochen haben, r�cken wir in dieser Ausgabe den Planeten selbst in den Mittelpunkt: die Venus. Der �h�llische Nachbar der Erde�,") == 0 );
		assertTrue(item.itunes_summary.compareTo("Nachdem wir in der letzten Raumzeit die Herausforderungen eines Fluges zur Venus besprochen haben, r�cken wir in dieser Ausgabe den Planeten selbst in den Mittelpunkt: die Venus. Der �h�llische Nachbar der Erde�, mit seiner t�dlichen Atmosph�re und umgedrehter Rotationsrichtung, gibt der Wissenschaft noch viele R�tsel auf.Obwohl die Venus au�erhalb der habitablen Zone liegt, mag der Planet einmal die Voraussetzungen f�r Leben geboten haben. Aus diesem Grund ist die Venus f�r die Forschung auch besonders interessant: k�nnen R�ckschl�sse auf eventuelle Folgen einer Erderw�rmung gezogen werden?Dem� Planeten fehlen verschiedene Schutzmechanismen und Voraussetzungen, die auf der Erde Leben erm�glichen. Die Oberfl�chentemperatur von ungef�hr 500 Grad Celsius und ein atmosph�rischer Druck von 92 bar machen es schwierig, sich dem Planeten zu n�hern. Bisher hat noch kein Landesystem mehr als ein paar Minuten auf der Vesus-Oberfl�che �berlebt.Im Gespr�ch mit Tim Pritlove berichtet J�rn Helbert vom Institut f�r Planetenforschung des DLR �ber den aktuellen Kenntnisstand zur Venus und dar�ber, welche Fragestellungen von der wissenschaftlichen Gemeinde derzeit in den Mittelpunkt ger�ckt werden.Au�erdem stellt er die Arbeit der Experimentellen Planetenphysik vor, die eng mit den Planetenmissionen der Raumfahrtagenturen verzahnt ist.Dauer:�01:09:46Aufnahme:�September 2012Download:�MP3,�MP4, Ogg VorbisLinks:\tWP: Venus\tWP: Morgenstern\tDLR: J�rn Helbert\tTwitter Account von J�rn Helbert\tDLR: Institut f�r Planetenforschung: Experimentelle Planetenphysik\tWP: Technische Universit�t Braunschweig\tWP: Cluster\tWP: Mariner\tWP: Venera-Mission\tWP: Viking\tWP: Magellan\tWP: Venus Express\tRaumzeit: RZ046 Venus Express\tWP: Habitable Zone\tWP: Schwefels�ure\tRaumzeit: RZ039 Der Mond\tWP: Vulkanismus\tWP: Seismograph\tWP: Akatsuki (Venus Climate Orbiter)\tJAXA | Japan Aerospace Exploration Agency") == 0 );
		assertTrue(item.itunes_duration.compareTo("1:09:46") == 0 );
		assertTrue(item.mediaUrl.compareTo("http://feedproxy.google.com/~r/raumzeit-podcast/~5/ciAeoMf56mo/rz047-die-venus.m4a") == 0 );
		assertTrue(item.mediaLength.compareTo("31137786") == 0 );
		assertTrue(item.mediaType.compareTo("audio/x-m4a") == 0 );
		//assertTrue(item.filename.compareTo("") == 0 );
		
	}
}
