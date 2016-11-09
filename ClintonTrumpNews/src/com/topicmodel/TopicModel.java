package com.topicmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.crawler.cnn.CNNPost;
import com.crawler.cnn.Preprocessing;
import com.crawler.cnn.RemoveStopWord;
import com.main.Properties;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;


public class TopicModel {
	public LdaModel tModel;
	 ArrayList<CNNPost> posts;
	 Map<Integer, Integer> maxDocTopicId = null;
	 
	public TopicModel(ArrayList<CNNPost> posts){
		this.posts = posts;
	}
	public TopicModel(){
		
	}
	
	public void generateTopicModel(boolean flag) {

		int numIteration = Properties.ldaNumIter;
		int numThreads = Properties.ldaNumThread;
		int numTopics = Properties.ldaNumTopic;
		String corpusFile = Properties.ldaCorpusPath;

		if (!new File(Properties.ldaCorpusPath).exists() && !flag) {
			topicMalletFileGeneration();
		}
		TopicGeneration model = new TopicGeneration();
		if (new File(Properties.ldaCorpusPath).exists() && flag) {
			model.loadTopoicModel();
			tModel = model.gettModel();
		} else {
			model.TopoicModel(corpusFile, numTopics, numThreads, numIteration);
			model.saveTopicModel();
			tModel = model.gettModel();
		}

	}

	public ArrayList<String> showTopic(int id,int topic){
		
		 ArrayList<String> words = new ArrayList<String>();
		
		 ArrayList<TreeSet<IDSorter>> topicSortedWords = tModel.model.getSortedWords();
		 Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
		 
		 Alphabet dataAlphabet = tModel.instances.getDataAlphabet();
		 
		 int rank = 0;
		 while (iterator.hasNext() && rank < 10) {
	            IDSorter idCountPair = iterator.next();
	            System.out.println(dataAlphabet.lookupObject(idCountPair.getID()).toString());
	            rank++;
	            words.add(dataAlphabet.lookupObject(idCountPair.getID()).toString());
	        }
		 
		 return words;
	}
	
	public void topicMalletFileGeneration() {

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(Properties.ldaCorpusPath));

			for (int i = 0; i < posts.size(); i++) {
				CNNPost p = posts.get(i);
				StringBuilder sb = new StringBuilder();
				sb.append(p.getTitle());
				for (String s : p.getBody()) {
					sb.append(s);
				}
				String body = sb.toString();
				RemoveStopWord st = new RemoveStopWord(Preprocessing.loadStopWord());
				body = st.doRemove(body);
				writer.write(i + "  X  " + body);
				writer.newLine();
			}
			System.out.println("Finish Constructing LDA Corpus file.  " + posts.size());
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getTopic(ParallelTopicModel model, int id){
		int k = -1;
		int numTopics = model.getNumTopics();
		double[] topicDistribution = model.getTopicProbabilities(id);
		double maxi = -1;
		int maxiTopic = -1;
		
		for (int topic = 0; topic < numTopics; topic++) {
			if (maxi < topicDistribution[topic]) {
				maxi = topicDistribution[topic];
				maxiTopic = topic;
			}
		}
		k = maxiTopic;
		return k;
	}
	
	public ArrayList<Integer> getTopTopic(int id){
		int k = -1;
		ParallelTopicModel model = tModel.model;
		int numTopics = model.getNumTopics();
		double[] topicDistribution = model.getTopicProbabilities(id);
		double maxi = -1;
		int maxiTopic = -1;
		class Score{
			int topic;
			double distr;
			public Score(int topic,double distr){
				this.topic = topic;
				this.distr = distr;
			}
		}
		ArrayList<Score> docScore = new ArrayList<Score>();
		for (int topic = 0; topic < numTopics; topic++) {
			docScore.add(new Score(topic,topicDistribution[topic]));
		}
		Collections.sort(docScore,new Comparator<Score>() {

			@Override
			public int compare(Score c1, Score c2) {
				return Double.compare(c2.distr, c1.distr);
			}
		});
		ArrayList<Integer> topTopic = new ArrayList<Integer>();
		for(Score s : docScore){
			topTopic.add(s.topic);
		}
		k = maxiTopic;
		return topTopic;
	}
	
	public double getTopicDistribution(ParallelTopicModel model, int id){
		double distribution = -1;
		int numTopics = model.getNumTopics();
		double[] topicDistribution = model.getTopicProbabilities(id);
		double maxi = -1;
		int maxiTopic = -1;
		
		for (int topic = 0; topic < numTopics; topic++) {
			if (maxi < topicDistribution[topic]) {
				maxi = topicDistribution[topic];
				maxiTopic = topic;
			}
		}
		distribution = maxi;
		return distribution;
	}
	
	
	public void documentTopicProbability(ParallelTopicModel model, Map<String, Double> maxDocTopicDistribution,
			Map<Integer, Integer> maxDocTopicId) {

		int numDocs = model.getData().size();
		int numTopics = model.getNumTopics();

		double maxi = -1;
		int maxiTopic = -1;

		for (int j = 0; j < numDocs; j++) {

			double[] topicDistribution = model.getTopicProbabilities(j);
			maxi = -1;
			maxiTopic = -1;

			for (int topic = 0; topic < numTopics; topic++) {
				if (maxi < topicDistribution[topic]) {
					maxi = topicDistribution[topic];
					maxiTopic = topic;
				}
			}

			int docId = Integer.parseInt(model.getData().get(j).instance.getName().toString());
			maxDocTopicId.put(docId, maxiTopic);

		}
	}
	
	public  void readObject(){
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
	
	public static void main ( String arg[] ){
		
		
	}

}
