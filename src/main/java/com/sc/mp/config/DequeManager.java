package com.sc.mp.config;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户会话队列管理器
 * @author aisino
 *
 */
public class DequeManager {
	private static DequeManager manager;
	private Map<String, Deque<String>> map;
	
	private DequeManager() {
		map = new HashMap<String, Deque<String>>();
	}
	
	public static DequeManager getDequeManager() {
		if (manager == null) {
			manager = new DequeManager();
		}
		return manager;
	}
	
	// 添加
	public synchronized void addDeque(String name, Deque<String> deque) {
		if (deque != null) {
			map.put(name, deque);
		}
	}
	
	// 获取
	public synchronized Deque<String> getDeque(String name) {
		if (name == null) {
			return null;
		}
		return map.get(name);
	}
	
	// 删除
	public synchronized void delDeque(String key) {
		if (key != null) {
			map.remove(key);
		}
	}
}
