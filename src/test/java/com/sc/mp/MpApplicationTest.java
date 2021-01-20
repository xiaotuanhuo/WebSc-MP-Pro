package com.sc.mp;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sc.mp.mapper.DocMapper;
import com.sc.mp.model.WebScDoc;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.sc.mp.mapper")
public class MpApplicationTest {
	
	@Autowired
	DocMapper docMapper;

	@Test
	public void test() {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("orgId", "SVyxuf3ivyUWAUzjqLR");
		paraMap.put("state", "5");
		paraMap.put("limit", 10);
		List<WebScDoc> docs = docMapper.selectWebScDocTmps(paraMap);
		System.out.println(docs);
		
//		Date date = new Date();
//		int month = CalendarUtil.getMonth(date);
//		int curMonthCount = docMapper.selectOperativeCount(
//				"SVyxuf3ivyUWAUzjqLR",
//				CalendarUtil.getYear(date)+"-"+(month<10?("0"+month):month)+"-01", 
//				CalendarUtil.getYear(date)+"-"+(month<10?("0"+month):month)+"-32");
//		System.out.println("curMonthCount: "+curMonthCount);
	}

}
