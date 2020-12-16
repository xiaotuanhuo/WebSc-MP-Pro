package com.sc.mp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sc.mp.model.WebScAnesthetic;
import com.sc.mp.model.WebScOperative;
import com.sc.mp.service.AnestheticService;
import com.sc.mp.service.OperativeService;
import com.sc.mp.util.RedisUtil;

@Controller
@RequestMapping("/data")
public class DataController {
	private static final Logger log = LoggerFactory.getLogger(DataController.class);
	
	@Resource
	private OperativeService operativeService;
	
	@Resource
	private AnestheticService anestheticService;
	
	@Resource
	private RedisUtil rs;
	
	/**
	 * 获取手术名称清单
	 */
	public List<WebScOperative> getWebScOperativeList(){
		//从redis里获取
		log.info("Redis:获取手术名称列表");
		@SuppressWarnings("unchecked")
//		List<WebScOperative> operativeNamels = (List<WebScOperative>) rs.get("WSC_OPERATIVE");
		List<WebScOperative> operativeNamels = null;
		
		if(operativeNamels == null){
			log.info("Redis获取失败:从数据读取手术名称列表");
			operativeNamels = operativeService.getWebScOperativeList(null);
//			rs.set("WSC_OPERATIVE", operativeNamels, 86400);
		}
		return operativeNamels;
	}
	
	/**
	 * 获取麻醉方法清单
	 */
	public List<WebScAnesthetic> getWebScAnestheticList(){
		//从redis里获取
		log.info("Redis:获取麻醉方法列表");
		@SuppressWarnings("unchecked")
//		List<WebScAnesthetic> anestheticls = (List<WebScAnesthetic>) rs.get("WSC_ANESTHETIC");
		List<WebScAnesthetic> anestheticls = null;
		
		if(anestheticls == null){
			log.info("Redis获取失败:从数据读取麻醉方法列表");
			anestheticls = anestheticService.getWebScAnestheticList(null);
//			rs.set("WSC_ANESTHETIC", anestheticls, 86400);
		}
		return anestheticls;
	}
}
