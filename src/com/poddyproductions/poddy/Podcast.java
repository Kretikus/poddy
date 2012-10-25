package com.poddyproductions.poddy;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class Podcast {

	private static final String TAG = "DisplayPodcast";
	private static final SimpleDateFormat podCastFormat  = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	private Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e(TAG, e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e(TAG, e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
                // return DOM
            return doc;
    }
	

	public final String getElementValue(Node elem) {
	         Node child;
	         if( elem != null){
	             if (elem.hasChildNodes()){
	                 for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                     if( child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE ) {
	                         return child.getNodeValue();
	                     }
	                 }
	             }
	         }
	         return "";
	  } 

	public String getValue(Element item, String str) {
	    NodeList n = item.getElementsByTagName(str);
	    return this.getElementValue(n.item(0));
	}

	static final String CHANNEL_ITEM = "channel"; // parent node
	static final String ITEMS_ITEM   = "item"; // parent node

	public class PodcastItem {
		public String title;
		public String link;
		public String guid;
		public String description;
		public String category;
		public Date   pubDate;
		public String itunes_subtitle;
		public String itunes_summary;
		public String itunes_duration;
		public String mediaUrl;
		public String mediaLength;
		public String mediaType;
		public String filename;

		public String toString() {
			return title;
		}
	}

	public long   id;
	public String feedUrl;
	public Date   lastUpdated;
	public String title;
	public String description;
	public String link;
	public String language;
	public String copyright;
	public String lastBuildDate;
	public String pubDate;
	public String docs;
	public String webMaster;
	public String itunes_subtitle;
	public String itunes_summary;
	public String itunes_image;

	public String toString() {
		return title;
	}

	private Date createDate(String str) {
		Date date;
		try {
			date = podCastFormat.parse(str);
		} catch(ParseException ex) {
			Log.e(TAG, "Cannot format time!");
			date = new Date();
		}
		return date;
	}
	
	public ArrayList<PodcastItem> podcastItems = new ArrayList<PodcastItem>();

	public Podcast() { id = -1; }

	public Podcast(String feedUrl, String xml) {
		id = -1;
		lastUpdated = new Date();
		this.feedUrl = feedUrl;
		Document doc = getDomElement(xml);
		if (doc != null) {
			NodeList nl = doc.getElementsByTagName(CHANNEL_ITEM);
			if(nl.getLength() < 1) return;
			
			Element rootElement = (Element)nl.item(0);
			title           = getValue(rootElement, "title");
			description     = getValue(rootElement, "description");
			link            = getValue(rootElement, "link");
			language        = getValue(rootElement, "language");
			copyright       = getValue(rootElement, "copyright");
			lastBuildDate   = getValue(rootElement, "lastBuildDate");
			pubDate         = getValue(rootElement, "pubDate");
			docs            = getValue(rootElement, "docs");
			webMaster       = getValue(rootElement, "webMaster");
			itunes_subtitle = getValue(rootElement, "itunes:subtitle");
			itunes_summary  = getValue(rootElement, "itunes:summary");
			
			NodeList imgEnclosureList   = rootElement.getElementsByTagName("itunes:image");
			if (imgEnclosureList.getLength() > 0) {
				Element enclosureElement = (Element)imgEnclosureList.item(0);
				itunes_image = enclosureElement.getAttribute("href");
			}

//			Log.e(TAG, "title: "       + title);
//			Log.e(TAG, "description: " + description);
//			Log.e(TAG, "link: "       + link);
//			Log.e(TAG, "language: " + language);
//			Log.e(TAG, "copyright: "       + copyright);
//			Log.e(TAG, "lastBuildDate: " + lastBuildDate);
//			Log.e(TAG, "pubDate: "       + pubDate);
//			Log.e(TAG, "docs: " + docs);
//			Log.e(TAG, "webMaster: " + webMaster);
//			Log.e(TAG, "itunes_subtitle: "       + itunes_subtitle);
//			Log.e(TAG, "itunes_summary: " + itunes_summary);

			NodeList items = doc.getElementsByTagName(ITEMS_ITEM);
			//Log.e(TAG, "Doing: " + items.getLength());
			for (int i = 0; i < items.getLength(); i++) {
				//Log.e(TAG, "I: " + Integer.valueOf(i));
				Element element = (Element)items.item(i);
				PodcastItem item = new PodcastItem();
				item.title           = getValue(element, "title");
				item.link            = getValue(element, "link");
				item.guid            = getValue(element, "guid");
				item.description     = getValue(element, "description");
				item.category        = getValue(element, "category");
				String pubDate       = getValue(element, "pubDate");
				item.pubDate         = createDate(pubDate);
				item.itunes_subtitle = getValue(element, "itunes:subtitle");
				item.itunes_summary  = getValue(element, "itunes:summary");
				item.itunes_duration = getValue(element, "itunes:duration");

				NodeList enclosureList   = element.getElementsByTagName("enclosure");
				if (enclosureList.getLength() > 0) {
					Element enclosureElement = (Element)enclosureList.item(0);
					item.mediaUrl    = enclosureElement.getAttribute("url");
					item.mediaLength = enclosureElement.getAttribute("length");
					item.mediaType   = enclosureElement.getAttribute("type");
				}
				
//				Log.e(TAG, "title: "          + item.title);
//				Log.e(TAG, "description: "    + item.description);
//				Log.e(TAG, "link: "           + item.link);
//				Log.e(TAG, "guid: "           + item.guid);
//				Log.e(TAG, "category: "       + item.category);
//				Log.e(TAG, "pubDate: "        + item.pubDate);
//				Log.e(TAG, "itunes:subtitle: "+ item.itunes_subtitle);
//				Log.e(TAG, "itunes:summary: " + item.itunes_summary);
//				Log.e(TAG, "itunes:duration: "+ item.itunes_duration);
//				Log.e(TAG, "mediaUrl: "       + item.mediaUrl);
//				Log.e(TAG, "mediaLength: "    + item.mediaLength);
//				Log.e(TAG, "mediaType: "      + item.mediaType);

				podcastItems.add(item);
			}
		}
	}
}
