package com.crawler.cnn;

import java.io.Serializable;
import java.util.ArrayList;

public class CNNPost implements Serializable{

	private String title;
	private ArrayList<String> body;
	private String authorName;
	private String date;
	
	
	public CNNPost(String title, String authorName,String date, ArrayList<String> body){
		this.title = title;
		this.authorName = authorName;
		this.date = date;
		this.body = body;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<String> getBody() {
		return body;
	}
	public void setBody(ArrayList<String> body) {
		this.body = body;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	
}
