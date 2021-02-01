package com.sc.mp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.sc.mp.model.WebScAnesthetic;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.WebScUser_Distribution;
import com.sc.mp.service.DocService;
import com.sc.mp.service.QaTeamService;
import com.sc.mp.service.SendRecordService;
import com.sc.mp.service.UserService;
import com.sc.mp.util.Base64DecodeMultipartFile;
import com.sc.mp.util.ScConstant;
import com.sc.mp.util.WxUtil;

@Controller
public class DocController {
	private static final Logger log = LoggerFactory.getLogger(DocController.class);
			
	@Resource
	DocService docService;
	
	@Resource
	UserService userService;
	
	@Resource
	DataController data;
	
	@Resource
	QaTeamService qaTeamService;
	
	@Resource
	SendRecordService sendRecordService;
	
	@Resource
	private WxUtil wxUtil;
	
	@Value("${projectDomainName}")
	private String projectDomainName;
	
	@Value("${dirPath}")
	private String dirPath;
	
	/**
	* 	@Title: toDocList
	*  	@Description: 订单列表跳转
	*/
	@GetMapping(value="/toDocList")
	public String toDocList(HttpServletRequest request, 
	  		  				HttpServletResponse response,
	  		  				Model model,
	  		  				@RequestParam(value = "ds") String sState) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		
		model.addAttribute("state", sState);
		model.addAttribute("user", user);
		
		return "doc_list";
	}
	
	/**
	* 	@Title: DocListInit
	*  	@Description: 订单列表跳转
	*/ 
	@RequestMapping("/toDocListInit")
	@ResponseBody
    public JSONObject DocListInit(HttpServletRequest request, 
    					  		  HttpServletResponse response,
    					  		  @RequestParam(value = "ds") String sState){
		JSONObject returnMap = new JSONObject();
		
		//获取用户信息
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		
		//测试用
//		WebScUser searchUser = new WebScUser();
//		searchUser.setUserId(5);
//		WebScUser user = userService.selectUserInfo(searchUser);
		
		try{
			if(user != null){
				log.info("用户：" + user.getLoginName() + "省：" + user.getProvince() + " 市：" + user.getCity() + " 区：" + user.getArea());
				//用户角色
				String roleId = user.getRoleId();
				//查询条件
				WebScDoc docQuery = new WebScDoc();	
				docQuery.setRoleId(roleId);
				//更具不同角色,查询内容不同
				if(roleId.equals("1")){
					//系统管理员, 查询所有单据
				}else if(roleId.equals("2")){
					//医疗机构人员，查询本机构发布订单
					docQuery.setApplyUserId(String.valueOf(user.getUserId()));
					//省，市，区   查询范围
					docQuery.setProvince(user.getProvince());
					docQuery.setCity(user.getCity());
					docQuery.setArea(user.getArea());
				}else if(roleId.equals("3")){
					//卫监局人员
					//省，市，区   查询范围
					docQuery.setProvince(user.getProvince());
					docQuery.setCity(user.getCity());
					docQuery.setArea(user.getArea());
				}else if(roleId.equals("4")){
					//区域管理人员
					//省，市，区   查询范围
					docQuery.setProvince(user.getProvince());
					docQuery.setCity(user.getCity());
					docQuery.setArea(user.getArea());
					
					if(sState.equals("0")){
						//待处理
						docQuery.setDocumentState("'0','1','2','4','9'");
					}else if(sState.equals("1")){
						//已完成
						docQuery.setDocumentState("'5'");
					}else if(sState.equals("2")){
						//待取消
						docQuery.setDocumentState("'6'");
					}
				}else if(roleId.equals("5")){
					//医生，查询主治医生
					docQuery.setQaUserId(String.valueOf(user.getUserId()));
					//省，市，区   查询范围
					docQuery.setProvince(user.getProvince());
					docQuery.setCity(user.getCity());
					
					if(sState.equals("0")){
						//待处理
						docQuery.setDocumentState("'2','3'");
					}else if(sState.equals("1")){
						//已完成
						docQuery.setDocumentState("'5'");
					}else if(sState.equals("3")){
						//未评分订单
						docQuery.setDocumentState("'5'");
						docQuery.setDoctorEvaluate(-1);
					}
				}
				
				List<WebScDoc> docList = docService.selectWebScDocList(docQuery);
				for(WebScDoc d : docList){
		    		if(d.getStatus() != null && !d.getStatus().equals("")){
		    			if(d.getTmpPatientName() != null && !d.getTmpPatientName().equals(""))
		    				d.setPatientName(d.getTmpPatientName());
		    			if(d.getTmpPatientAge() != null && !d.getTmpPatientAge().equals(""))
		    				d.setPatientAge(d.getTmpPatientAge());
		    			if(d.getTmpPatientSex() != null && !d.getTmpPatientSex().equals(""))
		    				d.setPatientSex(d.getTmpPatientSex());
		    			if(d.getTmpOperativeId() != null && !d.getTmpOperativeId().equals(""))
		    				d.setOperativeId(d.getTmpOperativeId());
		    			if(d.getTmpOperativeName() != null && !d.getTmpOperativeName().equals(""))
		    				d.setOperativeName(d.getTmpOperativeName());
		    			if(d.getTmpAnestheticId() != null && !d.getTmpAnestheticId().equals(""))
		    				d.setAnestheticId(d.getTmpAnestheticId());
		    			if(d.getTmpAnestheticName() != null && !d.getTmpAnestheticName().equals(""))
		    				d.setAnestheticName(d.getTmpAnestheticName());
		    			
		    			//呼吸系统
		    			if(d.getYwsjHxxt() != null){
		    				String[] hxxts = d.getYwsjHxxt().split(",");
		    				d.setYwsjHxxtVal("");
		    				for(int i = 0; i < hxxts.length; i++){
		    					if(hxxts[i].equals("0")){
		    						d.setYwsjHxxtVal(d.getYwsjHxxtVal() + "频繁舌后坠（≥3次）  ");
		    					}else if(hxxts[i].equals("1")){
		    						d.setYwsjHxxtVal(d.getYwsjHxxtVal() + "喉痉挛  ");
		    					}else if(hxxts[i].equals("2")){
		    						d.setYwsjHxxtVal(d.getYwsjHxxtVal() + "返流  ");
		    					}else if(hxxts[i].equals("3")){
		    						d.setYwsjHxxtVal(d.getYwsjHxxtVal() + "误吸  ");
		    					}else if(hxxts[i].equals("4")){
		    						d.setYwsjHxxtVal(d.getYwsjHxxtVal() + "支气管痉挛  ");
		    					}else if(hxxts[i].equals("5")){
		    						d.setYwsjHxxtVal(d.getYwsjHxxtVal() + "计划外插管  ");
		    					}
		    				}
		    			}
		    			
		    			//循环系统
		    			if(d.getYwsjXhxt() != null){
		    				String[] xhxts = d.getYwsjXhxt().split(",");
		    				d.setYwsjXhxtVal("");
		    				for(int i = 0; i < xhxts.length; i++){
		    					if(xhxts[i].equals("0")){
		    						d.setYwsjXhxtVal(d.getYwsjXhxtVal() + "需纠正的低血压  ");
		    					}else if(xhxts[i].equals("1")){
		    						d.setYwsjXhxtVal(d.getYwsjXhxtVal() + "需纠正的高血压  ");
		    					}else if(xhxts[i].equals("2")){
		    						d.setYwsjXhxtVal(d.getYwsjXhxtVal() + "肺动脉栓塞  ");
		    					}else if(xhxts[i].equals("3")){
		    						d.setYwsjXhxtVal(d.getYwsjXhxtVal() + "心梗  ");
		    					}else if(xhxts[i].equals("4")){
		    						d.setYwsjXhxtVal(d.getYwsjXhxtVal() + "脑梗  ");
		    					}
		    				}
		    			}
		    			
		    			String photo = "";
		    			if(d.getPhoto_1() != null && !d.getPhoto_1().trim().equals("")){
		    				photo = photo + d.getPhoto_1() + ",";
		    			}
		    			if(d.getPhoto_2() != null && !d.getPhoto_2().trim().equals("")){
		    				photo = photo + d.getPhoto_2() + ",";
		    			}
		    			if(d.getPhoto_3() != null && !d.getPhoto_3().trim().equals("")){
		    				photo = photo + d.getPhoto_3() + ",";
		    			}
		    			if(!photo.equals(""))	photo = photo.substring(0, photo.length() - 1);
		    			d.setPhoto(photo);
		    		}
		    	}
				
				returnMap.put("code", 1);
				returnMap.put("msg", "");
				returnMap.put("list", docList);
				returnMap.put("role", user.getRoleId());
			}else{
				returnMap.put("code", 0);
				returnMap.put("msg", "订单数据获取错误，请重试！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("code", 0);
			returnMap.put("msg", "系统错误，请重试！");
		}
		log.info("用户：" + user.getUserName() + " 查询订单清单：" + returnMap.toJSONString());
		
		return JSONObject.parseObject(returnMap.toJSONString());
	}
	
	/**
	* 	@Title: toDocDetail
	*  	@Description: 订单列表跳转
	*/ 
	@RequestMapping("/toDocDetail")	
    public String toDocDetail(HttpServletRequest request, 
    					 	  HttpServletResponse response, 
    					  	  Model model,
    					  	  @RequestParam(value = "id") String sDocId,
    					  	  @RequestParam(value = "state") String sState) {
		try{
			//获取用户信息
			HttpSession session = request.getSession();
			WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
			
			//测试用
//			WebScUser searchUser = new WebScUser();
//			searchUser.setUserId(4);
//			WebScUser user = userService.selectUserInfo(searchUser);
			
			WebScDoc searchDoc = new WebScDoc();
			searchDoc.setDocumentId(sDocId);
			WebScDoc doc = docService.selectWebScDoc(searchDoc);
			
			if(doc.getStatus() != null && !doc.getStatus().equals("")){
				if(doc.getTmpPatientName() != null && !doc.getTmpPatientName().equals(""))
    				doc.setPatientName(doc.getTmpPatientName());
    			if(doc.getTmpPatientAge() != null && !doc.getTmpPatientAge().equals(""))
    				doc.setPatientAge(doc.getTmpPatientAge());
    			if(doc.getTmpPatientSex() != null && !doc.getTmpPatientSex().equals(""))
    				doc.setPatientSex(doc.getTmpPatientSex());
    			if(doc.getTmpOperativeId() != null && !doc.getTmpOperativeId().equals(""))
    				doc.setOperativeId(doc.getTmpOperativeId());
    			if(doc.getTmpOperativeName() != null && !doc.getTmpOperativeName().equals(""))
    				doc.setOperativeName(doc.getTmpOperativeName());
    			if(doc.getTmpAnestheticId() != null && !doc.getTmpAnestheticId().equals(""))
    				doc.setAnestheticId(doc.getTmpAnestheticId());
    			if(doc.getTmpAnestheticName() != null && !doc.getTmpAnestheticName().equals(""))
    				doc.setAnestheticName(doc.getTmpAnestheticName());

    			//呼吸系统
    			if(doc.getYwsjHxxt() != null){
    				String[] hxxts = doc.getYwsjHxxt().split(",");
    				doc.setYwsjHxxtVal("");
    				for(int i = 0; i < hxxts.length; i++){
    					if(hxxts[i].equals("0")){
    						doc.setYwsjHxxtVal(doc.getYwsjHxxtVal() + "频繁舌后坠（≥3次）  ");
    					}else if(hxxts[i].equals("1")){
    						doc.setYwsjHxxtVal(doc.getYwsjHxxtVal() + "喉痉挛  ");
    					}else if(hxxts[i].equals("2")){
    						doc.setYwsjHxxtVal(doc.getYwsjHxxtVal() + "返流  ");
    					}else if(hxxts[i].equals("3")){
    						doc.setYwsjHxxtVal(doc.getYwsjHxxtVal() + "误吸  ");
    					}else if(hxxts[i].equals("4")){
    						doc.setYwsjHxxtVal(doc.getYwsjHxxtVal() + "支气管痉挛  ");
    					}else if(hxxts[i].equals("5")){
    						doc.setYwsjHxxtVal(doc.getYwsjHxxtVal() + "计划外插管  ");
    					}
    				}
    			}
    			
    			//循环系统
    			if(doc.getYwsjXhxt() != null){
    				String[] xhxts = doc.getYwsjXhxt().split(",");
    				doc.setYwsjXhxtVal("");
    				for(int i = 0; i < xhxts.length; i++){
    					if(xhxts[i].equals("0")){
    						doc.setYwsjXhxtVal(doc.getYwsjXhxtVal() + "需纠正的低血压  ");
    					}else if(xhxts[i].equals("1")){
    						doc.setYwsjXhxtVal(doc.getYwsjXhxtVal() + "需纠正的高血压  ");
    					}else if(xhxts[i].equals("2")){
    						doc.setYwsjXhxtVal(doc.getYwsjXhxtVal() + "肺动脉栓塞  ");
    					}else if(xhxts[i].equals("3")){
    						doc.setYwsjXhxtVal(doc.getYwsjXhxtVal() + "心梗  ");
    					}else if(xhxts[i].equals("4")){
    						doc.setYwsjXhxtVal(doc.getYwsjXhxtVal() + "脑梗  ");
    					}
    				}
    			}
    			
    			String photo = "";
    			if(doc.getPhoto_1() != null && !doc.getPhoto_1().trim().equals("")){
    				photo = photo + doc.getPhoto_1() + ",";
    			}
    			if(doc.getPhoto_2() != null && !doc.getPhoto_2().trim().equals("")){
    				photo = photo + doc.getPhoto_2() + ",";
    			}
    			if(doc.getPhoto_3() != null && !doc.getPhoto_3().trim().equals("")){
    				photo = photo + doc.getPhoto_3() + ",";
    			}
    			if(!photo.equals(""))	photo = photo.substring(0, photo.length() - 1);
    			doc.setPhoto(photo);
    		}
			
			model.addAttribute("doc", doc);
			model.addAttribute("user", user);
			
			//微信网页jsdk，初始化参数生成
			String nonceStr = wxUtil.getNonceStr();
			String timestamp = wxUtil.getTimestamp();
			String appId = WxUtil.sCorpid;
			String signature = wxUtil.getSignature("http://" + projectDomainName + "/toDocDetail?id=" + doc.getDocumentId() + "&state=" + sState, nonceStr, timestamp, request.getSession());
			
			System.out.println("签名：" + signature);
			
			model.addAttribute("ns", nonceStr);
			model.addAttribute("ts",timestamp);
			model.addAttribute("aid", appId);
			model.addAttribute("sg",signature);
			
			model.addAttribute("state",sState);
			
			//数据缓存
			//获取麻醉方法
			List<WebScAnesthetic> anestheticls = data.getWebScAnestheticList();
			model.addAttribute("anestheticls", anestheticls);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "doc_detail";
	}

	/**
	 * @Description: 订单更新
	 * @Title: updateOrderInfo
	 * @return 
	*/
	@RequestMapping("/updateDocInfo")
	@ResponseBody
	public JSONObject updateDocInfo(HttpServletRequest request, 
							  		HttpServletResponse response){
		JSONObject returnMap = new JSONObject();
		
		try{
			//获取用户信息
			HttpSession session = request.getSession();
			WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
			
			//测试用
//			WebScUser searchUser = new WebScUser();
//			searchUser.setUserId(4);
//			WebScUser user = userService.selectUserInfo(searchUser);
			
			//参数
			String sFormJson = request.getParameter("jsondata");
			JSONObject jo = JSONObject.parseObject(sFormJson);
			Map<String, Object> updateMap = new HashMap<String, Object>();
			
			String sType = jo.get("type").toString();
			//更新状态位
			String id = jo.get("id").toString();
			updateMap.put("documentId", id);
			
			if(sType.equals("s")){
				String ds = jo.get("ds").toString();
				updateMap.put("documentState", ds);
				String ods = jo.get("ods").toString();
				updateMap.put("oldDocumentState", ods);
				
				if(ods.equals("0") && ds.equals("1")){
					//审核订单		区域管理员操作
					updateMap.put("adminUserId", user.getUserId());
					log.info("区域管理员：" + user.getUserName() + "	审核订单：" + id + " 操作开始！");
				}else if(ods.equals("1") && ds.equals("2")){
					//订单分配		区域管理员操作
					String qid = jo.get("qid").toString();
					updateMap.put("qaUserId", qid);
					log.info("区域管理员：" + user.getUserName() + "	分配订单：" + id + "	医生:" + qid + " 操作开始！");
					sendRecordService.insertSendRecord(id, user, 3001, qid);
				}else if(ods.equals("2") && ds.equals("1")){
					//取消分配		区域管理员操作
					updateMap.put("qaUserId", "");
					log.info("区域管理员：" + user.getUserName() + "	取消分配订单：" + id + " 操作开始！");
					sendRecordService.insertSendRecord(id, user, 3002, "0");
				}else if(ods.equals("2") && ds.equals("3")){
					//确认接单		医生操作
					log.info("医生：" + user.getUserName() + "	确认接取订单：" + id + " 操作开始！");
					sendRecordService.insertSendRecord(id, user, 4001, "0");
				}else if(ods.equals("3") && ds.equals("2")){
					//撤销分配单	医生操作
					log.info("医生：" + user.getUserName() + "	撤销分配订单：" + id + " 操作开始！");
					sendRecordService.insertSendRecord(id, user, 4002, "0");
				}else if(ods.equals("3") && ds.equals("3")){
					//撤销转单		医生操作
					String qid = jo.get("qid").toString();
					updateMap.put("qaUserId", qid);
					updateMap.put("transferUserId", "");
					log.info("医生：" + user.getUserName() + "	撤销转单：" + id + " 操作开始！");
					sendRecordService.insertSendRecord(id, user, 4003, "0");
				}else if(ods.equals("4") && ds.equals("5")){
					//完成订单		管理员操作
					log.info("区域管理员：" + user.getUserName() + "	完成订单：" + id + " 操作开始！");
				}else if(ds.equals("9")){
					//取消订单		管理员操作
					String reason = jo.get("reason").toString();
					String qxRadio = jo.get("qxRadio").toString();
					updateMap.put("memo", reason);
					updateMap.put("qxRadio", qxRadio);
					log.info("区域管理员：" + user.getUserName() + "	取消订单：" + id + " 操作开始！");
					sendRecordService.insertSendRecord(id, user, 9001, "0");
				}else if(!ds.equals("6") && ods.equals("9")){
					//拒绝取消订单		管理员操作
					log.info("区域管理员：" + user.getUserName() + "	拒绝取消订单：" + id + " 操作开始！");
				}else if(ds.equals("6") && ods.equals("9")){
					//同意取消订单		管理员操作
					log.info("区域管理员：" + user.getUserName() + "	同意取消订单：" + id + " 操作开始！");
				}
				
				int iRet = docService.updateDocInfoByMap(updateMap);
				
				if(iRet > 0){
					returnMap.put("code", 1);
					
					if(ods.equals("0") && ds.equals("1")){
						//审核成功		区域管理员操作
						log.info("区域管理员：" + user.getUserName() + "	审核订单：" + id + " 成功！");
						//数据库日志记录		暂定
					}else if(ods.equals("1") && ds.equals("2")){
						//分配成功		区域管理员操作
						log.info("区域管理员：" + user.getUserName() + "	分配订单：" + id + " 成功！");
					}else if(ods.equals("2") && ds.equals("1")){
						//取消分配		区域管理员操作
						log.info("区域管理员：" + user.getUserName() + "	取消分配订单：" + id + " 成功！");
					}else if(ods.equals("2") && ds.equals("3")){
						//确认接单		医生操作
						log.info("医生：" + user.getUserName() + "	确认接取订单：" + id + "  成功！");
					}else if(ods.equals("3") && ds.equals("2")){
						//撤销分配单	医生操作
						log.info("医生：" + user.getUserName() + "	撤销分配订单：" + id + "  成功！");
					}else if(ods.equals("3") && ds.equals("3")){
						//撤销转单		医生操作
						log.info("医生：" + user.getUserName() + "	撤销转单：" + id + "  成功！");
					}else if(ods.equals("4") && ds.equals("5")){
						//完成订单		管理员操作
						log.info("区域管理员：" + user.getUserName() + "	完成订单：" + id + " 成功！");
					}else if(ds.equals("9")){
						//取消订单		管理员操作
						log.info("区域管理员：" + user.getUserName() + "	取消订单：" + id + " 成功！");
					}else if(!ds.equals("6") && ods.equals("9")){
						//拒绝取消订单		管理员操作
						log.info("区域管理员：" + user.getUserName() + "	拒绝取消订单：" + id + " 成功！");
					}else if(ds.equals("6") && ods.equals("9")){
						//同意取消订单		管理员操作
						log.info("区域管理员：" + user.getUserName() + "	同意取消订单：" + id + " 操作开始！");
					}
				}else{
					returnMap.put("code", 0);
					log.info("人员：" + user.getUserName() + "	操作订单：" + id + " 系统错误！");
				}
			}else if(sType.equals("zd")){
				String ds = jo.get("ds").toString();
				updateMap.put("documentState", ds);
				String ods = jo.get("ods").toString();
				updateMap.put("oldDocumentState", ods);
				
				String qid = jo.get("qid").toString();
				updateMap.put("qaUserId", qid);
				updateMap.put("transferUserId", user.getUserId());
				log.info("医生：" + user.getUserName() + "  转单：" + id + " 给医生：" + qid + "	操作开始！");
				
				int iRet = docService.updateDocInfoByMap(updateMap);
				
				sendRecordService.insertSendRecord(id, user, 4004, qid);
				
				if(iRet > 0){
					returnMap.put("code", 1);
					log.info("医生：" + user.getUserName() + "	转单：" + id + " 成功！");
				}else{
					returnMap.put("code", 0);
					log.info("区域管理员：" + user.getUserName() + "	操作订单：" + id + " 系统错误！");
				}
			}else if(sType.equals("t")){
				String ds = jo.get("ds").toString();
				updateMap.put("documentState", ds);
				String ods = jo.get("ods").toString();
				updateMap.put("oldDocumentState", ods);
				
				Map<String, Object> tmpUpdateMap = jo.getInnerMap();
				tmpUpdateMap.put("documentId", id);
				
				//医生备注
				if(jo.get("doctorMemo") != null){
					String doctorMemo = jo.get("doctorMemo").toString();
					updateMap.put("qaMemo", doctorMemo);
				}
				
				log.info("id:" + id + " sskssj:" + tmpUpdateMap.get("sskssj") + " ssjssj:" + tmpUpdateMap.get("ssjssj") + " sssc:" + tmpUpdateMap.get("sssc"));
				if(tmpUpdateMap.get("sssc") == null || tmpUpdateMap.get("sssc").toString().equals("NaN")){
					String sskssj = tmpUpdateMap.get("sskssj").toString();
					String ssjssj = tmpUpdateMap.get("ssjssj").toString();
					long min = getDistanceMin(sskssj, ssjssj);
					tmpUpdateMap.put("sssc", min);
				}
				if(tmpUpdateMap.get("sssc").toString().equals("")){
					tmpUpdateMap.put("sssc", 0);
				}
				
				tmpUpdateMap.put("photo_1", "");
				tmpUpdateMap.put("photo_2", "");
				tmpUpdateMap.put("photo_3", "");
				if(jo.get("photo") != null){
					String photo = jo.get("photo").toString();
					System.out.println(photo);
					if(photo != null && !photo.equals("")){
						String[] photols = photo.split(",");
						for(int i = 0; i< photols.length; i++){
							System.out.println(photols[i]);
							if(i == 0){
								tmpUpdateMap.put("photo_1", photols[0]);
							}else if(i == 1){
								tmpUpdateMap.put("photo_2", photols[1]);
							}else if(i == 2){
								tmpUpdateMap.put("photo_3", photols[2]);
							}
						}
					}
				}
				
				if(ods.equals("3") && ds.equals("4")){
					//完成订单
					tmpUpdateMap.put("status", "1");
					log.info("医生：" + user.getUserName() + "	完成订单：" + id + " 开始！");
					sendRecordService.insertSendRecord(id, user, 4005, "0");
				}else if(ods.equals("3") && ds.equals("3")){
					//保存草稿
					tmpUpdateMap.put("status", "0");
					updateMap.put("oldDocumentState", "3");
					
					log.info("医生：" + user.getUserName() + "	保存草稿：" + id + " 开始！");
				}else if(ods.equals("5") && ds.equals("4")){
					//退回待完成订单		管理员操作
					log.info("区域管理员：" + user.getUserName() + "	退回待完成订单：" + id + " 操作开始！");
					String qid = jo.get("adminMemo").toString();
					updateMap.put("adminMemo", qid);
					
					tmpUpdateMap.put("status", "0");
				}
				
				int iRet = docService.updateTmpDoc(updateMap, tmpUpdateMap);
				
				if(iRet > 0){
					returnMap.put("code", 1);
					if(ods.equals("3") && ds.equals("4")){
						//完成订单
						log.info("医生：" + user.getUserName() + "	完成订单：" + id + " 成功！");
					}else if(ods.equals("3") && ds.equals("3")){
						//保存草稿
						log.info("医生：" + user.getUserName() + "	保存草稿：" + id + " 成功！");
					}else if(ods.equals("5") && ds.equals("4")){
						//退回待完成订单		管理员操作
						log.info("区域管理员：" + user.getUserName() + "	退回待完成订单：" + id + " 成功！");
					}
				}else{
					returnMap.put("code", 0);
					log.info("医生：" + user.getUserName() + "	操作订单：" + id + " 系统错误！");
				}
			}else if(sType.equals("df")){
				String doctorEvaluate = jo.get("de").toString();
				String doctorEvaluateMemo = jo.get("dem").toString();
				updateMap.put("doctorEvaluate", doctorEvaluate);
				updateMap.put("doctorEvaluateMemo", doctorEvaluateMemo);
				log.info("医生：" + user.getUserName() + "  评价订单：" + id + " 操作开始！");
				
				int iRet = docService.updateDocInfoByMap(updateMap);
				
				if(iRet > 0){
					returnMap.put("code", 1);
					log.info("医生：" + user.getUserName() + "	转单：" + id + " 成功！");
				}else{
					returnMap.put("code", 0);
					log.info("区域管理员：" + user.getUserName() + "	操作订单：" + id + " 系统错误！");
				}
			}else if(sType.equals("twc")){
				String ds = jo.get("ds").toString();
				updateMap.put("documentState", ds);
				String ods = jo.get("ods").toString();
				updateMap.put("oldDocumentState", ods);
				
				Map<String, Object> tmpUpdateMap = new HashMap<String, Object>();
				tmpUpdateMap.put("documentId", id);
				tmpUpdateMap.put("status", "0");
				
				//管理员备注
				String adminMemo = jo.get("adminMemo").toString();
				updateMap.put("adminMemo", adminMemo);
				
				//退回待完成订单		管理员操作
				log.info("区域管理员：" + user.getUserName() + "	退回待完成订单：" + id + " 操作开始！");
				
				int iRet = docService.updateTmpDoc2(updateMap, tmpUpdateMap);
				
				if(iRet > 0){
					returnMap.put("code", 1);
					//退回待完成订单		管理员操作
					log.info("区域管理员：" + user.getUserName() + "	退回待完成订单：" + id + " 成功！");
				}else{
					returnMap.put("code", 0);
					log.info("区域管理员：" + user.getUserName() + "	退回待完成订单：" + id + " 系统错误！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
	}

	/**
	 * @Description: 获取医生分配列表
	 * @Title: getDistributionDrGridList
	 * @return 
	*/
	@RequestMapping("/getDistributionDrGridList")
	@ResponseBody
	public JSONObject getDistributionDrGridList(HttpServletRequest request, 
	  											HttpServletResponse response,
	  											@RequestParam(value = "id") String sDocumentId,
	  											@RequestParam(value = "searchVal") String searchVal){
		JSONObject returnMap = new JSONObject();
		try{
			//获取用户信息
			HttpSession session = request.getSession();
			WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
			
			//测试用
//			WebScUser searchUser = new WebScUser();
//			searchUser.setUserId(5);
//			WebScUser user = userService.selectUserInfo(searchUser);
			
			//获取医生名单
			List<WebScUser_Distribution> drList = docService.getDistributionDrGridList(sDocumentId, searchVal, user);
		
			returnMap.put("code", 1);
			returnMap.put("list", drList);
			
		}catch(Exception e){
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
	}
	
	/**
	 * 
	 * @Title: transferuser   
	 * @Description: 获取转单医生列表
	 * @param: @param documentId
	 * @param: @param userName
	 * @param: @return      
	 * @return: JSONObject      
	 * @throws
	 */
	@RequestMapping("/transferuser")
    @ResponseBody
    public JSONObject transferuser(HttpServletRequest request, 
								   HttpServletResponse response,
								   @RequestParam(value = "id") String documentId,
    							   @RequestParam(value = "searchVal") String searchVal) {
		JSONObject returnMap = new JSONObject();
		
		try{
			//获取用户信息
			HttpSession session = request.getSession();
			WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
			
			//测试用
//			WebScUser searchUser = new WebScUser();
//			searchUser.setUserId(4);
//			WebScUser user = userService.selectUserInfo(searchUser);
			
	    	//获取订单数据
			WebScDoc searchDoc = new WebScDoc();
			searchDoc.setDocumentId(documentId);
	    	List<WebScDoc> docs = docService.selectWebScDocList(searchDoc);
	    	
			if(docs != null){
				//查询订单当日医院情况
				WebScDoc doc = docs.get(0);
				if(doc.getStatus() != null && !doc.getStatus().equals("")){
					if(doc.getTmpPatientName() != null && !doc.getTmpPatientName().equals(""))
	    				doc.setPatientName(doc.getTmpPatientName());
	    			if(doc.getTmpPatientAge() != null && !doc.getTmpPatientAge().equals(""))
	    				doc.setPatientAge(doc.getTmpPatientAge());
	    			if(doc.getTmpPatientSex() != null && !doc.getTmpPatientSex().equals(""))
	    				doc.setPatientSex(doc.getTmpPatientSex());
	    			if(doc.getTmpOperativeId() != null && !doc.getTmpOperativeId().equals(""))
	    				doc.setOperativeId(doc.getTmpOperativeId());
	    			if(doc.getTmpOperativeName() != null && !doc.getTmpOperativeName().equals(""))
	    				doc.setOperativeName(doc.getTmpOperativeName());
	    			if(doc.getTmpAnestheticId() != null && !doc.getTmpAnestheticId().equals(""))
	    				doc.setAnestheticId(doc.getTmpAnestheticId());
	    			if(doc.getTmpAnestheticName() != null && !doc.getTmpAnestheticName().equals(""))
	    				doc.setAnestheticName(doc.getTmpAnestheticName());
//	    			doc.setPatientName(doc.getTmpPatientName());
//	    			doc.setPatientAge(doc.getTmpPatientAge());
//	    			doc.setPatientSex(doc.getTmpPatientSex());
//	    			doc.setOperativeId(doc.getTmpOperativeId());
//	    			doc.setOperativeName(doc.getTmpOperativeName());
//	    			doc.setAnestheticId(doc.getTmpAnestheticId());
//	    			doc.setAnestheticName(doc.getTmpAnestheticName());

	    			String photo = "";
	    			if(doc.getPhoto_1() != null && !doc.getPhoto_1().trim().equals("")){
	    				photo = photo + doc.getPhoto_1() + ",";
	    			}
	    			if(doc.getPhoto_2() != null && !doc.getPhoto_2().trim().equals("")){
	    				photo = photo + doc.getPhoto_2() + ",";
	    			}
	    			if(doc.getPhoto_3() != null && !doc.getPhoto_3().trim().equals("")){
	    				photo = photo + doc.getPhoto_3() + ",";
	    			}
	    			if(!photo.equals(""))	photo = photo.substring(0, photo.length() - 1);
	    			doc.setPhoto(photo);
	    		}
				
				//获取可转单医生名单
				Map<String, String> searchMap = new HashMap<String, String>();
				searchMap.put("operateStartTime", doc.getOperateStartTime());
				searchMap.put("orgId", doc.getOrgId());
				searchMap.put("userName", searchVal);
				searchMap.put("NoUserId", user.getUserId() + "");
				
				List<WebScUser> userls = docService.getTransferUser(searchMap);
				
				if(userls != null && userls.size() > 0){
					returnMap.put("code", 1);
					returnMap.put("list", userls);
				}else{
					returnMap.put("code", 0);
				}
			}else{
				returnMap.put("code", 0);
			}
		}catch(Exception e){
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
	}
	
	@RequestMapping("/showTeam")
	@ResponseBody
    public JSONObject showTeam(@RequestParam(value = "id") String documentId) {
		JSONObject returnMap = new JSONObject();
		
		List<WebScUser> userls = qaTeamService.getQaTeamInfo(documentId);
		
		if(userls == null){
			userls = new ArrayList<WebScUser>();
		}

		returnMap.put("list", userls);
		
		return JSONObject.parseObject(returnMap.toJSONString());
	}
	
	//获取团队人员列表
	@RequestMapping("/getQaUserInfo")
    @ResponseBody
    public JSONObject getQaUserInfo(HttpServletRequest request, 
			   						HttpServletResponse response,
			   						@RequestParam(value = "id") String documentId,
    								@RequestParam(value = "type") String roleId,
    								@RequestParam(value = "searchVal") String userName) {
		JSONObject returnMap = new JSONObject();
		List<WebScUser> userls = new ArrayList<WebScUser>();

		try{
			//获取用户信息
			HttpSession session = request.getSession();
			WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
			
			//测试用
//			WebScUser searchUser = new WebScUser();
//			searchUser.setUserId(4);
//			WebScUser user = userService.selectUserInfo(searchUser);
			
	    	//获取订单数据
			WebScDoc searchDoc = new WebScDoc();
			searchDoc.setDocumentId(documentId);
			WebScDoc doc = docService.selectWebScDoc(searchDoc);
			
			if(doc != null){
				//查询订单当日医院情况
				Map<String, String> searchMap = new HashMap<String, String>();
				searchMap.put("userName", userName);
				searchMap.put("roleId", roleId);
				searchMap.put("province", doc.getOrgProvince());
				searchMap.put("NoUserId", user.getUserId() + "");
				
				userls = docService.getQaUserInfo(searchMap);
				
				if(userls != null && userls.size() > 0){
					if(doc.getQaTeamId() != null){
						List<WebScUser> ls = qaTeamService.getQaTeamInfo(doc.getQaTeamId());
						for(WebScUser u : ls){
							for(WebScUser tu : userls){
								if(tu.getUserId() == u.getUserId()){
									userls.remove(tu);
									break;
								}
							}
						}
					}
				}
				
				returnMap.put("code", 1);
				returnMap.put("list", userls);
			}else{
				returnMap.put("code", 0);
				returnMap.put("errmsg", "订单不存在");
			}
		}catch(Exception e){
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
	}
	
	@RequestMapping("/addQaTeamUser")
    @ResponseBody
    public JSONObject addQaTeamUser(@RequestParam(value = "id") String documentId,
    						  		@RequestParam(value = "uid") String userId,
    						  		@RequestParam(value = "type") String roleId) {
		JSONObject returnMap = new JSONObject();
		
		try{
			
			Map<String, String> insertMap = new HashMap<String, String>();
			insertMap.put("documentId", documentId);
			insertMap.put("userId", userId);
			insertMap.put("roleId", roleId);
			
			qaTeamService.insertQaUser(insertMap);
			
			//查询用户信息
			List<WebScUser> userls = qaTeamService.getQaTeamInfo(documentId);
			for(WebScUser u : userls){
				if(u.getUserId().toString().trim().equals(userId)){
					returnMap.put("user", u);
					System.out.println(u);
					break;
				}
			}
			
			returnMap.put("code", 1);
		}catch(Exception e){
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
    }
	
	@RequestMapping("/delQaTeamUser")
    @ResponseBody
    public JSONObject delQaTeamUser(@RequestParam(value = "id") String documentId,
    						  		@RequestParam(value = "uid") String userId) {
		JSONObject returnMap = new JSONObject();
		
		try{
			Map<String, String> deleteMap = new HashMap<String, String>();
			deleteMap.put("documentId", documentId);
			deleteMap.put("userId", userId);
			
			qaTeamService.deleteQaUser(deleteMap);
			
			returnMap.put("code", 1);
		}catch(Exception e){
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
    }
	
	@RequestMapping(value = "/uploadImg",method = RequestMethod.POST)
	@ResponseBody
    public JSONObject uploadImg(HttpServletRequest request, 
								HttpServletResponse response,
								@RequestParam("id") String documentId,
								@RequestParam("myPhoto") String base64Data) {
		JSONObject returnMap = new JSONObject();
		
		try{
			log.info("======开始上传图片======");
			
			//当前年月
        	Date d = new Date();  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
            String dateNowStr = sdf.format(d);  
            log.info("文件夹：" + dirPath  + File.separator + dateNowStr);              
            
            File path = new File(dirPath  + File.separator + dateNowStr);
            if (!path.exists()) {
            	path.mkdir();
			}
            
			MultipartFile file = Base64DecodeMultipartFile.base64Convert(base64Data);
			
			if (Objects.nonNull(file)) {
				String originalFilename = file.getOriginalFilename();
				String filePath = File.separator + dateNowStr + File.separator + documentId + "_" + originalFilename;
				
	        	File dir = new File(dirPath + filePath);
	        	System.out.println("路径:" + dirPath + filePath);
	        	if (dir.exists()) {
	        		returnMap.put("code", 0);;
	        		returnMap.put("msg", "同名文件已存在！");
	        	}else{
	    			file.transferTo(new File(new File(dirPath).getAbsoluteFile() + filePath));
	    			
	    			returnMap.put("fileName", filePath);
	    			returnMap.put("code", 1);
	        	}
	        	log.info("======图片上传成功,路径======" + filePath);
			}else {
				returnMap.put("code", 0);
	            log.error("图片上传失败");
	        }
		}catch(Exception e){
			returnMap.put("code", 0);
		}
		
		return JSONObject.parseObject(returnMap.toJSONString());
    }
	
	@RequestMapping("/getPhotoByFileName")
	public void getPhotoByFileName (@RequestParam(value = "id") String documentId, 
									@RequestParam(value = "FileName") String FileName, 
									final HttpServletResponse response) throws IOException{
		try { 
			File file = new File(dirPath + FileName);
			if(file != null){
				FileInputStream fis = new FileInputStream (file);
	            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
	            byte[] b = new byte[1000];  
	            int n;  
	            while ((n = fis.read(b)) != -1) {  
	                bos.write(b, 0, n);  
	            }  
	            fis.close();  
	            bos.close();
	            
	            byte[] data = bos.toByteArray();  
	            
	            response.setContentType("image/jpeg");  
	    	    response.setCharacterEncoding("UTF-8");  
	    	    OutputStream outputSream = response.getOutputStream();  
	    	    InputStream in = new ByteArrayInputStream(data);  
	    	    int len = 0;  
	    	    byte[] buf = new byte[1024];  
	    	    while ((len = in.read(buf, 0, 1024)) != -1) {  
	    	        outputSream.write(buf, 0, len);  
	    	    }  
	    	    outputSream.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/deletePhotoByFileName")
	@ResponseBody
	public void deletePhotoByFileName (@RequestParam(value = "id") String documentId, 
									   @RequestParam(value = "FileName") String FileName){
		try {
			File file = new File(dirPath + FileName);
			log.info("删除文件：" + dirPath + FileName);
			file.delete();
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@RequestMapping("/updateDocImage")
	@ResponseBody
	public JSONObject updateDocImage (HttpServletRequest request, 
	  							HttpServletResponse response,
	  							@RequestParam(value = "id") String documentId,
	  							@RequestParam(value = "serverId") String serverId){
		JSONObject returnMap = new JSONObject();
		try {
			HttpSession session = request.getSession();
			
			String sFileName = wxUtil.downloadMedia(dirPath, documentId, serverId, session);
			
			returnMap.put("code", 1);
			returnMap.put("fileName", sFileName);
		}catch (Exception e) {
			returnMap.put("code", 0);
			e.printStackTrace();
		}
		
		return returnMap;
	}

	public static long getDistanceMin(String str1, String str2) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long days=0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }
}
