package com.topicmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.main.Properties;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;

public class TopicGeneration{

	
	InstanceList instances = null;
	ParallelTopicModel model = null;
	
	LdaModel tModel;
	
	public void TopoicModel( String corpusFile, int numTopics, int numThreads, int numIteration){
		try{
	       
			long startTime = System.currentTimeMillis();
			
			ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
	        pipeList.add( new CharSequenceLowercase() );
	        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
	        pipeList.add( new TokenSequenceRemoveStopwords(new File(Properties.ldaStopWord), "UTF-8", false, false, false) );
	        pipeList.add( new TokenSequence2FeatureSequence() );
	        
	        this.instances = new InstanceList (new SerialPipes(pipeList));
	        
	        Reader fileReader = new InputStreamReader(new FileInputStream(new File(corpusFile)), "UTF-8");
	        this.instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
	                                               3, 2, 1));
	        
	        this.model = new ParallelTopicModel(numTopics, 1.0, 0.01);

	        this.model.addInstances(instances);
	        
	        this.model.setNumThreads(numThreads);
	        this.model.setNumIterations(numIteration);
	        this.model.estimate();
	        
	        
	        tModel = new LdaModel();
	        tModel.setInstances(instances);
	        tModel.setModel(model);

	        
	        long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			
			System.out.println("Topic Model total Time: [ "+totalTime +" ms ]");

	        
		}catch( Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean saveTopicModel(){
		
		if(this.instances == null || this.model == null){
			System.err.println("No model Trained. Can not save!");
			return false;
		}
		
		try{
			
			LdaModel tModel = new LdaModel();
			tModel.setInstances(this.instances);
			tModel.setModel(this.model);
			
			FileOutputStream fout = new FileOutputStream(Properties.ldaModel);
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(tModel);
			oos.close();
			System.out.println("Save Complete");
			return true;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public  boolean loadTopoicModel(){
		
		try{

			long startTime = System.currentTimeMillis();
			
			FileInputStream fin = new FileInputStream(Properties.ldaModel);
			ObjectInputStream ois = new ObjectInputStream(fin);
			tModel = (LdaModel) ois.readObject();
			ois.close();
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			
			System.out.println("Successfully Load the model: Time [ "+totalTime +" ms ]");
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}

	public LdaModel gettModel() {
		return tModel;
	}
	
	
}
