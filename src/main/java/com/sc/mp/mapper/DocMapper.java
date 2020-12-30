package com.sc.mp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.WebScUser_Distribution;

@Mapper
public interface DocMapper {
	
	List<WebScDoc> selectWebScDocList(WebScDoc doc);
	
	int updateDocInfoByMap(Map<String, Object> updateMap);
	
	//分配用
	WebScOrganization findWebScDocOrg(String documentId);
	List<String> getWorkDr(String time);
	List<WebScUser_Distribution> getDistributionDrGridList(Map<String, String> searchMap);
	
	//转单
	List<WebScUser> getTransferUser(Map<String, String> searchMap);
	
	//保存草稿/完成订单
	int updateTmpDocInfoByMap(Map<String, Object> updateMap);
	
	//退回待完成
	int updateTmpDocInfoByMapOnlyState(Map<String, Object> updateMap);
	
	//团队
	List<WebScUser> getQaUserInfo(Map<String, String> searchMap);
}
