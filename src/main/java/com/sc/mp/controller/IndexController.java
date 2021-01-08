package com.sc.mp.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.sc.mp.annotation.OperationLog;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.bean.PageResultBean;
import com.sc.mp.bean.ResultBean;
import com.sc.mp.config.DequeManager;
import com.sc.mp.config.SessionContext;
import com.sc.mp.model.WebScCalendar;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.model.WebScUser;
import com.sc.mp.service.UserService;
import com.sc.mp.util.ScConstant;

@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private UserService userService;
	
	@Autowired
	private SessionContext context;
	@Autowired
	private DequeManager manager;
	
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
	@Value("${sc.wx.login}")
	private String wxLogin;			// 医生角色登录后首页
	
	/**
	 * 登录页面
	 * @return
	 */
	@RequestMapping("/toLogin")
	public String goLogin() {
		return "login";
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, RedirectAttributes ra) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		
		if (context.getSession(session.getId()) != null) {
			context.delSession(session);
		}
		if (manager.getDeque(user.getLoginName()) != null
				&& manager.getDeque(user.getLoginName()).contains(session.getId())) {
			manager.delDeque(user.getLoginName());
		}
		
		session.invalidate();
//		return "redirect:/index";
		
//		ra.addFlashAttribute("wxUserid", user.getWxUserid());
//		ra.addFlashAttribute("openid", user.getWxOpenid());
		
		log.info("登出后重定向地址===");
//		return "redirect:/toLogin";
		return "redirect:" + wxLogin;
	}
	
	/**
	 * 提示
	 * @param request
	 * @return
	 */
	@RequestMapping("/tip")
	public String tip() {
		return "tip";
	}
	
	/**
	 * 企业微信绑定登录
	 */
	@GetMapping("/login")
	public String wxworkLogin(HttpServletRequest request, @RequestParam(value = "code") String code,
			@RequestParam(value = "state") String state, Model model) {
		HttpSession session = request.getSession();
		WebScUser wxUser = userService.getOpenid(code);	// 仅保存企业微信端的userid openid
		log.info("indexcontroller state=" + state);
		if (state.equals("gologin")) {
			request.getSession().invalidate();
			model.addAttribute("wxUserid", wxUser.getWxUserid());
			model.addAttribute("openid", wxUser.getWxOpenid());
			return "login";
		}
		
		
		WebScUser user = userService.getUserByOpenid(wxUser.getWxOpenid());
		if (user != null) {
			// 当前企业微信已绑定系统用户
			session.setAttribute(ScConstant.USER_SESSION_KEY, user);
			session.setAttribute("kickout", false);
			return "redirect:/index";
		} else {
			request.getSession().invalidate();
			model.addAttribute("wxUserid", wxUser.getWxUserid());
			model.addAttribute("openid", wxUser.getWxOpenid());
			return "login";
		}
//		return "login";
	}
	
	/**
	 * 登录校验
	 * @param request
	 * @param username
	 * @param password
	 * @param username
	 * @param password
	 * @param model
	 * @return
	 */
	@PostMapping(value="/login")
	public String login(HttpServletRequest request, @RequestParam("wxUserid") String wxUserid,
			@RequestParam("openid") String openid, @RequestParam("username") String username,
			@RequestParam("password") String password, Model model) {
		HttpSession session = request.getSession();
		// 登录校验
		WebScUser user = userService.selectByLoginInfo(username, password);
		if (user != null) {
			if (user.getRoleId().equals(superRoleId) || user.getRoleId().equals(regionalRoleId)
					|| user.getRoleId().equals(doctorRoleId)) {
				// userid openid与系统用户的解绑与重新绑定
				if (user.getWxOpenid() == null || !user.getWxOpenid().equals(openid)) {
					// openid是否绑定其他系统用户
					WebScUser userOfOpenid = userService.getUserByOpenid(openid);
					if (userOfOpenid != null) {
						// 将userid openid与其解绑
						userService.unbind(userOfOpenid);
					}
					// openid与当前用户绑定
					user.setWxUserid(wxUserid);
					user.setWxOpenid(openid);
					userService.bind(user);
				}
				session.setAttribute(ScConstant.USER_SESSION_KEY, user);
				session.setAttribute("kickout", false);
				return "redirect:/index";
			} else {
				session.invalidate();
				model.addAttribute("wxUserid", wxUserid);
				model.addAttribute("openid", openid);
				model.addAttribute("msg", ScConstant.NO_AUTH);
				return "login";
			}
		} else {
			session.invalidate();
			model.addAttribute("wxUserid", wxUserid);
			model.addAttribute("openid", openid);
			model.addAttribute("msg", ScConstant.USER_ERROR);
			return "login";
		}
	}
	
	/**
	 * 登录成功跳转
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		String retPage = loginPage;
		if (user.getRoleId().equals(superRoleId)) {
			retPage = superPage;
			// 统计年月日手术量
			Map<String, Integer> map = userService.statsYMD();
			model.addAttribute("statsYMD", map);
		} else if (user.getRoleId().equals(regionalRoleId)) {
			retPage = regionalPage;
		} else if (user.getRoleId().equals(doctorRoleId)) {
			retPage = doctorPage;
		} else {
			session.invalidate();
			model.addAttribute("msg", ScConstant.NO_AUTH);
		}
		model.addAttribute("user", user);
		return retPage;
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
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
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
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		List<WebScCalendar> calendars = userService.getCalendarsByDoctor(user);
		PageInfo<WebScCalendar> infos = new PageInfo<>(calendars);
		return new PageResultBean<>(infos.getTotal(), infos.getList());
	}
	
	@PostMapping(value="/addCalendar")
	@ResponseBody
	public ResultBean addCalendar(HttpServletRequest request) {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
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
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		if (userService.deleteCalendar(user, calendarId)) {
			return ResultBean.success("删除成功");
		} else {
			return ResultBean.error("删除失败");
		}
	}
	
	@RequestMapping("/statsMonths")
	@ResponseBody
	public ResultBean statsMonths() {
		List<OperationCount> monthCounts = userService.statsByYear();
		return ResultBean.success(monthCounts);
	}
	
	@RequestMapping("/statsCity")
	@ResponseBody
	public ResultBean statsCity() {
		List<OperationCount> cityCounts = userService.statsByCity();
		return ResultBean.success(cityCounts);
	}
	
	@RequestMapping("/statsWork")
	@ResponseBody
	public ResultBean statsWork() {
		List<OperationCount> workCounts = userService.statsWork();
		return ResultBean.success(workCounts);
	}
	
	@RequestMapping("/organizations")
	@ResponseBody
	public ResultBean getOrganizations(@RequestParam(value = "province") String province,
			@RequestParam(value = "city") String city,
			@RequestParam(value = "area") String area) {
		List<WebScOrganization> organizations = userService.getOrganizations(province, city, area);
		return ResultBean.success(organizations);
	}
	
	@PostMapping("/reporting")
	@ResponseBody
	public ResultBean getReporting(@RequestBody String data) {
		JSONObject jsonData = JSONObject.parseObject(data);
		List<OperationCount> operationCounts = userService.getReporting(jsonData);
		return ResultBean.success(operationCounts);
	}
	
	@OperationLog("更新用户Lucene文件")
	@PostMapping("/updateUserIndex")
	@ResponseBody
	public ResultBean updateUserIndex() {
		userService.createDoctorAndNurseIndex();
		return ResultBean.success();
	}
	
	@OperationLog("搜索用户名称列表")
    @GetMapping(value = "/searchUser")
    @ResponseBody
	public String searchOperativeNames(@RequestParam(value = "query", required = true) String name,
			@RequestParam(value = "limit", required = true) Integer limit) {
		return userService.searchIndex(name, limit);
	}
	
}
