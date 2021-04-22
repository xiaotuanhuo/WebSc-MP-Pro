package com.sc.mp.mapper;

import java.util.List;

import com.sc.mp.model.WebScRecord;

import io.lettuce.core.dynamic.annotation.Param;

public interface WebScRecordMapper {
	int deleteByPrimaryKey(Integer recordId);
	int insert(WebScRecord record);
	int insertSelective(WebScRecord record);
	WebScRecord selectByPrimaryKey(Integer recordId);
	List<WebScRecord> selectMyRecords(@Param("userId") String userId);
	int updateByPrimaryKeySelective(WebScRecord record);
	int updateByPrimaryKey(WebScRecord record);
}