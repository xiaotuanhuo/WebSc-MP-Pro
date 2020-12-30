package com.sc.mp.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.mapper.DeptMapper;
import com.sc.mp.mapper.DocMapper;
import com.sc.mp.mapper.OrganizationMapper;
import com.sc.mp.mapper.RoleMapper;
import com.sc.mp.mapper.ScCalendarMapper;
import com.sc.mp.mapper.UserMapper;
import com.sc.mp.model.WebScRole;
import com.sc.mp.model.WebScCalendar;
import com.sc.mp.model.WebScDept;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.model.WebScUser;
import com.sc.mp.util.ScConstant;
import com.sc.mp.util.DistrictUtil;
import com.sc.mp.util.UUID19;
import com.sc.mp.util.WxUtil;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Resource
	private UserMapper userMapper;
	
	@Resource
	private RoleMapper roleMapper;
	
	@Resource
	private DeptMapper deptMapper;
	
	@Resource
	private ScCalendarMapper scCalendarMapper;
	
	@Resource
	private DocMapper docMapper;
	
	@Resource
	private OrganizationMapper organizationMapper;
	
	@Resource
	private WxUtil wxUtil;
	
	@Value("${sc.stats.top}")
	private int top;			// 超级管理员角色id
	
	public WebScUser selectUserInfo(WebScUser user) {
		return userMapper.selectUserInfo(user);
	}
	
	public WebScUser selectByLoginInfo(String name, String password) {
		WebScUser user = userMapper.selectByLoginName(name);
		if (user == null) {
			log.info(ScConstant.USERNAME_ERROR + name);
			return null;
		}
		String loginPwd = user.getLoginPwd();
		String salt = user.getSalt();
		String cryptoPassword = new Md5Hash(password, salt).toString();
		if (!loginPwd.equals(cryptoPassword)) {
			log.info(ScConstant.PASSWORD_ERROR + password);
			return null;
		}
		if (user.getArea() != null) {
			user.setDistrictName(DistrictUtil.getDistrictByCode(user.getProvince()).getName() + "-"
					+ DistrictUtil.getDistrictByCode(user.getCity()).getName() + "-"
					+ DistrictUtil.getDistrictByCode(user.getArea()).getName());
		} else if (user.getCity() != null) {
			user.setDistrictName(DistrictUtil.getDistrictByCode(user.getProvince()).getName() + "-"
					+ DistrictUtil.getDistrictByCode(user.getCity()).getName());
		} else if (user.getProvince() != null) {
			user.setDistrictName(DistrictUtil.getDistrictByCode(user.getProvince()).getName());
		}
		WebScRole role = roleMapper.selectByPrimaryKey(Integer.parseInt(user.getRoleId()));
		user.setRoleName(role.getRoleName());
		WebScDept dept = deptMapper.selectByPrimaryKey(user.getRoleTypeId());
		user.setDeptName(dept.getDeptName());
		return user;
	}
	
	/**
	 * 区域备休信息查询
	 * @param page
	 * @param rows
	 * @param date
	 * @param user
	 * @return
	 */
	public List<WebScCalendar> getCalendars(int page, int rows, String date, WebScUser user) {
		PageHelper.startPage(page, rows);
		if (date.isEmpty()) {
			// 默认当天
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			date = format.format(new Date());
		}
//		if (user == null) {
//			log.info("session已过期");
//			return null;
//		}
		return scCalendarMapper.selectCalendarsByDate(date, user);
	}
	
	/**
	 * 医生备休信息查询
	 * 查询当天及以后
	 * @return
	 */
	public List<WebScCalendar> getCalendarsByDoctor(WebScUser user) {
		if (user == null) {
			// TODO
		}
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String date = format.format(new Date());
		List<WebScCalendar> calendars = scCalendarMapper.selectByDoctor(user.getUserId());
		return calendars;
	}
	
	/**
	 * 新增备休信息
	 * @param calendarInfo
	 * @param doctorId
	 * @return
	 */
	public WebScCalendar addCalendar(JSONObject calendarInfo, Integer doctorId) {
		WebScCalendar calendar = new WebScCalendar();
		String id = UUID19.uuid();
		String day = calendarInfo.getString("day");			// 备休日
		String begin = calendarInfo.getString("begin");		// 开始时间
		String end = calendarInfo.getString("end");			// 结束时间
		String timeBtn = calendarInfo.getString("timeBtn");	// 上午/下午/全天
		String title = null;	// 标题
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startTime = format.parse(day + " " + begin + ":00");
			Date endTime = format.parse(day + " " + end + ":00");
			calendar.setStartTime(startTime);
			calendar.setEndTime(endTime);
		} catch (ParseException e) {
			log.error("时间转换出错：" + (day + " " + begin + ":00") + " " + (day + " " + end + ":00"));
		}
		if (timeBtn.equals(ScConstant.AM)) {
			title = ScConstant.AM_TEXT;
		} else if (timeBtn.equals(ScConstant.PM)) {
			title = ScConstant.PM_TEXT;
		} else if (timeBtn.equals(ScConstant.ALL)) {
			title = ScConstant.ALL_TEXT;
		} else {
			title = ScConstant.CAL_PREFIX + begin + ":00" + " - " + end + ":00";
		}
		calendar.setCalendarId(id);
		calendar.setUserId(doctorId);
		calendar.setTitle(title);
		
		if (scCalendarMapper.insert(calendar) == 1) {
			return calendar;
		} else {
			return null;
		}
	}
	
	/**
	 * 备休信息删除
	 */
	public boolean deleteCalendar(WebScUser user, String calendarId) {
		if (user == null) {
			// TODO
		}
		return scCalendarMapper.deleteByPrimaryKey(calendarId) == 1;
	}
	
	/**
	 * 年月日手术量统计
	 */
	public Map<String, Integer> statsYMD() {
		Map<String, Integer> statsMap = new HashMap<String, Integer>();
		int daySum = docMapper.statsDay();
		int monthSum = docMapper.statsMonth();
		int yearSum = docMapper.statsYear();
		statsMap.put("day", daySum);
		statsMap.put("month", monthSum);
		statsMap.put("year", yearSum);
		return statsMap;
	}
	
	public List<OperationCount> statsByYear() {
		Calendar cal = Calendar.getInstance();
		int thisYear = cal.get(Calendar.YEAR);	// 今年
		int lastYear = thisYear - 1;	// 去年
		int beforeLastYear = thisYear - 2;	// 前年
		List<OperationCount> monthCounts = docMapper.statsByYear(thisYear, lastYear, beforeLastYear);
		return monthCounts;
	}
	
	public List<OperationCount> statsByCity() {
		List<OperationCount> operationCounts = docMapper.statsByCity(top);
		for (int i = 0; i < operationCounts.size(); i++) {
			if (operationCounts.get(i).getCityCode().equals(ScConstant.OTHER_PROVINCE)) {
				if (operationCounts.get(i).getCount() == 0) {
					operationCounts.remove(operationCounts.get(i));
				} else {
					operationCounts.get(i).setCityName(ScConstant.OTHER_PROVINCE_TEXT);
				}
			} else {
				switch (operationCounts.get(i).getCityCode()) {
					// code是直辖市时设置城市名
					case ScConstant.BJS_CODE:
						operationCounts.get(i).setCityName(ScConstant.BJS_NANE);
						break;
					case ScConstant.TJS_CODE:
						operationCounts.get(i).setCityName(ScConstant.TJS_NANE);
						break;
					case ScConstant.SHS_CODE:
						operationCounts.get(i).setCityName(ScConstant.SHS_NANE);
						break;
					case ScConstant.CQS_CODE:
						operationCounts.get(i).setCityName(ScConstant.CQS_NANE);
						break;
					default:
						operationCounts.get(i).setCityName(
							DistrictUtil.getDistrictByCode(operationCounts.get(i).getCityCode()).getName());
						break;
				}
			}
		}
		return operationCounts;
	}
	
	/**
	 * 统计医生全职兼职情况
	 * @return
	 */
	public List<OperationCount> statsWork() {
		return userMapper.statsDoctorWork();
	}
	
	public List<WebScOrganization> getOrganizations(String province, String city, String area) {
		return organizationMapper.selectOrganizations(province.equals("") ? null : province,
				city.equals("") ? null : city, area.equals("") ? null : area);
	}
	
	public List<OperationCount> getReporting(JSONObject data) {
		Map<String, String> map = new HashMap<>();
		map.put("beginTime", data.getString("beginTime"));
		map.put("endTime", data.getString("endTime"));
		map.put("province", data.getString("province"));
		map.put("city", data.getString("city"));
		map.put("area", data.getString("area"));
		map.put("orgId", data.getString("orgId"));
		List<OperationCount> operationCounts = docMapper.selectReporting(map);
		// 组织完整行政区划名称
		for (OperationCount oc : operationCounts) {
			oc.setWholeDistrict(DistrictUtil.getDistrictByCode(oc.getProvince()).getName() + "-"
					+ DistrictUtil.getDistrictByCode(oc.getCity()).getName() + "-"
					+ DistrictUtil.getDistrictByCode(oc.getArea()).getName());
		}
		return operationCounts;
	}
	
	/**
	 * 获取企业微信用户openid userid
	 * @param code
	 * @return
	 */
	public WebScUser getOpenid(String code) {
		WebScUser user = wxUtil.getWxUserOpenid(code, null);
		
//		// 模拟从企业微信获取userid openid
//		WebScUser user = new WebScUser();
//		user.setWxUserid("111");
//		user.setWxOpenid("222");
		
//		// 获取openid失败的处理
//		if (user.getWxOpenid() == null) {
//			// TODO
//		}
		return user;
	}
	
	/**
	 * 根据openId获取用户
	 * @param openid
	 * @return
	 */
	public WebScUser getUserByOpenid(String openid) {
		return userMapper.selectUserByOpenid(openid);
	}
	
	public boolean unbind(WebScUser user) {
		return userMapper.unbindOpenid(user.getUserId()) == 1;
	}
	
	public boolean bind(WebScUser user) {
		return userMapper.bundOpenid(user) == 1;
	}
}