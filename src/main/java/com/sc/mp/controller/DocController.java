package com.sc.mp.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sc.mp.model.WebScAnesthetic;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScUser;
import com.sc.mp.service.DocService;
import com.sc.mp.service.UserService;
import com.sc.mp.util.WxUtil;

@Controller
@RequestMapping(value="doc")
public class DocController {
	private static final Logger log = LoggerFactory.getLogger(DocController.class);
			
	@Resource
	DocService docService;
	
	@Resource
	UserService userService;
	
	@Resource
	DataController data;
	
	@Resource
	private WxUtil wxUtil;
	
	@Value("${projectDomainName}")
	private String projectDomainName;
	
	/**
	* 	@Title: toDocList
	*  	@Description: 订单列表跳转
	*/
	@GetMapping(value="/toDocList")
	public String toDocList() {
		return "doc_list";
	}
	
	/**
	* 	@Title: DocListInit
	*  	@Description: 订单列表跳转
	*/ 
	@RequestMapping("/toDocListInit")
	@ResponseBody
    public JSONObject DocListInit(HttpServletRequest request, 
    					  	HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		JSONObject returnMap = new JSONObject();
		
//		//获取用户信息
//		WebScUser user = ControllerUtil.getUserInfo(request);
		
		//测试用
		WebScUser searchUser = new WebScUser();
		searchUser.setUserId(5);
		WebScUser user = userService.selectUserInfo(searchUser);
		
		try{
			if(user != null){
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
				}else if(roleId.equals("5")){
					//医生，查询主治医生
					docQuery.setQaUserId(String.valueOf(user.getUserId()));
					//省，市，区   查询范围
					docQuery.setProvince(user.getProvince());
					docQuery.setCity(user.getCity());
				}
				
				List<WebScDoc> docList = docService.selectWebScDocList(docQuery);
				for(WebScDoc d : docList){
		    		if(d.getStatus() != null && !d.getStatus().equals("")){
		    			d.setPatientName(d.getTmpPatientName());
		    			d.setPatientAge(d.getTmpPatientAge());
		    			d.setPatientSex(d.getTmpPatientSex());
		    			d.setOperativeId(d.getTmpOperativeId());
		    			d.setOperativeName(d.getTmpOperativeName());
		    			d.setAnestheticId(d.getTmpAnestheticId());
		    			d.setAnestheticName(d.getTmpAnestheticName());

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
    					  	  @RequestParam(value = "id") String sDocId) {
		try{
//			//获取用户信息
//			WebScUser user = ControllerUtil.getUserInfo(request);
			//测试用
			WebScUser searchUser = new WebScUser();
			searchUser.setUserId(5);
			WebScUser user = userService.selectUserInfo(searchUser);
			
			WebScDoc searchDoc = new WebScDoc();
			searchDoc.setDocumentId(sDocId);
			WebScDoc doc = docService.selectWebScDoc(searchDoc);
			
			if(doc.getStatus() != null && !doc.getStatus().equals("")){
    			doc.setPatientName(doc.getTmpPatientName());
    			doc.setPatientAge(doc.getTmpPatientAge());
    			doc.setPatientSex(doc.getTmpPatientSex());
    			doc.setOperativeId(doc.getTmpOperativeId());
    			doc.setOperativeName(doc.getTmpOperativeName());
    			doc.setAnestheticId(doc.getTmpAnestheticId());
    			doc.setAnestheticName(doc.getTmpAnestheticName());

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
			String signature = wxUtil.getSignature("http://" + projectDomainName + "/toDocDetail?id=" + doc.getDocumentId(), nonceStr, timestamp, request.getSession());
						
			model.addAttribute("nonceStr", nonceStr);
			model.addAttribute("timestamp",timestamp);
			model.addAttribute("appId", appId);
			model.addAttribute("signature",signature);
			
			//数据缓存
			//获取麻醉方法
			List<WebScAnesthetic> anestheticls = data.getWebScAnestheticList();
			model.addAttribute("anestheticls", anestheticls);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "doc_detail";
	}
}
