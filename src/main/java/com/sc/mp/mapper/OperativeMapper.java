package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScOperative;

@Mapper
public interface OperativeMapper {
	@Select("SELECT * FROM WSC_OPERATIVE WHERE operative_id = #{operativeId} limit 1")
	WebScOperative selectOperativeById(@Param("operativeId") String operativeId);
	
	@Select("SELECT * FROM WSC_OPERATIVE WHERE operative_name = #{operativeName} limit 1")
	WebScOperative selectOperative(@Param("operativeName") String operativeName);
	
	List<WebScOperative> getWebScOperativeList(String operativeName);
}
