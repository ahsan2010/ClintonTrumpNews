package com.crawler.cnn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.text.AbstractDocument.Content;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	//Filter out the unncessary information
	
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp3|zip|gz))$");

    public ArrayList<CNNPost> posts = new ArrayList<CNNPost>();
    //Only selected pages should be visited
    
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		if (!href.startsWith("http://edition.cnn.com/")) {
			return false;
		}

		return true;
	}

	
    //  This function is called when a page is fetched and ready to be processed by your program.
      
     
     @Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if (!url.startsWith("http://edition.cnn.com/2016/11/07/politics")) {
			return;
		}

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		//	String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			extract(html);
		}
	}
    
	public void extract(String html) {

		Document doc = Jsoup.parse(html);

		Elements contents = doc.select("[class=zn-body__paragraph]");
		Iterator<Element> it = contents.iterator();
		ArrayList<String> texts = new ArrayList<String>();
		while (it.hasNext()) {
			Element c = it.next();
			String temp = c.text();
			if (!temp.trim().isEmpty() && !temp.contains("Loading weather data ...")) {
				texts.add(temp);
			}
		}

		String title = doc.select("meta[itemprop=headline]").attr("content");
		String author = doc.select("meta[itemprop=author]").attr("content");
		String date = doc.select("meta[itemprop=dateCreated]").attr("content");

		System.out.println(title);
		System.out.println(author);
		System.out.println(date);

		for (String s : texts) {
			System.out.println(s);
		}

		if (date != null && !date.trim().isEmpty()) {
			try {
				DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
				DateTime dt = DateTime.parse(date, formatter);

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

	}
     
}