package com.sc.mp.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sc.mp.mapper.OperativeMapper;
import com.sc.mp.model.WebScOperative;

@Service
public class OperativeService {
	@Resource
    private OperativeMapper operativeMapper;
	
	public List<WebScOperative> getWebScOperativeList(String operativeName){
		return operativeMapper.getWebScOperativeList(operativeName);
	}
}
