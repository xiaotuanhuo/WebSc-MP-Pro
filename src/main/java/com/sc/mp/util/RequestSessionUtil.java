package com.sc.mp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestSessionUtil {
	
	/**
	 * 获取request
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = 
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 获取session
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession(false);
		return session;
	}
	
	/**
	 * 获取当前用户
	 * @return
	 */
	public static Object getCurUser() {
		HttpSession session = getSession();
		if (session == null) {
			return null;
		}else {
			return session.getAttribute(ScConstant.USER_SESSION_KEY);
		}
	}
}
