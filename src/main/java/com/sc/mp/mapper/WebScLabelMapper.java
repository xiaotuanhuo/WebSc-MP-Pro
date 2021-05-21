package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScLabel;

public interface WebScLabelMapper {
	@Select("SELECT * FROM WSC_LABEL WHERE label_id in (${labelIds})")
	List<WebScLabel> selectLabelsByIds(@Param("labelIds") String labelIds);
	
	@Select("SELECT * FROM WSC_LABEL WHERE leaf = '1' ORDER BY parent_id,label_level ASC")
	List<WebScLabel> selectLabels();
	
    int insert(WebScLabel record);

    int insertSelective(WebScLabel record);
}