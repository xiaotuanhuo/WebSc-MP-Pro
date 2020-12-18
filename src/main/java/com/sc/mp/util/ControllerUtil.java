package com.sc.mp.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.sc.mp.model.WebScUser;

@Component
public class ControllerUtil {

	/**
	 * 获取用户信息
	 * @param request
	 * @return
	 */
	public static WebScUser getUserInfo(HttpServletRequest request){
		HttpSession session = request.getSession();  
		//根据Cookies获取sessionId
		Cookie cookie = ControllerUtil.getCookieByName(request, "WEBSCMP_JSESSIONID");
		String sessionId = cookie.getValue();
		//获取用户信息
		return (WebScUser) session.getAttribute(sessionId);
	}
	
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }   
	}
	
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	public static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
}
