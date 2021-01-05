package com.sc.mp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sc.mp.mapper.DocMapper;
import com.sc.mp.model.StateCount;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.WebScUser_Distribution;
import com.sc.mp.util.DistrictUtil;


@Service
public class DocService {
	@Resource
	private DocMapper docMapper;
	
    public List<WebScDoc> selectWebScDocList(WebScDoc doc){
    	return docMapper.selectWebScDocList(doc);
    }
    
    public WebScDoc selectWebScDoc(WebScDoc doc){
    	List<WebScDoc> list = docMapper.selectWebScDocList(doc);
    	if(list != null && list.size() > 0)
    		return list.get(0);
    	else
    		return null;
    }
    
    public int updateDocInfoByMap(Map<String, Object> updateMap){
    	return docMapper.updateDocInfoByMap(updateMap);
    }
    
    @Transactional
    public int updateTmpDoc(Map<String, Object> updateMap, Map<String, Object> tmpUpdateMap){
    	docMapper.updateDocInfoByMap(updateMap);
    	
    	return docMapper.updateTmpDocInfoByMap(tmpUpdateMap);
    }
    
    @Transactional
    public int updateTmpDoc2(Map<String, Object> updateMap, Map<String, Object> tmpUpdateMap){
    	docMapper.updateDocInfoByMap(updateMap);
    	
    	return docMapper.updateTmpDocInfoByMapOnlyState(tmpUpdateMap);
    }
    
    /**
     * 
     * @Title: getStateCount   
     * @Description: 获取状态计数
     * @param: @param doc
     * @param: @return      
     * @return: StateCount      
     * @throws
     */
    public StateCount getStateCount(WebScDoc doc){
    	StateCount sc = new StateCount();
    	List<WebScDoc> ls = docMapper.selectWebScDocList(doc);
    	
    	for(WebScDoc td : ls){
    		if(td.getDocumentState().equals("0")){
    			sc.setiCount_0(sc.getiCount_0() + 1);
    		}else if(td.getDocumentState().equals("1")){
    			sc.setiCount_1(sc.getiCount_1() + 1);
    			sc.setiCount_123(sc.getiCount_123() + 1);
    		}else if(td.getDocumentState().equals("2")){
    			sc.setiCount_2(sc.getiCount_2() + 1);
    			sc.setiCount_123(sc.getiCount_123() + 1);
    		}else if(td.getDocumentState().equals("3")){
    			sc.setiCount_3(sc.getiCount_3() + 1);
    			sc.setiCount_123(sc.getiCount_123() + 1);
    		}else if(td.getDocumentState().equals("4")){
    			sc.setiCount_4(sc.getiCount_4() + 1);
    		}else if(td.getDocumentState().equals("9")){
    			sc.setiCount_9(sc.getiCount_9() + 1);
    		}
    	}
    	
    	return sc;
    }
    
    /**
     * 
     * @Title: getDistributionDrGridList   
     * @Description: 订单分配获取医生信息用
     * @param: @param documentId
     * @param: @param qaName
     * @param: @param user
     * @param: @return      
     * @return: List<WebScUser_Distribution>      
     * @throws
     */
    public List<WebScUser_Distribution> getDistributionDrGridList(String documentId, String qaName, WebScUser user){
		List<WebScUser_Distribution> drList = new ArrayList<WebScUser_Distribution>();
    	
    	//获取订单数据
    	WebScDoc doc = new WebScDoc();
    	doc.setDocumentId(documentId);
    	List<WebScDoc> docList = docMapper.selectWebScDocList(doc);
    	
    	if(docList != null){
	    	doc = docList.get(0);
	    	
	    	//获取医院信息
	    	WebScOrganization org = docMapper.findWebScDocOrg(documentId);
	    	
	    	//获取当天医院匹配的医生
	    	
	    	List<String> uls = docMapper.getWorkDr(doc.getOperateStartTime());
	    	
	    	//创建查询条件
	    	Map<String, String> searchMap = new HashMap<String, String>();
	    	searchMap.put("qaName", qaName);
	    	searchMap.put("province", user.getProvince());
	    	searchMap.put("org_id",org.getOrgId());
	    	searchMap.put("operate_start_time", doc.getOperateStartTime());
	    	//获取医生信息
	    	drList = docMapper.getDistributionDrGridList(searchMap);
	    	
	    	for(WebScUser_Distribution duser : drList){
	    		int iDistributionScore = 0;
	    		int iScope = 0;
	    		//匹配当日医院
	    		duser.setIswork("0");
	    		if(uls != null && uls.size() > 0){
	    			for(String userid : uls){
	    				if(duser.getUserId().equals(userid)){
	    					duser.setIswork("1");
	    					iDistributionScore += 3;
	    					break;
	    				}
	    			}
	    		}
	    		//当日有空
	    		if(duser.getIscalendar() != null && !duser.getIscalendar().equals("")){
	    			iDistributionScore += 2;
	    			duser.setIscalendar("1");
	    		}else{
	    			duser.setIscalendar("0");
	    		}
	    		//备案
	    		if(duser.getIsrecord() != null && !duser.getIsrecord().equals("")){
	    			iDistributionScore += 2;
	    			duser.setIscalendar("1");
	    		}else{
	    			duser.setIsrecord("0");
	    		}
	    		
	    		//所在区域范围
	    		if(org.getProvince().equals(duser.getProvince())){
	    			iDistributionScore += 1;
	    			iScope += 1;
	    		}
	    		
	    		if(org.getCity() != null && duser.getCity() != null){
	    			if(org.getCity().equals(duser.getCity())){
	    				iDistributionScore += 1;
		    			iScope += 1;
	    			}
	    		}
	    		
	    		if(org.getArea() != null && duser.getArea() != null){
	    			if(org.getArea().equals(duser.getArea())){
	    				iDistributionScore += 1;
		    			iScope += 1;
	    			}
	    		}
	    		duser.setIsScope(iScope + "");
	    		
	    		//病人类型
	    		if(duser.getPatient_type() != null){
	    			//订单病人类型
	    			int age = doc.getPatientAge();
	    			String docPatType = "0";
	    			if(age <= 16) docPatType = "1";
	    			else if(age > 16 && age <= 60) docPatType = "2";
	    			else docPatType = "3";
	    			
	    			duser.setIsPatient("0");
	    			String[] pat = duser.getPatient_type().split(",");
	    			for(String str : pat){
	    				if(str.equals(docPatType)){
	    					duser.setIsPatient("1");
	    					iDistributionScore += 1;
	    					break;
	    				}
	    			}
	    		}
	    		
	    		//省/市/区 转换成中文
	    		if(duser.getProvince() != null && !duser.getProvince().equals(""))
	    			duser.setProvince(DistrictUtil.getDistrictByCode(duser.getProvince()).getName());
	    		if(duser.getCity() != null && !duser.getCity().equals(""))
	    			duser.setCity(DistrictUtil.getDistrictByCode(duser.getCity()).getName());
	    		if(duser.getArea() != null && !duser.getArea().equals(""))
	    			duser.setArea(DistrictUtil.getDistrictByCode(duser.getArea()).getName());
	    		
	    		duser.setDistributionScore(iDistributionScore);
	    	}
	    	
	    	//list 排序
	    	Collections.sort(drList, new Comparator<WebScUser_Distribution>() {  
	    		  
	            @Override  
	            public int compare(WebScUser_Distribution o1, WebScUser_Distribution o2) {  
	                int i = o1.getDistributionScore() - o2.getDistributionScore();
	                if(i == 0){
	                	return o1.getUserName().compareTo(o2.getUserName());
	                }
	                return -i;  
	            }  
	        });
    	}
    	return drList;
    }

    public List<WebScUser> getTransferUser(Map<String, String> searchMap){
    	return docMapper.getTransferUser(searchMap);
    }
    
    public List<WebScUser> getQaUserInfo(Map<String, String> searchMap){
    	return docMapper.getQaUserInfo(searchMap);
    }
}
