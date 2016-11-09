package com.main;

import org.apache.log4j.Logger;

public class Properties {

    
	public static String root = "/home/amee/git/clintonTrumpNews/ClintonTrumpNews"; // Set the project directory as the root folder
	public static int numberOfCrawlers = 7;
	public static int maximumCrawlingDepth = 2;
	public static String stop_word_path = root+"/others/stopList.csv";
	public static String save_posts_path = root+"/others/posts.ser";
	public static String ldaModel = root + "/others/model.lda";
	public static String ldaCorpusPath = root + "/others/cnn.corpus";
	public static String ldaStopWord = root +"/others/en.txt";
	public static int ldaNumIter = 200;
	public static int ldaNumThread = 5;
	public static int ldaNumTopic = 5;
	public static int minThresholdDay = 1;
	public final static Logger logger = Logger.getLogger("ClintonTrumpNews Logger");
	
	public static void setRoot(String path){
		root = path;
	}
	public static String getRoot(){
		return root;
	}
	public static void updatePath(){
		stop_word_path = root+"/others/stopList.csv";
		save_posts_path = root+"/others/posts.ser";
		ldaModel = root + "/others/model.lda";
		ldaStopWord = root +"/others/en.txt";
		ldaCorpusPath = root + "/others/cnn.corpus";
	}
}
