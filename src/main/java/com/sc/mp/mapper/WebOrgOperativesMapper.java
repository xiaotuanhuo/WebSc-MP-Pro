package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebOrgOperatives;

public interface WebOrgOperativesMapper {
	
	@Select("SELECT * FROM WSC_ORG_OPERATIVES WHERE org_id=#{orgId} LIMIT ${limit}")
	List<WebOrgOperatives> selectOrgOperatives(@Param("orgId") String orgId, @Param("limit") int limit); 
	
    int insert(WebOrgOperatives record);

    int insertSelective(WebOrgOperatives record);
}