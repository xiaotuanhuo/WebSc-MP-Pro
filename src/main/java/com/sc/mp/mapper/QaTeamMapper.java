package com.sc.mp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sc.mp.model.WebScUser;

@Mapper
public interface QaTeamMapper {
	//获取团队信息
	List<WebScUser> getQaTeamInfo(String qaTeamId);
	
	int insertQaUser(Map<String, String> insertMap);
	
	int deleteQaUser(Map<String, String> deleteMap);
}
