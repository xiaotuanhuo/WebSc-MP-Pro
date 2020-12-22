package com.sc.mp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sc.mp.bean.OperationCount;
import com.sc.mp.model.WebScDoc;

@Mapper
public interface DocMapper {
	
	@Select("SELECT * FROM WSC_DOCUMENT WHERE org_id=#{orgId} AND document_state=#{state} ORDER BY operate_start_time DESC LIMIT ${limit}")
	List<WebScDoc> selectWebScDocs(
			@Param("orgId") String orgId, 
			@Param("state") String state, 
			@Param("limit") int limit);
	
	@Select("SELECT COUNT(*) FROM WSC_DOCUMENT WHERE org_id=#{orgId} AND operate_start_time>=#{startDate} AND operate_start_time<#{endDate}")
	int selectOperativeCount(
			@Param("orgId") String orgId, 
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate);
	
	@Update("UPDATE WSC_DOCUMENT SET hospital_evaluate=${evaluateStar},hospital_evaluate_memo=#{evaluateText} WHERE document_id=#{documentId}")
	int updDocById(@Param("documentId") String documentId, 
			@Param("evaluateText") String evaluateText, 
			@Param("evaluateStar") int evaluateStar);
	
	List<WebScDoc> selectXcxDocsByConditions(Map<String, Object> paraMap);
	
	List<WebScDoc> selectWebScDocList(WebScDoc doc);
	
	int insert(WebScDoc doc);
	
	WebScDoc selectByPrimaryKey(String documentId);
	
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
