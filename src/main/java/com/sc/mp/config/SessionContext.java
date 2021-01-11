package com.sc.mp.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * session管理器
 * @author aisino
 *
 */
public class SessionContext {
	private static SessionContext context;
	private Map<String, HttpSession> map;
	
	private SessionContext() {
		map = new HashMap<>();
	}
	
	public static SessionContext getSessionContext() {
		if (context == null) {
			context = new SessionContext();
		}
		return context;
	}
	
	// 添加
	public synchronized void addSession(HttpSession session) {
		if (session != null) {
			map.put(session.getId(), session);
		}
	}
	
	// 获取
	public synchronized HttpSession getSession(String sessionId) {
		if (sessionId == null) {
			return null;
		}
		return map.get(sessionId);
	}
	
	// 删除
	public synchronized void delSession(HttpSession session) {
		if (session != null) {
			map.remove(session.getId());
		}
	}
	
	// 获取map的个数
	public synchronized String getSize() {
		int size = map.size();
		return String.valueOf(size);
	}

}
