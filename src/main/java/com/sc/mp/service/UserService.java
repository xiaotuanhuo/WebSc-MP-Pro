package com.sc.mp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sc.mp.mapper.DeptMapper;
import com.sc.mp.mapper.RoleMapper;
import com.sc.mp.mapper.ScCalendarMapper;
import com.sc.mp.mapper.UserMapper;
import com.sc.mp.model.WebScRole;
import com.sc.mp.model.WebScCalendar;
import com.sc.mp.model.WebScDept;
import com.sc.mp.model.WebScUser;
import com.sc.mp.util.DistrictUtil;

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
	
	public String test() {
		log.info("test......");
		WebScUser user = userMapper.selectByPrimaryKey(1);
		return user.getLoginName();
	}
	
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
		if (user == null) {
			log.info("session已过期");
			return null;
		}
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
	 * 备休信息删除
	 */
	public boolean deleteCalendar(WebScUser user, String calendarId) {
		if (user == null) {
			// TODO
		}
		return scCalendarMapper.deleteByPrimaryKey(calendarId) == 1;
	}
}