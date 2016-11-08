package com.crawler.cnn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.main.Properties;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static ArrayList<CNNPost> posts = new ArrayList<CNNPost>();
	public ArrayList<CNNPost> clintonPosts = new ArrayList<CNNPost>();
	public ArrayList<CNNPost> trumpPosts = new ArrayList<CNNPost>();
	final static Logger logger = Logger.getLogger(Controller.class);
	
	public Controller() {

	}

	String getPostBody(ArrayList<String> body){
		
		StringBuilder sb = new StringBuilder();
		for(String s : body){
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
		
	}
	
	public Map<String,Integer> countWord(String text){
		int total = 0 ;
		Map<String,Integer> wordFreq = new HashMap<String,Integer>();
		wordFreq.put("hilary", 0);wordFreq.put("clinton", 0);
		wordFreq.put("donald", 0);wordFreq.put("trump", 0);    // Initialize them with 0 Frequency
		
		StringTokenizer tokenizer = new StringTokenizer(text, " +-=/{}[]?:@#$%^&*!~0123456789<>");
        while (tokenizer.hasMoreElements()) {
            String word = tokenizer.nextToken().trim().toLowerCase();
            if (word.length() < 3) {
                continue;
            }
        	if(wordFreq.containsKey(word)){
        		wordFreq.put(word, wordFreq.get(word) + 1);
        	}else{
        		wordFreq.put(word, 1);
        	}
        }
		
		return wordFreq;
	}
	
	
	public void selectPosts(){
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		for(CNNPost p : posts){
			
			String body = getPostBody(p.getBody());
			String title = p.getTitle();
			
			Map<String,Integer> wordFreqBody = countWord(body);
			Map<String,Integer> wordFreqTitle = countWord(title);
			
			int clintonScore = 2*(wordFreqTitle.get("hilary")+wordFreqTitle.get("clinton"))+ (wordFreqBody.get("hilary")+wordFreqBody.get("clinton"));
			int trumpScore = 2*(wordFreqTitle.get("donald")+wordFreqTitle.get("trump"))+ (wordFreqBody.get("donald")+wordFreqBody.get("trump"));
			System.out.println(p.getTitle());
			System.out.println("Clinton: " + clintonScore +" Trump: " + trumpScore);
			
			System.out.println("----------------------------------------");
			
			if(clintonScore == 0 && trumpScore == 0)continue;
			
			if(clintonScore > trumpScore){
				clintonPosts.add(p);
			}
			else if(trumpScore > clintonScore){
				trumpPosts.add(p);
			}else{
				clintonPosts.add(p);
				trumpPosts.add(p);
			}
			
		}
		
		System.out.println("Hilary Post: ");
		for(CNNPost p : clintonPosts){
			System.out.println(p.getTitle());
		}
		System.out.println("Trump Post: ");
		for(CNNPost p : trumpPosts){
			System.out.println(p.getTitle());
		}
		
	}
	
	public void startCrawler() throws Exception {

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
		boolean exists = new File(Properties.save_posts_path).exists();
		if(exists){
			readObject();
		}
		else{
			
			long start = System.currentTimeMillis();

			controller.start(MyCrawler.class, numberOfCrawlers);
			long end = System.currentTimeMillis();

			System.out.println("[Crawling CNNPosts: " + (end - start) + " ms]");
			System.out.println();
			writeObject();
		}
		
		System.out.println("Crawling Finishes.");
		
	}
	
	public void writeObject(){
        long start = System.currentTimeMillis();

		try{
			 FileOutputStream fileOut = new FileOutputStream(Properties.save_posts_path);
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);

	            out.writeObject(posts);
	            out.close();
	            fileOut.close();
	            long end = System.currentTimeMillis();

	            System.out.println("Time Saving Posts: " + (end - start) + " ms]");
	            System.out.println();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void readObject(){
        long start = System.currentTimeMillis();

		 try {
	            FileInputStream fileIn = null;
	            String path = Properties.save_posts_path;

	            fileIn = new FileInputStream(path);

	            System.out.println("FileReading...");

	            ObjectInputStream in = new ObjectInputStream(fileIn);
	           
	            posts = (ArrayList<CNNPost>)in.readObject();
	            long end = System.currentTimeMillis();

	            System.out.println("[Loading Saved Posts: " + (end - start) + " ms]");
	            System.out.println();
	            
	            
	            
	            for(CNNPost p : posts){
	            	System.out.println(p.getTitle());
	            }
	            
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	}
	public static void main (String arg[]){
		Controller cl = new Controller();
		try{
			cl.startCrawler();
			cl.selectPosts();
			System.out.println("Finish");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}