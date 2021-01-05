package com.sc.mp.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sc.mp.mapper.AccessTokenMapper;
import com.sc.mp.model.WebScAccessToken;

@Service
public class AccessTokenService {
	
	@Resource
	private AccessTokenMapper accessTokenMapper;
	
	public WebScAccessToken getWebScAccessToken(){
		return accessTokenMapper.getWebScAccessToken();
	}
	
	public void updateWebScAccessToken(String accesstoken){
		accessTokenMapper.deleteWebScAccessToken();
		accessTokenMapper.updateWebScAccessToken(accesstoken);
	}
}
