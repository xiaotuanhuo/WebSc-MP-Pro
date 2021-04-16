package com.sc.mp;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sc.mp.bean.KeyValue;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.mapper.AnestheticMapper;
import com.sc.mp.mapper.DocMapper;
import com.sc.mp.mapper.OperativeMapper;
import com.sc.mp.model.WebScAnesthetic;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScOperative;
import com.sc.mp.util.LuceneUtil;
import com.sc.mp.util.UnixtimeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.sc.mp.mapper")
public class MpApplicationTest {
	
	@Autowired
	DocMapper docMapper;
	@Autowired
	OperativeMapper operativeMapper;
	@Autowired
	AnestheticMapper anestheticMapper;
	
	@Value("${operativeName-lucene-path}")
	private String operativeNameLucenePath;

	@Test
	public void test() {
		List<WebScAnesthetic> anesthetics = anestheticMapper.getWebScAnesthetics();
		System.out.println(anesthetics);
		
//		WebScDoc doc = docMapper.selectByPrimaryKey("00mF0PNJAUrihwTXWEC");
//		System.out.println(doc);
		
//		List<OperationCount> ssEs = docMapper.statsByMonthForOrgan("r5OPGFOBjTt0zhebLoP", "");
//		System.out.println(ssEs);
	}

}
