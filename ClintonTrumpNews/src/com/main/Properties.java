package com.main;

import org.apache.log4j.Logger;

public class Properties {

    
	public static String root = "/home/amee/git/clintonTrumpNews/ClintonTrumpNews"; // Set the project directory as the root folder
	public static int numberOfCrawlers = 7;
	public static int maximumCrawlingDepth = 2;
	public static String stop_word_path = root+"/others/stopList.csv";
	public static String save_posts_path = root+"/others/posts.ser";
	public final static Logger logger = Logger.getLogger("ClintonTrumpNews Logger");
}
