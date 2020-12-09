package com.sc.mp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.bean.WebScDoc;

@Mapper
public interface DocMapper {
	
	List<WebScDoc> selectWebScDocList(WebScDoc doc);
	
}
