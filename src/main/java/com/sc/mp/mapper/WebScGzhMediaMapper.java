package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sc.mp.model.WebScGzhMedia;

public interface WebScGzhMediaMapper {
	@Select("SELECT * FROM WSC_GZH_MEDIA ORDER BY update_time DESC")
	List<WebScGzhMedia> selectGzhMedias();
	
	@Select("SELECT '1' FROM WSC_GZH_MEDIA WHERE media_id=#{mediaId}")
	String isExists(String mediaId);
	
    int insert(WebScGzhMedia record);

    int insertSelective(WebScGzhMedia record);
}