package com.crawler.cnn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.main.Properties;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	//Filter out the unncessary information
	
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp3|zip|gz))$");
	final static Logger logger = Logger.getLogger(MyCrawler.class);
   
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
		boolean flag  = false;
		
		for(int i = 0 ; i <= Properties.minThresholdDay ; i ++){
			DateTime lastWeek = new DateTime().minusDays(i);
			StringBuilder sb = new StringBuilder();
			sb.append("http://edition.cnn.com/");
			sb.append(lastWeek.getYear());
			sb.append("/");
			if(lastWeek.getMonthOfYear()<10){
				sb.append("0");
			}
			sb.append(lastWeek.getMonthOfYear());
			sb.append("/");
			if(lastWeek.getDayOfMonth()<10){
				sb.append(0);
			}
			sb.append(lastWeek.getDayOfMonth());
			sb.append("/politics");
			
			if(url.startsWith(sb.toString())){
				flag = true;
			}
		}
		if(flag == false) return;
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();
			extract(html);
			
			
		}
	}
    
     // Here in this method, we are extracting information from the html content
     
	public void extract(String html) {

		Document doc = Jsoup.parse(html);

		String title = doc.select("meta[itemprop=headline]").attr("content");
		String authorName = doc.select("meta[itemprop=author]").attr("content");
		String date = doc.select("meta[itemprop=dateCreated]").attr("content");
		
		String da = date.replace('T',' ').replace('Z',' ').trim();
		DateTime date2 = DateTime.parse(da,DateTimeFormat.forPattern("yyyy-mm-dd HH:mm:ss"));
		
		//if(date2.isAfter(new DateTime().minusDays(Properties.minThresholdDay))) return;
		
		Elements contents = doc.select("[class=zn-body__paragraph]");
		Iterator<Element> it = contents.iterator();
		ArrayList<String> texts = new ArrayList<String>();
		while (it.hasNext()) {
			Element c = it.next();
			String temp = c.text();
			texts.add(temp);
		}
		System.out.println("Reading.. " + date2.toString() + " " + title);
		
		
		Controller.posts.add(new CNNPost(title, authorName, date, texts));

	}
     
}