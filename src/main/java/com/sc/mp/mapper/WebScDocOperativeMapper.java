package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScDocOperative;

public interface WebScDocOperativeMapper {
	
	@Select("SELECT operative_id FROM WSC_DOCUMENT_OPERATIVE WHERE org_id = #{orgId} AND operative_id<>'' GROUP BY operative_id ORDER BY count(*) DESC LIMIT ${limit}")
	List<String> getCommonlyUsedOperative(@Param("orgId") String orgId, @Param("limit") int limit);
	
	@Select("SELECT '1' FROM WSC_DOCUMENT_OPERATIVE WHERE document_operative_id=#{id} limit 1")
	String isExist(@Param("id") String id);
	
    int insert(WebScDocOperative record);

    int insertSelective(WebScDocOperative record);
}