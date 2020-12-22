package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScAnesthetic;

@Mapper
public interface AnestheticMapper {
	@Select(value = "SELECT * FROM WSC_ANESTHETIC")
	List<WebScAnesthetic> getWebScAnesthetics();
	
	@Select("SELECT * FROM WSC_ANESTHETIC WHERE anesthetic_id = #{anestheticId} limit 1")
	WebScAnesthetic selectAnestheticById(@Param("anestheticId") String anestheticId);
	
	@Select("SELECT * FROM WSC_ANESTHETIC WHERE anesthetic_name = #{anestheticName} limit 1")
	WebScAnesthetic selectAnesthetic(@Param("anestheticName") String anestheticName);
	
	public List<WebScAnesthetic> getWebScAnestheticList(String anestheticName);
}
