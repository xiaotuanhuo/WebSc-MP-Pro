package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sc.mp.model.WebScOrganization;

public interface OrganizationMapper {
	
	/**
	 * 医疗机构查询
	 * @param province
	 * @param city
	 * @param area
	 * @return
	 */
	List<WebScOrganization> selectOrganizations(@Param("province") String province, @Param("city") String city,
			@Param("area") String area);
    
}