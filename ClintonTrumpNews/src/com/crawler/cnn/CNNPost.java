package com.crawler.cnn;

public class CNNPost {

	private String title;
	private String body;
	private String authorName;
	private String date;
	
	
	public CNNPost(String title, String authorName,String date, String body){
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
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
