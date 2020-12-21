package com.sc.mp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sc.mp.bean.OperationCount;
import com.sc.mp.model.WebScDoc;

@Mapper
public interface DocMapper {
	
	List<WebScDoc> selectWebScDocList(WebScDoc doc);
	
	/**
	 * 当日手术量统计
	 * @return
	 */
	int statsDay();
	
	/**
	 * 当月手术量统计
	 * @return
	 */
	int statsMonth();
	
	/**
	 * 当年手术量统计
	 * @return
	 */
	int statsYear();
	
	/**
	 * 按年统计每月手术量
	 * @param year				今年
	 * @param lastYear			去年
	 * @param beforeLastYear	前年
	 * @return
	 */
	List<OperationCount> statsByYear(@Param("thisYear") Integer thisYear, @Param("lastYear") Integer lastYear,
			@Param("beforeLastYear") Integer beforeLastYear);
}
