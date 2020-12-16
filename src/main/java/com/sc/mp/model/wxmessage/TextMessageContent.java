package com.sc.mp.model.wxmessage;

public class TextMessageContent {
	//消息内容，最长不超过2048个字节
	private String content;
	
	public TextMessageContent() {
	}

	public TextMessageContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
