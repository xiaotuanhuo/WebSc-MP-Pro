package com.sc.mp.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sc.mp.mapper.DocMapper;
import com.sc.mp.model.WebScDoc;


@Service
public class DocService {
	@Resource
	private DocMapper docMapper;
	
    public List<WebScDoc> selectWebScDocList(WebScDoc doc){
    	return docMapper.selectWebScDocList(doc);
    }
    
    public WebScDoc selectWebScDoc(WebScDoc doc){
    	List<WebScDoc> list = docMapper.selectWebScDocList(doc);
    	if(list != null && list.size() > 0)
    		return list.get(0);
    	else
    		return null;
    }
}
