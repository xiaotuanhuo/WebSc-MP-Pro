package com.sc.mp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sc.mp.bean.KeyValue;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.WebScUser_Distribution;

@Mapper
public interface DocMapper {
	
	/**
	 * 根据document_state订单状态统计某一时间段的手术量
	 * @param orgId 医疗机构id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	@Select("SELECT\r\n" + 
			"	'已完成' AS NAME,\r\n" + 
			"	count(*) AS VALUE\r\n" + 
			"FROM\r\n" + 
			"	WSC_DOCUMENT\r\n" + 
			"WHERE\r\n" + 
			"	org_id = #{orgId}\r\n" + 
			"AND operate_start_time >= #{startDate}\r\n" + 
			"AND operate_start_time < #{endDate}\r\n" + 
			"AND document_state IN ('6', '9')\r\n" + 
			"UNION\r\n" + 
			"	SELECT\r\n" + 
			"		'未完成' AS NAME,\r\n" + 
			"		count(*) AS VALUE\r\n" + 
			"	FROM\r\n" + 
			"		WSC_DOCUMENT\r\n" + 
			"	WHERE\r\n" + 
			"		org_id = #{orgId}\r\n" + 
			"	AND operate_start_time >= #{startDate}\r\n" + 
			"	AND operate_start_time < #{endDate}\r\n" + 
			"	AND document_state NOT IN ('5', '6', '9')\r\n" + 
			"	UNION\r\n" + 
			"		SELECT\r\n" + 
			"			'取消' AS NAME,\r\n" + 
			"			count(*) AS VALUE\r\n" + 
			"		FROM\r\n" + 
			"			WSC_DOCUMENT\r\n" + 
			"		WHERE\r\n" + 
			"			org_id = #{orgId}\r\n" + 
			"		AND operate_start_time >= #{startDate}\r\n" + 
			"		AND operate_start_time < #{endDate}\r\n" + 
			"		AND document_state = '5';")
	List<KeyValue> selectSslStatisticsByState(
			@Param("orgId") String orgId, 
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate);
	
	/**
	 * 获取已完成订单的手术用时
	 * @param paraMap
	 * @return
	 */
	List<WebScDoc> selectWebScDocTmps(Map<String, Object> paraMap);
	
	/**
	 * 获取orgId机构state订单状态的前limit条
	 * @param orgId 医疗机构id
	 * @param state 订单状态
	 * @param limit 前多少条
	 * @return
	 */
	@Select("SELECT * FROM WSC_DOCUMENT WHERE org_id=#{orgId} AND document_state=#{state} ORDER BY operate_start_time DESC LIMIT ${limit}")
	List<WebScDoc> selectWebScDocs(
			@Param("orgId") String orgId, 
			@Param("state") String state, 
			@Param("limit") int limit);
	
	/**
	 * 统计某机构某时间段的手术量
	 * @param orgId 医疗机构id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	@Select("SELECT COUNT(*) FROM WSC_DOCUMENT WHERE org_id=#{orgId} AND operate_start_time>=#{startDate} AND operate_start_time<#{endDate}")
	int selectOperativeCount(
			@Param("orgId") String orgId, 
			@Param("startDate") String startDate, 
			@Param("endDate") String endDate);
	
	/**
	 * 修改订单医疗机构评价信息
	 * @param documentId 订单号
	 * @param evaluateText 评价说明
	 * @param evaluateStar 评价星级
	 * @return
	 */
	@Update("UPDATE WSC_DOCUMENT SET hospital_evaluate=${evaluateStar},hospital_evaluate_memo=#{evaluateText} WHERE document_id=#{documentId}")
	int updDocById(@Param("documentId") String documentId, 
			@Param("evaluateText") String evaluateText, 
			@Param("evaluateStar") float evaluateStar);
	
	/**
	 * 筛选某机构订单
	 * @param paraMap
	 * @return
	 */
	List<WebScDoc> selectXcxDocsByConditions(Map<String, Object> paraMap);
	
	List<WebScDoc> selectWebScDocList(WebScDoc doc);
	

	int updateDocInfoByMap(Map<String, Object> updateMap);
	
	//分配用
	WebScOrganization findWebScDocOrg(String documentId);
	List<String> getWorkDr(String time);
	List<WebScUser_Distribution> getDistributionDrGridList(Map<String, String> searchMap);
	
	//转单
	List<WebScUser> getTransferUser(Map<String, String> searchMap);
	
	//保存草稿/完成订单
	int updateTmpDocInfoByMap(Map<String, Object> updateMap);
	
	//退回待完成
	int updateTmpDocInfoByMapOnlyState(Map<String, Object> updateMap);
	
	//团队
	List<WebScUser> getQaUserInfo(Map<String, String> searchMap);
	
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
	 * 医生当日手术量统计
	 * @return
	 */
	int statsTodayForDc(@Param("userId") String userId);
	
	/**
	 * 医生当月手术量统计
	 * @return
	 */
	int statsMonthForDc(@Param("userId") String userId);
	
	/**
	 * 医生当年手术量统计
	 * @return
	 */
	int statsYearForDc(@Param("userId") String userId);
	
	/**
	 * 医生总手术量统计
	 * @return
	 */
	int statsSumForDc(@Param("userId") String userId);
	
	/**
	 * 按年统计每月手术量
	 * @param year				今年
	 * @param lastYear			去年
	 * @param beforeLastYear	前年
	 * @return
	 */
	List<OperationCount> statsByYear(@Param("thisYear") Integer thisYear, @Param("lastYear") Integer lastYear,
			@Param("beforeLastYear") Integer beforeLastYear);
	
	/**
	 * 统计某医疗机构本周每天的手术量
	 * @param orgId
	 * @param orgName
	 * @return
	 */
	List<OperationCount> statsByWeekForOrgan(@Param("orgId") String orgId, @Param("orgName") String orgName);
	
	/**
	 * 统计某医疗机构当月每天的手术量
	 * @param orgId
	 * @param orgName
	 * @return
	 */
	List<OperationCount> statsByMonthForOrgan(@Param("orgId") String orgId, @Param("orgName") String orgName);
	
	/**
	 * 统计某医疗机构当年每天的手术量
	 * @param orgId
	 * @param orgName
	 * @return
	 */
	List<OperationCount> statsByYearForOrgan(@Param("orgId") String orgId, @Param("orgName") String orgName);
	
	
	/**
	 * 按市统计手术量TopX
	 * @param top	统计前几位
	 * @return
	 */
	List<OperationCount> statsByCity(@Param("top") Integer top);
	
	/**
	 * 已完成手术的列表查询
	 * @param map
	 * @return
	 */
	List<OperationCount> selectReporting(@Param("params") Map<String, String> map);
}
