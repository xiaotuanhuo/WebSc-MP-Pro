package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sc.mp.model.WebScSendRecord;

@Mapper
public interface SendRecordMapper {
	void insert(WebScSendRecord record);
	
	void update(WebScSendRecord record);
	
	List<WebScSendRecord> getWebScSendRecord();
}
