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
import com.sc.mp.model.WebScEvaluate;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.WebScUser_Distribution;

@Mapper
public interface DocMapper {
	
	@Select("SELECT * FROM WSC_DOCUMENT ORDER BY CREATE_DATE DESC LIMIT ${limit}")
	List<WebScDoc> selectDocs(@Param("limit") int limit);
	
	/**
	 * 根据document_state订单状态统计某一时间段的手术量
	 * @param orgId 医疗机构id
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	@Select("SELECT\r\n" + 
			"	'取消' AS NAME,\r\n" + 
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
			"			'已完成' AS NAME,\r\n" + 
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
	 * 医生本周手术量统计
	 * @param userId
	 * @param monday	周一日期
	 * @param sunday	周日日期
	 * @return
	 */
	int statsWeekForDc(@Param("userId") String userId, @Param("mon") String monday, @Param("sun") String sunday);
	
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
	 * 按年统计每月手术量
	 * @param year				今年
	 * @param lastYear			去年
	 * @param beforeLastYear	前年
	 * @return
	 */
	List<OperationCount> statsByYear(@Param("thisYear") Integer thisYear, @Param("lastYear") Integer lastYear,
			@Param("beforeLastYear") Integer beforeLastYear);
	
	/**
	 * 统计某医疗机构本周每天的手术量（小程序用）
	 * @param orgId
	 * @param orgName
	 * @param sunday 0：周日 -1：非周日
	 * @return
	 */
	List<OperationCount> xcxStatsByWeekForOrgan(@Param("orgId") String orgId, @Param("orgName") String orgName,
			@Param("sunday") String sunday);
	
	/**
	 * 统计某医疗机构本周每天的手术量（小程序用）
	 * @param orgId
	 * @param orgName
	 * @param sunday 0：周日 -1：非周日
	 * @param date 指定日期当前周数据
	 * @return
	 */
	List<OperationCount> xcxStatsByWeekForOrgan2(@Param("orgId") String orgId, @Param("orgName") String orgName,
			@Param("sunday") String sunday, @Param("date") String date);
	
	/**
	 * 统计某医疗机构指定月每天的手术量
	 * @param orgId
	 * @param orgName
	 * @param date
	 * @return
	 */
	List<OperationCount> xcxStatsByMonthForOrgan(
			@Param("orgId") String orgId, 
			@Param("orgName") String orgName,
			@Param("date") String date);
	
	/**
	 * 统计某医疗机构指定年每月的手术量
	 * @param orgId
	 * @param orgName
	 * @param date
	 * @return
	 */
	List<OperationCount> xcxStatsByYearForOrgan(
			@Param("orgId") String orgId, 
			@Param("orgName") String orgName,
			@Param("date") String date);
	
	/**
	 * 统计某医疗机构本周每天的手术量
	 * @param orgId
	 * @param orgName
	 * @param sunday 0：周日 -1：非周日
	 * @return
	 */
	List<OperationCount> statsByWeekForOrgan(@Param("orgId") String orgId, @Param("orgName") String orgName,
			@Param("sunday") String sunday);
	
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
	 * 统计所选市区所有医疗机构本周每天的手术量
	 * @param organs
	 * @param cityName
	 * @param sunday 0：周日 -1：非周日
	 * @return
	 */
	List<OperationCount> statsByWeekForCity(@Param("organs") List<String> organs, @Param("cityName") String cityName,
			@Param("sunday") String sunday);
	
	/**
	 * 统计所选市区所有医疗机构当月每天的手术量
	 * @param organs
	 * @param cityName
	 * @return
	 */
	List<OperationCount> statsByMonthForCity(@Param("organs") List<String> organs, @Param("cityName") String cityName);
	
	/**
	 * 统计所选市区所有医疗机构当年每月的手术量
	 * @param organs
	 * @param cityName
	 * @return
	 */
	List<OperationCount> statsByYearForCity(@Param("organs") List<String> organs, @Param("cityName") String cityName);
	
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
	
	/**
	 * 日手术量详情
	 * @return
	 */
	List<OperationCount> daySubInfo();
	
	/**
	 * 月手术量详情
	 * @return
	 */
	List<OperationCount> monthSubInfo();
	
	/**
	 * 年手术量详情
	 * @return
	 */
	List<OperationCount> yearSubInfo();
	
	/**
	 * 医疗机构基础信息统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> selectBasicData(@Param("list") List<String> organs);
	
	/**
	 * 医疗机构日手术量统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> selectOrganDay(@Param("list") List<String> organs);
	
	/**
	 * 医疗机构月手术量统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> selectOrganMonth(@Param("list") List<String> organs);
	
	/**
	 * 医疗机构年手术量统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> selectOrganYear(@Param("list") List<String> organs);
	
	/**
	 * 医疗机构自定义日期范围手术量统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> selectOrganDate(@Param("list") List<String> organs, @Param("startTime") String startTime,
			@Param("endTime") String endTime);
	
	
	/**
	 * 医生列表
	 * @return
	 */
	List<OperationCount> statOperativeForDoc();
	
	/**
	 * 医生基础信息统计
	 * @param id
	 * @return
	 */
	List<OperationCount> statDrBasicData(@Param("id") String id);
	
	/**
	 * 医生日手术量详情统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> statDrInfoDay(@Param("id") String id);
	
	/**
	 * 医生日手术量详情统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> statDrInfoMonth(@Param("id") String id);
	
	/**
	 * 医生日手术量详情统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> statDrInfoYear(@Param("id") String id);
	
	/**
	 * 医生日手术量详情统计
	 * @param organs
	 * @return
	 */
	List<OperationCount> statDrInfoDate(@Param("id") String id, @Param("startTime") String startTime,
			@Param("endTime") String endTime);
	
	
	List<OperationCount> statAnestheticForDoc();
	
	int insertWscEvaluate(WebScEvaluate wse);
}
