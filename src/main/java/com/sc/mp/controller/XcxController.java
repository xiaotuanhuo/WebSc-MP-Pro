package com.sc.mp.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sc.mp.annotation.OperationLog;
import com.sc.mp.bean.KeyValue;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.bean.PageResultBean;
import com.sc.mp.bean.ResultBean;
import com.sc.mp.bean.enums.DocStateEnum;
import com.sc.mp.bean.enums.RoleEnum;
import com.sc.mp.mapper.AnestheticMapper;
import com.sc.mp.mapper.DocMapper;
import com.sc.mp.mapper.OperativeMapper;
import com.sc.mp.mapper.UserMapper;
import com.sc.mp.mapper.WebOrgOperativesMapper;
import com.sc.mp.mapper.WebScDocOperativeMapper;
import com.sc.mp.mapper.WebScEvaluateMapper;
import com.sc.mp.mapper.WebScGzhMediaMapper;
import com.sc.mp.mapper.WebScLabelMapper;
import com.sc.mp.model.WebOrgOperatives;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScDocOperative;
import com.sc.mp.model.WebScEvaluate;
import com.sc.mp.model.WebScGzhMedia;
import com.sc.mp.model.WebScLabel;
import com.sc.mp.model.WebScOperative;
import com.sc.mp.model.WebScUser;
import com.sc.mp.service.SendRecordService;
import com.sc.mp.util.CalendarUtil;
import com.sc.mp.util.DateUtils;
import com.sc.mp.util.HttpsUtil;
import com.sc.mp.util.LuceneUtil;
import com.sc.mp.util.StringUtil;
import com.sc.mp.util.UUID19;
import com.sc.mp.util.UnixtimeUtil;

@Controller
@RequestMapping(value = "xcx")
public class XcxController {
	private static final Logger logger = LoggerFactory.getLogger(XcxController.class);
	
	@Resource
	SendRecordService sendRecordService;
	
	@Autowired
	AnestheticMapper anestheticMapper;
	@Autowired
	OperativeMapper operativeMapper;
	@Autowired
	DocMapper docMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	WebScGzhMediaMapper webScGzhMediaMapper;
	@Autowired
	WebScEvaluateMapper webScEvaluateMapper;
	@Autowired
	WebScLabelMapper webScLabelMapper;
	@Autowired
	WebOrgOperativesMapper webOrgOperativesMapper;
	@Autowired
	WebScDocOperativeMapper webScDocOperativeMapper;
	
	@Value("${operativeName-lucene-path}")
	private String operativeNameLucenePath;
	@Value("${wx-appid}")
	private String appid;
	@Value("${wx-secret}")
	private String secret;
	@Value("${wx-api-uri}")
	private String wxApiUri;
	@Value("${lucene-token}")
	private String luceneToken;
	
	@OperationLog("获取麻醉方法列表")
    @GetMapping(value = "/getAnestheticsAndOperatives")
    @ResponseBody
    public ResultBean getAnestheticsAndOperatives() {
		ResultBean resultBean = null;
		try {
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("anesthetics", anestheticMapper.getWebScAnesthetics());
//			resMap.put("operatives", operativeMapper.getWebScOperatives());
			resultBean = ResultBean.success(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取麻醉方法列表失败，"+e.getMessage());
			resultBean = ResultBean.error("获取麻醉方法列表失败，"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("保存订单")
    @PostMapping(value = "/saveOrder")
    @ResponseBody
	public ResultBean saveOrder(@RequestBody() WebScDoc doc) {
		ResultBean resultBean = null;
		try {
			String docId = UUID19.uuid();
			doc.setDocumentId(docId);
			doc.setDocumentState("0");
			
			//是否是紧急订单，1：紧急，0：不急
			Date operativeDate = DateUtils.parseDate(doc.getOperateStartTime());
			if(UnixtimeUtil.getUnixHour(new Date().getTime())+24>=
			UnixtimeUtil.getUnixHour(operativeDate.getTime())) {
				doc.setDocumentType("1");
			}else {
				doc.setDocumentType("0");
			}
			//病人类型，1：未成年人，2：中年人，3：老年人
			if (doc.getPatientAge()<=16) {
				doc.setPatienttypeId("1");
			}else if (doc.getPatientAge()>16&&doc.getPatientAge()<=60) {
				doc.setPatienttypeId("2");
			}else {
				doc.setPatienttypeId("3");
			}
			
			docMapper.insert(doc);
			
			String operatives[] = doc.getOperativeId().split(",");
			for (int i = 0; i < operatives.length; i++) {
				WebScDocOperative webScDocOperative = new WebScDocOperative(doc, operatives[i]);
				webScDocOperativeMapper.insert(webScDocOperative);
			}
			
			//插入通知消息
			WebScUser user = userMapper.selectByPrimaryKey(doc.getApplyUserId());
			sendRecordService.insertSendRecord(docId, user, 1001, "0");
			
			resultBean = ResultBean.success("订单发布成功（订单号："+docId+"）");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单发布失败，"+e.getMessage());
			resultBean = ResultBean.error("订单发布失败，"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("获取订单明细")
    @GetMapping(value = "/getDocDetail")
    @ResponseBody
	public ResultBean getDocDetail(@RequestParam("documentId") String documentId) {
		ResultBean resultBean = null;
		try {
			logger.info(documentId);
			WebScDoc webScDoc = docMapper.selectByPrimaryKey(documentId);
			
			//手术名称
			String operativeIds[] = webScDoc.getOperativeId().split(",");
			String operativeName = "";
			for (int i = 0; i < operativeIds.length; i++) {
				if(i == 0) {
					operativeName = operativeMapper.selectOperativeById(operativeIds[i]).getOperativeName();
				}else {
					operativeName = operativeName +"，"+operativeMapper.selectOperativeById(operativeIds[i]).getOperativeName();
				}
			}
			webScDoc.setOperativeName(operativeName);
			
			//麻醉方法
			webScDoc.setAnestheticName(anestheticMapper.selectAnestheticById(webScDoc.getAnestheticId()).getAnestheticName());
			
			//格式化手术时间
			webScDoc.setOperateStartTime(
					DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", 
							DateUtils.parseDate(webScDoc.getOperateStartTime())));
			
			//麻醉医生
			if(StringUtil.isNotEmpty(webScDoc.getQaUserId())) {
				String qaDoctorName = userMapper.selectByPrimaryKey(webScDoc.getQaUserId()).getUserName();
				
				webScDoc.setQaUserName(qaDoctorName.substring(0, 1)+"医生");
			}else {
				webScDoc.setQaUserName("");
			}
			
			//手术医生
//			if(StringUtil.isNotEmpty(webScDoc.getOperateUser())) {
//				String operativeUsers[] = webScDoc.getOperateUser().split(";");
//				String operativeUser = "";
//				for (String ou : operativeUsers) {
//					if(StringUtil.isEmpty(operativeUser)) {
//						operativeUser = ou.substring(0, 1)+"医生";
//					}else {
//						operativeUser = operativeUser +";"+ ou.substring(0, 1)+"医生";
//					}
//				}
//			}
			
			resultBean = ResultBean.success(webScDoc);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取订单明细失败，"+e.getMessage());
			resultBean = ResultBean.error("获取订单明细失败，"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("查询订单列表")
    @PostMapping(value = "/searchOrderList")
    @ResponseBody
	public PageResultBean<WebScDoc> searchOrderList(@RequestBody() Map<String, Object> paraMap) {
		PageResultBean<WebScDoc> prb = null;
		try {
			logger.info(paraMap.toString());
			if (StringUtil.isNotNull(paraMap.get("qaUser"))) {
				paraMap.put("qaUserId", userMapper.getDoctorId(paraMap.get("qaUser").toString()));
			}else {
				paraMap.put("qaUserId", "");
			}
			
			prb = new PageResultBean<WebScDoc>();
			PageHelper.startPage(paraMap.get("page")==null?1:(int)paraMap.get("page"), 
					paraMap.get("limit")==null?10:(int)paraMap.get("limit"));
			
			List<WebScDoc> docs = docMapper.selectXcxDocsByConditions(paraMap);
			for (WebScDoc doc : docs) {
				//手术名称
				String operativeIds[] = doc.getOperativeId().split(",");
				String operativeName = "";
				for (int i = 0; i < operativeIds.length; i++) {
					if(i == 0) {
						operativeName = operativeMapper.selectOperativeById(operativeIds[i]).getOperativeName();
					}else {
						operativeName = operativeName +"，"+operativeMapper.selectOperativeById(operativeIds[i]).getOperativeName();
					}
				}
				doc.setOperativeName(operativeName);
				
				//手术时间
				doc.setOperateStartTime(
						DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", 
								DateUtils.parseDate(doc.getOperateStartTime())));
				//手术状态
				doc.setDocumentState(DocStateEnum.getvalueOf(doc.getDocumentState()).getTxt());
				//麻醉方法
				doc.setAnestheticName(anestheticMapper.selectAnestheticById(doc.getAnestheticId()).getAnestheticName());
				//麻醉医生
				if(StringUtil.isNotEmpty(doc.getQaUserId())) {
					String qaDoctorName = userMapper.selectByPrimaryKey(doc.getQaUserId()).getUserName();
					
					doc.setQaUserName(qaDoctorName.substring(0, 1)+"医生");
				}else {
					doc.setQaUserName("");
				}
				
			}
			
			PageInfo<WebScDoc> pageInfo = new PageInfo<>(docs);
			
			prb.setCount(Long.valueOf(pageInfo.getTotal()).intValue());
			prb.setData(docs);
			prb.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取订单列表，"+e.getMessage());
			prb = new PageResultBean<WebScDoc>();
			prb.setCode(-1);
		}
		
        return prb;
    }
	
	@OperationLog("评价")
    @PostMapping(value = "/orderEvaluate")
    @ResponseBody
	public ResultBean orderEvaluate(@RequestBody() WebScEvaluate evaluate) {
		ResultBean resultBean = null;
		try {
			WebScDoc doc = docMapper.selectByPrimaryKey(evaluate.getDocumentId());
			evaluate.setKind("0");
			evaluate.setOrgId(doc.getOrgId());
			evaluate.setUserId(doc.getQaUserId());
			
			docMapper.updDocById(evaluate.getDocumentId(), evaluate.getRemark(), evaluate.getScore());
			
			webScEvaluateMapper.insert(evaluate);
			
			List<WebScLabel> labels = webScLabelMapper.selectLabelsByIds(evaluate.getLabelId());
			evaluate.setLabels(labels);
			
			resultBean = ResultBean.success(evaluate);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("评价失败（订单号："+evaluate.getDocumentId()+"），"+e.getMessage());
			resultBean = ResultBean.error("评价失败（订单号："+evaluate.getDocumentId()+"），"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("获取评价信息")
    @GetMapping(value = "/getEvaluate")
    @ResponseBody
	public ResultBean getEvaluate(@RequestParam("documentId") String documentId) {
		ResultBean resultBean = null;
		try {
			WebScEvaluate webScEvaluate = webScEvaluateMapper.selectEvaluate(documentId);
			
			List<WebScLabel> labels = webScLabelMapper.selectLabelsByIds(webScEvaluate.getLabelId());
			webScEvaluate.setLabels(labels);
			
			resultBean = ResultBean.success(webScEvaluate);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取订单明细失败，"+e.getMessage());
			resultBean = ResultBean.error("获取订单明细失败，"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("获取评价标签信息")
    @GetMapping(value = "/getEvaluateLabels")
    @ResponseBody
	public ResultBean getEvaluateLabels() {
		ResultBean resultBean = null;
		try {
			List<WebScLabel> labels = webScLabelMapper.selectLabels();
			List<List<WebScLabel>> rLabelss = new ArrayList<List<WebScLabel>>();
			List<WebScLabel> hLabels = new ArrayList<WebScLabel>();
			List<WebScLabel> yLabels = new ArrayList<WebScLabel>();
			List<WebScLabel> cLabels = new ArrayList<WebScLabel>();
			for (WebScLabel webScLabel : labels) {
				if("1".equals(webScLabel.getLabelLevel())) {
					hLabels.add(webScLabel);
				}else if("2".equals(webScLabel.getLabelLevel())) {
					yLabels.add(webScLabel);
				}else if("3".equals(webScLabel.getLabelLevel())){
					cLabels.add(webScLabel);
				}
			}
			rLabelss.add(hLabels);
			rLabelss.add(yLabels);
			rLabelss.add(cLabels);
			
			resultBean = ResultBean.success(rLabelss);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取评价标签信息失败，"+e.getMessage());
			resultBean = ResultBean.error("获取评价标签信息失败，"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("登录")
    @PostMapping(value = "/login")
    @ResponseBody
	public ResultBean login(@RequestBody() Map<String, Object> paraMap) {
		ResultBean resultBean = null;
		try {
			logger.info(paraMap.toString());
			//0、check用户名和密码
			WebScUser webScUser = userMapper.selectByLoginName(paraMap.get("name").toString());
			if(StringUtil.isNull(webScUser)) {
				logger.info("用户名有误，loginname:"+paraMap.get("name").toString());
				resultBean = ResultBean.error("用户名或密码有误");
			}else {
				if(!(RoleEnum.JGDDLRY.getCode()+"").equals(webScUser.getRoleId())) {
					logger.info("用户角色有误，loginname:"+webScUser.getLoginName());
					resultBean = ResultBean.error("不存在账号为“"+webScUser.getLoginName()+"”的录入人员");
				}else {
					String pws = new Md5Hash(paraMap.get("pws").toString(), webScUser.getSalt()).toString();
					if(pws.equals(webScUser.getLoginPwd())) {
						//1、获取openId和sessionKey
						URI uri = new URI(
								wxApiUri
								+ "appid="+appid
										+ "&secret="+secret
												+ "&js_code="+paraMap.get("code").toString()
														+ "&grant_type="+paraMap.get("code").toString());
						String responseMsg = HttpsUtil.getHttps(uri);
						JSONObject responseMsgJson = JSONObject.parseObject(responseMsg);
						
						//2、保存openId到数据库
						userMapper.updOpenid(responseMsgJson.getString("openid"), webScUser.getUserId());
						
						//3、将用户信息、openId和sessionKey传到前台
						Map<String, Object> resMap = new HashMap<String, Object>();
						resMap.put("openid", responseMsgJson.getString("openid"));
						resMap.put("session_key", responseMsgJson.getString("session_key"));
						resMap.put("userInfo", webScUser);
						
						resultBean = ResultBean.success(resMap);
					}else {
						logger.info("密码有误，loginname:"+webScUser.getLoginName());
						resultBean = ResultBean.error("用户名或密码有误");
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("登录失败，"+e.getMessage());
			resultBean = ResultBean.error("登录失败，"+e.getMessage());
		}
		
        return resultBean;
    }
	
	@OperationLog("登出")
    @PostMapping(value = "/signOut")
    @ResponseBody
	public ResultBean signOut(@RequestBody() Map<String, Object> paraMap){
		ResultBean resultBean = null;
		try {
			
			userMapper.updOpenid("", paraMap.get("userId").toString());
			
			resultBean = ResultBean.success("登出成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("登出失败，"+e.getMessage());
			resultBean = ResultBean.error("登出失败，"+e.getMessage());
		}
		
        return resultBean;
	}
	
	@OperationLog("自动登录")
    @PostMapping(value = "/autoLogin")
    @ResponseBody
    public ResultBean autoLogin(@RequestBody() Map<String, Object> paraMap){
		ResultBean resultBean = null;
		try {
			//1、获取openId和sessionKey
			URI uri = new URI(
					wxApiUri
					+ "appid="+appid
							+ "&secret="+secret
									+ "&js_code="+paraMap.get("code").toString()
											+ "&grant_type="+paraMap.get("code").toString());
			String responseMsg = HttpsUtil.getHttps(uri);
			JSONObject responseMsgJson = JSONObject.parseObject(responseMsg);
			
			WebScUser webScUser = userMapper.selectUserByOpenid(responseMsgJson.getString("openid"));
			if(StringUtil.isNull(webScUser)) {
				resultBean = new ResultBean("未绑定微信openid", null, 2);
			}else {
				//3、将用户信息、openId和sessionKey传到前台
				Map<String, Object> resMap = new HashMap<String, Object>();
				resMap.put("openid", responseMsgJson.getString("openid"));
				resMap.put("session_key", responseMsgJson.getString("session_key"));
				resMap.put("userInfo", webScUser);
				resultBean = ResultBean.success(resMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("自动登录失败，"+e.getMessage());
			resultBean = ResultBean.error("自动登录失败，"+e.getMessage());
		}
		
        return resultBean;
	}
	
	@OperationLog("搜索手术名称列表")
    @PostMapping(value = "/searchOperativeNames")
    @ResponseBody
	public ResultBean searchOperativeNames(@RequestBody() Map<String, Object> paraMap) {
		ResultBean resultBean = null;
		try {
			List<Map<String, String>> operatives = new ArrayList<Map<String,String>>();
			if (StringUtil.isEmpty(paraMap.get("key").toString())) {
				//从数据库中拿取
				List<WebOrgOperatives> orgOperatives = webOrgOperativesMapper.selectOrgOperatives(
						paraMap.get("orgId").toString(), 8);
				for (WebOrgOperatives webOrgOperatives : orgOperatives) {
					Map<String, String> hitMap = new HashMap<String, String>();
	                //手术名称
	                hitMap.put("text", webOrgOperatives.getOperativeName());
	                hitMap.put("value", webOrgOperatives.getOperativeId());
	                operatives.add(hitMap);
				}
			}else {
				operatives = LuceneUtil.searchOperativeNames(
						operativeNameLucenePath, paraMap.get("key").toString(), 5);
			}
			
			resultBean = ResultBean.success(operatives);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("搜索手术名称列表失败，"+e.getMessage());
			resultBean = ResultBean.error("搜索手术名称列表失败，"+e.getMessage());
		}
		return resultBean;
	}
	
	@OperationLog("获取前七天手术量信息")
    @GetMapping(value = "/getBeforeSevenOperativeCount")
    @ResponseBody
	public ResultBean getBeforeSevenOperativeCount(@RequestParam("orgId") String orgId) {
		
		ResultBean resultBean = null;
		try {
			//1、获取前七天的手术量
			List<Map<String, Object>> operativeCountList = new ArrayList<Map<String,Object>>();
			long nowDate = UnixtimeUtil.getUnixDay(new Date().getTime());
			for (int i = (int) (nowDate-7); i < nowDate; i++) {
				Map<String, Object> operativeCountMap = new HashMap<String, Object>();
				
				String startDateStr = UnixtimeUtil.getStringDay(i);
				String endDateStr = UnixtimeUtil.getStringDay(i+1);
				Date date = DateUtils.parseDate(startDateStr);
				
				int operativeCount = docMapper.selectOperativeCount(orgId, startDateStr, endDateStr);
				
				operativeCountMap.put("date", CalendarUtil.getMonth(date)+"月"+CalendarUtil.getDay(date)+"号");
				operativeCountMap.put("count", operativeCount);
				
				operativeCountList.add(operativeCountMap);
			}
			resultBean = ResultBean.success(operativeCountList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("搜索手术名称列表失败，"+e.getMessage());
			resultBean = ResultBean.error("搜索手术名称列表失败，"+e.getMessage());
		}
		return resultBean;
	}
	
	@OperationLog("更新手术名Lucene文件")
	@PostMapping(value = "/updOperativeLucene")
    @ResponseBody
	public ResultBean updOperativeLucene(@RequestBody() Map<String, Object> paraMap) {
		ResultBean resultBean = null;
		try {
			String token = paraMap.get("token").toString();
			if (luceneToken.equals(token)) {
				List<WebScOperative> webScOperatives = operativeMapper.getWebScOperatives();
				LuceneUtil.createOperativeNameIndex(operativeNameLucenePath, webScOperatives);
				resultBean = ResultBean.success("手术名Lucene文件更新成功");
			}else {
				resultBean = ResultBean.error("token有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新手术名Lucene文件失败，"+e.getMessage());
			resultBean = ResultBean.error("更新手术名Lucene文件失败，"+e.getMessage());
		}
		return resultBean;
	}

	@OperationLog("获取订单统计信息")
    @GetMapping(value = "/getOrderStatistics")
    @ResponseBody
	public ResultBean getOrderStatistics(@RequestParam("orgId") String orgId) {
		ResultBean resultBean = null;
		try {
			Map<String, Object> resMap = new HashMap<String, Object>();
			Date curDate = new Date();
			//日手术量
			long currentDateStamp = UnixtimeUtil.getUnixDay(curDate.getTime());
			List<KeyValue> todaySslKeyValues = docMapper.selectSslStatisticsByState(
					orgId, 
					UnixtimeUtil.getStringDay(currentDateStamp), 
					UnixtimeUtil.getStringDay(currentDateStamp+1));
			resMap.put("todaySsl", todaySslKeyValues);
			
			//月度手术量
			List<KeyValue> ydSslKeyValues = docMapper.selectSslStatisticsByState(
					orgId, 
					DateUtils.dateTime(CalendarUtil.getFirstDayOfGivenMonth(curDate)), 
					DateUtils.dateTime(CalendarUtil.getFirstDayOfNextMonth(curDate)));
			resMap.put("ydSsl", ydSslKeyValues);
			
			//周手术量
			List<OperationCount> wocs = docMapper.xcxStatsByWeekForOrgan(
					orgId, "", DateUtils.isSunday() ? "0" : "-1");
			List<KeyValue> weekSsl = new ArrayList<KeyValue>();
			for (OperationCount oc : wocs) {
				KeyValue kv = new KeyValue();
				kv.setName(oc.getDate());
				kv.setValue(oc.getCount());
				weekSsl.add(kv);
			}
			resMap.put("weekSsl", weekSsl);
			String title = CalendarUtil.getYear(curDate)+"-"+wocs.get(0).getDate().split("/")[0]+
					" 到 "+
					CalendarUtil.getYear(curDate)+"-"+wocs.get(6).getDate().split("/")[0];
			resMap.put("weekTitle", title);
			
			//月手术量
			List<OperationCount> mocs = docMapper.statsByMonthForOrgan(orgId, "");
			List<KeyValue> monthSsl = new ArrayList<KeyValue>();
			for (OperationCount oc : mocs) {
				KeyValue kv = new KeyValue();
				kv.setName(oc.getDate());
				kv.setValue(oc.getCount());
				monthSsl.add(kv);
			}
			resMap.put("monthSsl", monthSsl);
			resMap.put("monthTitle", CalendarUtil.getMonth(curDate)+"月");
			
			//年手术量
			List<OperationCount> yocs = docMapper.statsByYearForOrgan(orgId, "");
			List<KeyValue> yearSsl = new ArrayList<KeyValue>();
			for (OperationCount oc : yocs) {
				KeyValue kv = new KeyValue();
				kv.setName(oc.getDate());
				kv.setValue(oc.getCount());
				yearSsl.add(kv);
			}
			resMap.put("yearSsl", yearSsl);
			resMap.put("yearTitle", CalendarUtil.getYear(curDate)+"年");
			
			resultBean = ResultBean.success(resMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取订单统计信息失败，"+e.getMessage());
			resultBean = ResultBean.error("获取订单统计信息失败，"+e.getMessage());
		}
		return resultBean;
	}
	@OperationLog("订单统计上下翻页")
    @PostMapping(value = "/getOrderStatisticsNextBack")
    @ResponseBody
	public ResultBean getOrderStatisticsNextBack(@RequestBody() Map<String, Object> paraMap){
		ResultBean resultBean = null;
		try {
			Date curDate = new Date();
			Map<String, Object> resMap = new HashMap<String, Object>();
			switch (paraMap.get("flag").toString()) {
			case "week":
				Date date = CalendarUtil.dateAddDays(curDate, (int)paraMap.get("pageNum")*7);
				String sunday = DateUtils.isSunday(date)?"0":"-1";
				List<OperationCount> wocs = docMapper.xcxStatsByWeekForOrgan2(
						paraMap.get("orgId").toString(), "", sunday, DateUtils.parseDateToStr("yyyy-MM-dd", date));
				
				List<KeyValue> weekSsl = new ArrayList<KeyValue>();
				for (OperationCount oc : wocs) {
					KeyValue kv = new KeyValue();
					kv.setName(oc.getDate());
					kv.setValue(oc.getCount());
					weekSsl.add(kv);
				}
				resMap.put("ssl", weekSsl);
				String title = CalendarUtil.getYear(date)+"-"+wocs.get(0).getDate().split("/")[0]+
						" 到 "+
						CalendarUtil.getYear(date)+"-"+wocs.get(6).getDate().split("/")[0];
				resMap.put("title", title);
				
				resultBean = ResultBean.success(resMap);
				break;
			case "month":
				Date date2 = CalendarUtil.monthAdds(curDate, (int)paraMap.get("pageNum"));
				List<OperationCount> mocs = docMapper.xcxStatsByMonthForOrgan(paraMap.get("orgId").toString(), "", DateUtils.parseDateToStr("yyyy-MM-dd", date2));
				
				List<KeyValue> monthSsl = new ArrayList<KeyValue>();
				for (OperationCount oc : mocs) {
					KeyValue kv = new KeyValue();
					kv.setName(oc.getDate());
					kv.setValue(oc.getCount());
					monthSsl.add(kv);
				}
				resMap.put("ssl", monthSsl);
				resMap.put("title", CalendarUtil.getMonth(date2)+"月");
				
				resultBean = ResultBean.success(resMap);
				break;
			case "year":
				Date date3 = CalendarUtil.yearAdds(curDate, (int)paraMap.get("pageNum"));
				List<OperationCount> yocs = docMapper.xcxStatsByYearForOrgan(paraMap.get("orgId").toString(), "", DateUtils.parseDateToStr("yyyy-MM-dd", date3));
				
				List<KeyValue> yearSsl = new ArrayList<KeyValue>();
				for (OperationCount oc : yocs) {
					KeyValue kv = new KeyValue();
					kv.setName(oc.getDate());
					kv.setValue(oc.getCount());
					yearSsl.add(kv);
				}
				resMap.put("ssl", yearSsl);
				resMap.put("title", CalendarUtil.getYear(date3)+"年");
				
				resultBean = ResultBean.success(resMap);
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单统计上下翻页失败，"+e.getMessage());
			resultBean = ResultBean.error("订单统计上下翻页失败，"+e.getMessage());
		}
		return resultBean;
	}
	
	@OperationLog("获取公众号图文信息")
    @PostMapping(value = "/getGzhMedias")
    @ResponseBody
	public PageResultBean<WebScGzhMedia> getGzhMedias(@RequestBody() Map<String, Object> paraMap) {
		PageResultBean<WebScGzhMedia> prb = null;
		try {
			logger.info(paraMap.toString());
			
			prb = new PageResultBean<WebScGzhMedia>();
			PageHelper.startPage(paraMap.get("page")==null?1:(int)paraMap.get("page"), 
					paraMap.get("limit")==null?10:(int)paraMap.get("limit"));
			
			List<WebScGzhMedia> medias = webScGzhMediaMapper.selectGzhMedias();
			
			PageInfo<WebScGzhMedia> pageInfo = new PageInfo<>(medias);
			
			prb.setCount(Long.valueOf(pageInfo.getTotal()).intValue());
			prb.setData(medias);
			prb.setCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取公众号图文信息列表出错，"+e.getMessage());
			prb = new PageResultBean<WebScGzhMedia>();
			prb.setCode(-1);
		}
		
        return prb;
    }
}
