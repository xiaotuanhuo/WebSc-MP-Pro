package com.sc.mp.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sc.mp.mapper.AnestheticMapper;
import com.sc.mp.model.WebScAnesthetic;

@Service
public class AnestheticService {
	@Resource
    private AnestheticMapper anestheticMapper;
	
	public List<WebScAnesthetic> getWebScAnestheticList(String anestheticName){
		return anestheticMapper.getWebScAnestheticList(anestheticName);
	}
}
