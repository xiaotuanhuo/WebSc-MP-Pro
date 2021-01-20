package com.sc.mp.model.wxmessage;

import javafx.scene.text.Text;

/**
 * example:
 * {
 *	   "touser" : "UserID1|UserID2|UserID3",
 *	   "toparty" : "PartyID1|PartyID2",
 *	   "totag" : "TagID1 | TagID2",
 *	   "msgtype" : "text",
 *	   "agentid" : 1,
 *	   "text" : {
 *	       "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。"
 *	   },
 *	   "safe":0
 *	}
 * @author wangrui
 *
 */
public class TextMessage {
	//成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送
	private String touser;
	//部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	private String toparty;
	//标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	private String totag;
	//消息类型，此时固定为：text
	private String msgtype;
	//企业应用的id，整型。可在应用的设置页面查看
	private int agentid;
	//文本消息主体
	private TestMessageTextcard textcard;
	
	public TextMessage() {
	}
	
	public TextMessage(String touser, String toparty, String totag,
			String msgtype, int agentid, TestMessageTextcard text) {
		this.touser = touser;
		this.toparty = toparty;
		this.totag = totag;
		this.msgtype = msgtype;
		this.agentid = agentid;
		this.textcard = text;
	}

	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getToparty() {
		return toparty;
	}
	public void setToparty(String toparty) {
		this.toparty = toparty;
	}
	public String getTotag() {
		return totag;
	}
	public void setTotag(String totag) {
		this.totag = totag;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public int getAgentid() {
		return agentid;
	}
	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}
	public TestMessageTextcard getTextcard() {
		return textcard;
	}
	public void setTextcard(TestMessageTextcard text) {
		this.textcard = text;
	}
	@Override
	public String toString() {
		return "{\"touser\":\"" + touser + "\", " + 
				"\"toparty\":\"" + toparty + "\", " + 
				"\"totag\":\"" + totag + "\", " + 
				"\"msgtype\":\"" + msgtype + "\", " + 
				"\"agentid\":" + agentid + ", " +
				"\"textcard\":{" + 
				"\"title\" : \"" + this.textcard.getTitle() + "\", " +
				"\"description\" : \"" + this.textcard.getDescription() + "\", " +
				"\"url\" : \"" + this.textcard.getUrl() + "\", " +
				"\"btntxt\":\"" + this.textcard.getBtntxt() + "\" " + 
	   			"}," +
	   			"\"enable_id_trans\": 0, " +
	   			"\"enable_duplicate_check\": 0, " +
	   			"\"duplicate_check_interval\": 1800 }";
	}
	
}
