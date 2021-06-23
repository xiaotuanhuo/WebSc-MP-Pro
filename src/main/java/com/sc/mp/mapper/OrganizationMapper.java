package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScOrganization;

public interface OrganizationMapper {
	@Select("SELECT * FROM WSC_ORGANIZATION WHERE leaf = '1'")
	List<WebScOrganization> getOrgs();
	
	/**
	 * 医疗机构查询
	 * @param province
	 * @param city
	 * @param area
	 * @return
	 */
	List<WebScOrganization> selectOrganizations(@Param("province") String province, @Param("city") String city,
			@Param("area") String area);
    
	/**
	 * 查询市所属医疗机构id
	 * @param city
	 * @return
	 */
	List<String> selectOrgIdsByCity(@Param("city") String city);
}