package com.sc.mp.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sc.mp.model.WebScSendRecord;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.wxmessage.TestMessageTextcard;
import com.sc.mp.model.wxmessage.TextMessage;
import com.sc.mp.service.SendRecordService;
import com.sc.mp.service.UserService;
import com.sc.mp.util.WxUtil;

@Configuration
@Component
@EnableScheduling
public class ScheduleTask {
	private Logger log = LoggerFactory.getLogger(ScheduleTask.class);
	@Resource
	private SendRecordService sendRecordService;
	@Resource
	private UserService userService;
	@Resource
	private WxUtil wxUtil;
	
	public void schedule_sendmsg() {
//		log.info("schedule_sendmsg  开始定时执行");
//		//执行存储过程schedule_sendmsg
//		try {
//			List<WebScSendRecord> recordList = sendRecordService.getWebScSendRecord();
//			
//			for(WebScSendRecord wsr : recordList){
//				WebScUser senduser = userService.selectByPrimaryKey(wsr.getSendUserId());
//				
//				if(senduser.getWxOpenid() != null && !senduser.getWxOpenid().equals("")){
//					TestMessageTextcard content = new TestMessageTextcard("订单通知", wsr.getMemo(), "http://fw1.sucheng-group.com:9020//toDocDetail?id=" + wsr.getDocumentId() + "&state=0", "");
//					TextMessage tm = new TextMessage(senduser.getWxUserid(), "", "", "textcard", 1000016, content);
//					String ret = wxUtil.sendTextMessage(tm, null);
//					
//					JSONObject json = JSONObject.parseObject(ret);
//					Integer iErrCode = (Integer) json.get("errcode");
//					log.info("消息ID：" + wsr.getRecordId() + " 发送状态：" + iErrCode);
////					System.out.print("发给：" + us.getWxUserid() + "    iErrCode:" + iErrCode);
//					if(iErrCode == 0){
//						WebScSendRecord updateRd = new WebScSendRecord();
//						updateRd.setRecordId(wsr.getRecordId());
//						updateRd.setState("1");
//						sendRecordService.updateSendRecord(updateRd);
//					}else{
//						WebScSendRecord updateRd = new WebScSendRecord();
//						updateRd.setRecordId(wsr.getRecordId());
//						updateRd.setErrmsg(iErrCode + "");
//						updateRd.setState("9");
//						sendRecordService.updateSendRecord(updateRd);
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		log.info("schedule_sendmsg  执行结束");
    }
}