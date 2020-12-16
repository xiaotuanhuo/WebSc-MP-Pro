package com.sc.mp.model.wxmessage;

public class TestMessageTextcard {
	private String title;
	private String description;
	private String url;
	private String btntxt;
	
	public TestMessageTextcard(){}
	
	public TestMessageTextcard(String sTitle, String sDescription, String sUrl, String sBtntxt){
		this.title = sTitle;
		this.description = sDescription;
		this.url = sUrl;
		this.btntxt = sBtntxt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBtntxt() {
		return btntxt;
	}

	public void setBtntxt(String btntxt) {
		this.btntxt = btntxt;
	}
}
