package com.sc.mp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sc.mp.interceptor.LoginInterceptor;

@Configuration
public class ScConfigurer implements WebMvcConfigurer {
	
	@Bean(value="context")
	public SessionContext getContext() {
		return SessionContext.getSessionContext();
	}
	
	@Bean(value="manager")
	public DequeManager getManager() {
		return DequeManager.getDequeManager();
	}
	
	@Autowired
	private LoginInterceptor loginInterceptor;
	
	// 将登录拦截器配置到容器中
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/toLogin",
				"/logout", "/tip", "/WW_verify_TrbNaicGXoiHwdkl.txt", "/css/**", "/js/**", "/img/**", "/fonts/**",
				"/data/**", "/xcx/**", "/updateUserIndex");
	}
}
