package com.sc.mp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value="mp")
public class IndexController {
	
	@GetMapping(value="/area")
	public String index() {
		return "super";
	}
	
	@GetMapping(value="/mescroll")
	@ResponseBody
	public JSONObject mescroll() {
		String details = "{\r\n" + 
				"    \"detail\":[\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生1\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生2\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生3\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生4\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生5\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生6\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生7\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生8\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生9\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"name\":\"医生10\",\r\n" + 
				"            \"phone\":\"13888888888\",\r\n" + 
				"            \"org\":\"上海市徐汇区医生集团\",\r\n" + 
				"            \"date\":\"2020-11-18\",\r\n" + 
				"            \"time\":\"上午\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
//		System.out.println(details);
		return JSONObject.parseObject(details);
	}
}
