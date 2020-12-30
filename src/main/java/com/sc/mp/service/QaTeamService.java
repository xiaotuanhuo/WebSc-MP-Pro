package com.sc.mp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sc.mp.mapper.DocMapper;
import com.sc.mp.mapper.QaTeamMapper;
import com.sc.mp.model.WebScUser;

@Service
public class QaTeamService {
	
	@Resource
	private QaTeamMapper qaTeamMapper;
	
	@Resource
	private DocMapper docMapper;
	
	public List<WebScUser> getQaTeamInfo(String qaTeamId){
		return qaTeamMapper.getQaTeamInfo(qaTeamId);
	}
	
	@Transactional
	public int insertQaUser(Map<String, String> insertMap){
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("documentId", insertMap.get("documentId"));
		updateMap.put("qaTeamId", insertMap.get("documentId"));
		docMapper.updateDocInfoByMap(updateMap);
		
		return qaTeamMapper.insertQaUser(insertMap);
	}
	
	public int deleteQaUser(Map<String, String> deleteMap){
		int iRet = qaTeamMapper.deleteQaUser(deleteMap);
		
		List<WebScUser> userls = qaTeamMapper.getQaTeamInfo(deleteMap.get("documentId"));
		if(userls.size() == 0){
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("documentId", deleteMap.get("documentId"));
			updateMap.put("qaTeamId", "");
			docMapper.updateDocInfoByMap(updateMap);
		}
		return iRet;
	}
}
