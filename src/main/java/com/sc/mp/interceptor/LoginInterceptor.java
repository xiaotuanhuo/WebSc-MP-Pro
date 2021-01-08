package com.sc.mp.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.mp.bean.ResultBean;
import com.sc.mp.config.DequeManager;
import com.sc.mp.config.SessionContext;
import com.sc.mp.model.WebScUser;
import com.sc.mp.util.ScConstant;
import com.sc.mp.util.WebHelper;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	private int maxSession = 1; 			// 同一个帐号最大会话数 默认1
	private boolean kickoutBefore = true;	// 踢出之前/之后登录的用户  默认true 踢出之前登录的用户
	
	@Autowired
	private SessionContext context;
	@Autowired
	private DequeManager manager;
	
	@Value("${sc.wx.login}")
	private String wxLogin;			// 医生角色登录后首页
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		String ua = request.getHeader("user-agent");
//		if (!(ua.indexOf("MicroMessenger") != -1 && ua.indexOf("wxwork") != -1)) {
//			// 仅允许使用企业微信客户端
//			response.sendRedirect("/tip");
//			return false;
//		}
		HttpSession session = request.getSession();
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		
//		String agent=request.getHeader("User-Agent").toLowerCase();
//		if (agent.indexOf("firefox") != -1) {
//			code = "firefox";
//		} else {
//			code = "chrome";
//		}
		
		log.info("code=" + code);
		log.info("state=" + state);
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
		if (user == null) {
			log.info("user is null");
			if (state != null) {
				response.sendRedirect("/login?code=" + code + "&state=" + state);
			} else {
				response.sendRedirect("/login?code=" + code + "&state=no");
			}
			return false;
		}
		
		String loginName = user.getLoginName();
		log.info("===当前用户loginName：==" + loginName);
		String sessionId = session.getId();
		log.info("===当前用户sessionId：==" + sessionId);
		// 读取缓存用户
		Deque<String> deque = manager.getDeque(loginName);
		if (deque == null) {
			return true;
		}
		log.info("===当前deque：==" + deque);
		// 如果被踢出了，(前者或后者)直接退出，重定向到踢出后的地址
		if ((Boolean) session.getAttribute("kickout") != null
				&& (Boolean) session.getAttribute("kickout") == true) {
//			// 会话被踢出了
//			session.invalidate();	// session销毁
//			log.info("==踢出后用户重定向的路径kickoutUrl:");
//			response.sendRedirect("/login?code=" + code);
//			return false;
			
			// 会话被踢出了
			log.info("==用户被踢出==");
			session.invalidate();	// session销毁
			return isAjaxResponse(request, response, code);
		}
		if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
			// 当前账号新会话
			log.info("==跳转登陆页面:");
//			response.sendRedirect("/login?code=" + code);
			response.sendRedirect(wxLogin);
			return false;
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		WebScUser user = (WebScUser) request.getSession().getAttribute(ScConstant.USER_SESSION_KEY);
		System.out.println("afterCompletion----" + user.getUserId() + " ::: " + request.getRequestURL());
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();
		WebScUser user = (WebScUser) session.getAttribute(ScConstant.USER_SESSION_KEY);
//		System.out.println("postHandle----" + user.getUserId() + " ::: " + request.getRequestURL());
		
		String loginName = user.getLoginName();
		log.info("===当前用户username：==" + loginName);
		String sessionId = session.getId();
		log.info("===当前用户sessionId：==" + sessionId);
		// 读取缓存用户 没有就存入
		Deque<String> deque = manager.getDeque(loginName);
		log.info("===当前deque：==" + deque);
		if (deque == null) {
			// 初始化队列
			deque = new ArrayDeque<String>();
		}
		// 如果队列里没有此sessionId且用户没有被踢出 放入队列
		if (!deque.contains(sessionId) && (Boolean) session.getAttribute("kickout") != null
				&& (Boolean) session.getAttribute("kickout") == false) {
			// 将sessionId存入队列
			deque.push(sessionId);
			// 将用户的sessionId队列缓存
			manager.addDeque(loginName, deque);
			// 将session放入session管理器
			context.addSession(session);
		}
		// 如果队列里的sessionId数超出最大会话数，开始踢人
		while (deque.size() > maxSession) {
			log.info("===deque队列长度：==" + deque.size());
			String kickoutSessionId = null;
			// 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
			if (kickoutBefore) {	// 踢出前者
				kickoutSessionId = deque.removeLast();
			} else {	// 踢出后者
				kickoutSessionId = deque.removeFirst();
			}
			// 踢出后再更新下缓存队列
			manager.addDeque(loginName, deque);
			// 获取被踢出的sessionId的session对象
			HttpSession kickoutSession = context.getSession(kickoutSessionId);
			if (kickoutSession != null) {
				// 设置会话的kickout属性表示踢出了
				kickoutSession.setAttribute("kickout", true);
				// 从session管理器中移除
				context.delSession(kickoutSession);
			}
		}
	}
	
	private boolean isAjaxResponse(HttpServletRequest request, HttpServletResponse response, String code)
			throws IOException {
		// 判断是否已经踢出 1.如果是Ajax 访问，那么给予json返回值提示。 2.如果是普通请求，直接跳转到登录页
		ResultBean resultBean = ResultBean.success();
		if (WebHelper.isAjaxRequest(request)) {
			log.info(getClass().getName() + "当前用户已经在其他地方登录，并且是Ajax请求！");
			resultBean.setCode(ScConstant.MANY_LOGIN_CODE);
			resultBean.setMsg(ScConstant.MANY_LOGIN_MSG);
			resultBean.setData(wxLogin);
			out(response, resultBean);
		} else {
			// 重定向
//			response.sendRedirect("/login?code=" + code);
			response.sendRedirect(wxLogin);
		}
		return false;
	}
	
	/**
	 * @描述 response输出json
	 * @param response
	 * @param result
	 */
	public static void out(HttpServletResponse response, ResultBean result) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");			// 设置编码
			response.setContentType("application/json");	// 设置返回类型
			out = response.getWriter();
			out.println(objectMapper.writeValueAsString(result));	// 输出
			log.error("用户在线数量限制响应json信息成功");
		} catch (Exception e) {
			log.error("用户在线数量限制响应json信息出错", e);
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
	}
}
