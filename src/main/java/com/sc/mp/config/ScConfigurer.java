package com.sc.mp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sc.mp.interceptor.LoginInterceptor;

@Configuration
public class ScConfigurer implements WebMvcConfigurer {

	// 将登录拦截器配置到容器中
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/", "/login",
				"/logout", "/css/**", "/js/**", "/img/**", "/fonts/**", "/data/**", "/xcx/**");
	}
}
