package com.sc.mp.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sc.mp.bean.WebScDoc;
import com.sc.mp.bean.WebScUser;
import com.sc.mp.service.DocService;
import com.sc.mp.util.ControllerUtil;


@Controller
@RequestMapping(value="doc")
public class DocController {

	@Resource
	DocService docService;
	
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
    public void DocListInit(HttpServletRequest request, 
    					  	HttpServletResponse response) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		response.setContentType("application/json;charset=UTF-8");
		
		try{
			HttpSession session = request.getSession();
			PrintWriter out = response.getWriter();
//			//获取用户信息
			WebScUser user = ControllerUtil.getUserInfo(request);
//			WebScUser user = webScUserService.("1");
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
			
			List<WebScDoc> docs = docService.selectWebScDocList(docQuery);
			
			
			boolean bFlag = true;
			
			if(user != null){
				if(user.getRoleId().equals("1")){
					searchMap.put("areaBm", user.getAreaBm());
//					String docState;
//					if(docly == 1){
//						//待完成
//						docState = "'0','1','4','9'";
//					}else if(docly == 2){
//						//已完成
//						docState = "'5'";
//					}else{
//						//待取消
//						docState = "'9'";
//					}
//					searchMap.put("documentState", docState);
				}else if(user.getRoleId().equals("2")){
					searchMap.put("userId", user.getUserId());
				}else if(user.getRoleId().equals("3")){
					searchMap.put("applyUserId", user.getHospitalId());
				}else{
					bFlag = false;
				}
				
				List<WebScDoc> docList = new ArrayList<WebScDoc>();
				List<WebScDoc> retList = new ArrayList<WebScDoc>();
				int iDclCount = 0;
				int iDqxCount = 0;
				
				if(bFlag){
					//查询数据
					docList = webScDocService.selectDocList(searchMap);
					
					//手术名称信息List
					Map<String, String> operativenamemap = this.getWebScOperativeNameMap(request, new HashMap<String, Object>());
					
					//数据转换
					for(WebScDoc wsd : docList){
						//手术名称
						String OperativeVal = "";
						if(wsd.getPatienttypeId() != null){
							String[] str = wsd.getOperativeId().split(",");
							for(String s : str){
								OperativeVal += operativenamemap.get(s) + ",";
							}
							OperativeVal = OperativeVal.substring(0, OperativeVal.length() - 1);
							wsd.setOperativeValue(OperativeVal);
						}
						
						if(user.getRoleId().equals("1")){
							if(wsd.getDocumentState().equals("0") || wsd.getDocumentState().equals("1") || wsd.getDocumentState().equals("2") || wsd.getDocumentState().equals("3") || wsd.getDocumentState().equals("4") || wsd.getDocumentState().equals("9")){
								iDclCount++;
								if(docly == 1)	retList.add(wsd);
							}else if(wsd.getDocumentState().equals("9")){
								iDqxCount++;
								if(docly == 3)	retList.add(wsd);
							}else if(wsd.getDocumentState().equals("5")){
								if(docly == 2)	retList.add(wsd);
							}
						}else if(user.getRoleId().equals("2")){
							//隐藏患者中间字
							String patientName = "";
							char[] c = wsd.getPatientName().toCharArray(); 
							if(c.length ==1){
								patientName = wsd.getPatientName();
							}
							if(c.length == 2){
								patientName = wsd.getPatientName().replaceFirst(wsd.getPatientName().substring(1),"*");
							}
							if (c.length > 2) {
								patientName = wsd.getPatientName().replaceFirst(wsd.getPatientName().substring(1, c.length - 1) ,"*");
							}
							wsd.setPatientName(patientName);
							
							if(wsd.getDocumentState().equals("2") || wsd.getDocumentState().equals("3")){
								iDclCount++;
								if(docly == 1)	retList.add(wsd);
							}else if(wsd.getDocumentState().equals("5")){
								if(docly == 2)	retList.add(wsd);
							}
						}
					}
				}
				returnMap.put("code", 1);
				returnMap.put("msg", "");
				returnMap.put("data", retList);
				returnMap.put("dclcount", iDclCount);
				returnMap.put("qxcount", iDqxCount);
			}else{
				returnMap.put("code", 0);
				returnMap.put("msg", "用户信息获取错误，请退出程序后重试！");
			}
			
			JSONObject jsonObject = JSONObject.fromObject(returnMap);
			String returnValue = jsonObject.toString();
			out.write(returnValue);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
