package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sc.mp.model.WebScDoc;

@Mapper
public interface DocMapper {
	
	List<WebScDoc> selectWebScDocList(WebScDoc doc);
	
}
