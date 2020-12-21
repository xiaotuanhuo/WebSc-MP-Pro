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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.mapper.DeptMapper;
import com.sc.mp.mapper.DocMapper;
import com.sc.mp.mapper.RoleMapper;
import com.sc.mp.mapper.ScCalendarMapper;
import com.sc.mp.mapper.UserMapper;
import com.sc.mp.model.WebScRole;
import com.sc.mp.model.WebScCalendar;
import com.sc.mp.model.WebScDept;
import com.sc.mp.model.WebScUser;
import com.sc.mp.util.ScConstant;
import com.sc.mp.util.DistrictUtil;
import com.sc.mp.util.UUID19;

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
	
	public WebScUser selectUserInfo(WebScUser user) {
		return userMapper.selectUserInfo(user);
	}
	
	public WebScUser selectByLoginInfo(String name, String password) {
		WebScUser user = userMapper.selectByLoginName(name);
		if (user == null) {
			log.info("用户名不存在:" + name);
			return null;
		}
//		String pwd = user.getLoginPwd();
//		String salt = user.getSalt();
//		String cryptoPassword = new Md5Hash(password, salt).toString();
//		if (!pwd.equals(cryptoPassword)) {
//			log.info("密码不正确:" + password);
//			return null;
//		}
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
}