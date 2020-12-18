package com.sc.mp.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.sc.mp.bean.PageResultBean;
import com.sc.mp.bean.ResultBean;
import com.sc.mp.model.WebScCalendar;
import com.sc.mp.model.WebScUser;
import com.sc.mp.service.UserService;
import com.sc.mp.util.ControllerUtil;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private UserService userService;
	
	@Value("${sc.role.super}")
	private String superRoleId;			// 超级管理员角色id
	@Value("${sc.role.regional}")
	private String regionalRoleId;		// 区域管理员角色id
	@Value("${sc.role.doctor}")
	private String doctorRoleId;		// 医生角色id
	@Value("${sc.page.login}")
	private String loginPage;			// 超级管理员角色登录后首页
	@Value("${sc.page.super}")
	private String superPage;			// 超级管理员角色登录后首页
	@Value("${sc.page.regional}")
	private String regionalPage;		// 区域管理员角色登录后首页
	@Value("${sc.page.doctor}")
	private String doctorPage;			// 医生角色登录后首页
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:/index";
	}
	
	/**
	 * 登录界面跳转
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		Cookie cookie = ControllerUtil.getCookieByName(request, ControllerUtil.USER_SESSION_KEY);
		WebScUser user = null;
		String sessionId;
		String retPage = loginPage;
		
		if (cookie == null) {
			log.info("toLogin___noCookie");
			// 获取企业微信绑定用户信息
			// TODO
			sessionId = session.getId();
		} else {
			sessionId = cookie.getValue();
			log.info("toLogin___cookie__cookie=" + sessionId);
			if (sessionId != null) {
				user = (WebScUser) session.getAttribute(sessionId);
			}
		}
		if (user != null && user.getUserId() != null) {
			if (user.getRoleId().equals(superRoleId)) {
				retPage = superPage;
			} else if (user.getRoleId().equals(regionalRoleId)) {
				retPage = regionalPage;
			} else if (user.getRoleId().equals(doctorRoleId)) {
				retPage = doctorPage;
			} else {
				retPage = "403";
			}
			model.addAttribute("user", user);
		} else {
			cookie = new Cookie("WEBSCMP_JSESSIONID", session.getId());
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(60 * 60 * 24 * 30 * 24);
			response.addCookie(cookie);
		}
		return retPage;
	}
	
	@PostMapping(value="/login")
	@ResponseBody
	public ResultBean login(HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		String infoStr = request.getParameter("loginInfo");
		JSONObject loginInfo = JSONObject.parseObject(infoStr);
		// 登录校验
		WebScUser user = userService.selectByLoginInfo(loginInfo.getString("loginName"), loginInfo.getString("password"));
		if (user != null) {
			Cookie cookie = ControllerUtil.getCookieByName(request, ControllerUtil.USER_SESSION_KEY);
			String sessionId = cookie.getValue();
			log.info("login()___cookieId=" + sessionId);
			session.setAttribute(sessionId, user);
			return ResultBean.success("登录成功");
		} else {
			return ResultBean.error("用户名或者密码错误");
		}
	}
	
	/**
	 * 备休列表查询
	 * @param request
	 * @param page
	 * @param limit
	 * @param date
	 * @return
	 */
	@GetMapping("/calendarScroll")
	@ResponseBody
	public PageResultBean<WebScCalendar> getCalendarList(HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "date", required = false) String date) {
		HttpSession session = request.getSession();
		log.info("getList()___sessionId=" + session.getId());
		WebScUser user = (WebScUser) session.getAttribute(session.getId());
		List<WebScCalendar> calendars = userService.getCalendars(page, limit, date, user);
		PageInfo<WebScCalendar> infos = new PageInfo<>(calendars);
		return new PageResultBean<>(infos.getTotal(), infos.getList());
	}
	
	/**
	 * 我（医生）的备休信息查询
	 */
	@GetMapping("/calendars")
	@ResponseBody
	public PageResultBean<WebScCalendar> getMyCalendars(HttpServletRequest request) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(session.getId());
		List<WebScCalendar> calendars = userService.getCalendarsByDoctor(user);
		PageInfo<WebScCalendar> infos = new PageInfo<>(calendars);
		return new PageResultBean<>(infos.getTotal(), infos.getList());
	}
	
	@PostMapping(value="/addCalendar")
	@ResponseBody
	public ResultBean addCalendar(HttpServletRequest request) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(session.getId());
		String infoStr = request.getParameter("calendarInfo");
		JSONObject calendarInfo = JSONObject.parseObject(infoStr);
		WebScCalendar calendar = userService.addCalendar(calendarInfo, user.getUserId());
		if (calendar == null) {
			return ResultBean.error("添加备休失败");
		}
		return ResultBean.success(calendar); 
	}
	
	@DeleteMapping("/calendar/del/{id}")
	@ResponseBody
	public ResultBean delCalendar(HttpServletRequest request, @PathVariable("id") String calendarId) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(session.getId());
		if (userService.deleteCalendar(user, calendarId)) {
			return ResultBean.success("删除成功");
		} else {
			return ResultBean.error("删除失败");
		}
	}
}
