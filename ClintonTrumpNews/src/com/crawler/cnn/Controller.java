package com.crawler.cnn;

import com.main.Properties;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	
	public Controller(){
		
	}
	
	public void startCrawler() throws Exception{
		
		// Initializing the default value from Properties Files
		
		String crawlStorageFolder = Properties.root;
		int numberOfCrawlers = Properties.numberOfCrawlers;
		int maxCrawlingDepth = Properties.maximumCrawlingDepth;

		// configuring the crawler

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setResumableCrawling(false);
		config.setMaxDepthOfCrawling(maxCrawlingDepth);

		// Instantiate the controller for this crawl.

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		// Seeds are the URLs that are fetched and then the crawler starts
		// following links which are found in these pages

		controller.addSeed("http://edition.cnn.com/politics");

		// Start the crawler

		controller.start(MyCrawler.class, numberOfCrawlers);
	}

}