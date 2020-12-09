package com.sc.mp.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sc.mp.bean.WebScDoc;
import com.sc.mp.mapper.DocMapper;


@Service
public class DocService {
	@Resource
	private DocMapper docMapper;
	
    public List<WebScDoc> selectWebScDocList(WebScDoc doc){
    	return docMapper.selectWebScDocList(doc);
    }
}
