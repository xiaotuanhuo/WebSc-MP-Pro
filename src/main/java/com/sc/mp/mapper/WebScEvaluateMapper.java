package com.sc.mp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScEvaluate;

public interface WebScEvaluateMapper {
	
	@Select("SELECT * FROM WSC_EVALUATE WHERE document_id=#{documentId}")
	WebScEvaluate selectEvaluate(@Param("documentId") String documentId);
	
    int insert(WebScEvaluate record);

    int insertSelective(WebScEvaluate record);
}