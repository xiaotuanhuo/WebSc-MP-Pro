package com.sc.mp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sc.mp.model.WebScUser;
import com.sc.mp.util.ScConstant;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ua = request.getHeader("user-agent");
		if (!(ua.indexOf("MicroMessenger") != -1 && ua.indexOf("wxwork") != -1)) {
			// 仅允许使用企业微信客户端
			response.sendRedirect("/tip");
			return false;
		}
		WebScUser user = (WebScUser) request.getSession().getAttribute(ScConstant.USER_SESSION_KEY);
		if (user == null) {
//			request.setAttribute("msg", "无权限请先登录");
//			// 获取request返回页面到登录页
//			request.getRequestDispatcher("/index.html").forward(request, response);
			
//			response.sendRedirect("/login");
			
			String code = request.getParameter("code");
			log.info("code=" + code);
			response.sendRedirect("/login?code=" + code);
//			response.sendRedirect("/login?code=21122");
			return false;
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		WebScUser user = (WebScUser) request.getSession().getAttribute(ScConstant.USER_SESSION_KEY);
//		System.out.println("afterCompletion----" + user.getUserId() + " ::: " + request.getRequestURL());
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
//		WebScUser user = (WebScUser) request.getSession().getAttribute(ScConstant.USER_SESSION_KEY);
//		System.out.println("postHandle----" + user.getUserId() + " ::: " + request.getRequestURL());
	}
}
