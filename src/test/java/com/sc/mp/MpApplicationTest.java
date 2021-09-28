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
import com.sc.mp.mapper.OrganizationMapper;
import com.sc.mp.mapper.WebScDocOperativeMapper;
import com.sc.mp.mapper.WebScLabelMapper;
import com.sc.mp.model.WebScAnesthetic;
import com.sc.mp.model.WebScDoc;
import com.sc.mp.model.WebScDocOperative;
import com.sc.mp.model.WebScLabel;
import com.sc.mp.model.WebScOperative;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.util.LuceneUtil;
import com.sc.mp.util.StringUtil;
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
	@Autowired
	WebScLabelMapper webScLabelMapper;
	@Autowired
	WebScDocOperativeMapper webScDocOperativeMapper;
	@Autowired
	OrganizationMapper organizationMapper;
	
	@Value("${operativeName-lucene-path}")
	private String operativeNameLucenePath;

	@Test
	public void test() {
//		List<OperationCount> ocs = docMapper.xcxStatsByYearForOrgan("0mT9ShO7ME", "上海美星医疗美容门诊部", "2020-03-18");
//		System.out.println(ocs);
		
//		List<OperationCount> ocs = docMapper.xcxStatsByMonthForOrgan("0mT9ShO7ME", "上海美星医疗美容门诊部", "2021-03-18");
//		System.out.println(ocs);
		
//		List<OperationCount> ocs = docMapper.xcxStatsByWeekForOrgan2("0mT9ShO7ME", "上海美星医疗美容门诊部", "-1", "2021-05-18");
//		System.out.println(ocs);
		
//		List<WebScOrganization> organizations = organizationMapper.getOrgs();
//		System.out.println(organizations);
		
		
		splitOperative();
//		
//		List<String> operatives = webScDocOperativeMapper.getCommonlyUsedOperative("0Bd3dgEU1w", 20);
//		System.out.println(operatives);
//		List<WebScLabel> labels = webScLabelMapper.selectLabelsByIds("11,12,13");
//		System.out.println(labels);
//		List<WebScAnesthetic> anesthetics = anestheticMapper.getWebScAnesthetics();
//		System.out.println(anesthetics);
		
//		WebScDoc doc = docMapper.selectByPrimaryKey("00mF0PNJAUrihwTXWEC");
//		System.out.println(doc);
		
//		List<OperationCount> ssEs = docMapper.statsByMonthForOrgan("r5OPGFOBjTt0zhebLoP", "");
//		System.out.println(ssEs);
	}
	
	
	public void splitOperative() {
		try {
			List<WebScDoc> docs = docMapper.selectDocs(10000);
			Date nowDate = new Date();
			for (WebScDoc webScDoc : docs) {
				String operatives[] = webScDoc.getOperativeId().split(",");
				for (int i = 0; i < operatives.length; i++) {
					if (StringUtil.isNotEmpty(operatives[i])) {
						if (!"1".equals(webScDocOperativeMapper.isExist(webScDoc.getDocumentId()+"_"+operatives[i]))) {
							WebScDocOperative webScDocOperative = new WebScDocOperative();
							webScDocOperative.setAdminMemo(webScDoc.getAdminMemo());
							webScDocOperative.setAdminUserId(webScDoc.getAdminUserId());
							webScDocOperative.setAnestheticId(webScDoc.getAnestheticId());
							webScDocOperative.setApplyUserId(webScDoc.getApplyUserId());
							webScDocOperative.setCreateDate(nowDate);
							webScDocOperative.setDeleteReason(webScDoc.getDeleteReason());
							webScDocOperative.setDoctorEvaluate(webScDoc.getDoctorEvaluate());
							webScDocOperative.setDoctorEvaluateMemo(webScDoc.getDoctorEvaluateMemo());
							webScDocOperative.setDocumentId(webScDoc.getDocumentId());
							webScDocOperative.setDocumentOperativeId(webScDoc.getDocumentId()+"_"+operatives[i]);
							webScDocOperative.setDocumentState(webScDoc.getDocumentState());
							webScDocOperative.setDocumentTitle(webScDoc.getDocumentTitle());
							webScDocOperative.setDocumentType(webScDoc.getDocumentType());
							webScDocOperative.setHospitalEvaluate(webScDoc.getHospitalEvaluate());
							webScDocOperative.setHospitalEvaluateMemo(webScDoc.getHospitalEvaluateMemo());
							webScDocOperative.setHospitalMemo(webScDoc.getHospitalMemo());
							webScDocOperative.setMemo(webScDoc.getMemo());
							webScDocOperative.setOldDocumentState(webScDoc.getOldDocumentState());
							webScDocOperative.setOperateAide(webScDoc.getOperateQide());
							webScDocOperative.setOperateEndTime(webScDoc.getOperateEndTime());
							webScDocOperative.setOperateStartTime(webScDoc.getOperateStartTime());
							webScDocOperative.setOperateUser(webScDoc.getOperateUser());
							webScDocOperative.setOperationtypeId(webScDoc.getOperationtypeId());
							webScDocOperative.setOperativeId(operatives[i]);
							webScDocOperative.setOrgId(webScDoc.getOrgId());
							webScDocOperative.setPatientAge(webScDoc.getPatientAge());
							webScDocOperative.setPatientBednum(webScDoc.getPatientBednum());
							webScDocOperative.setPatientName(webScDoc.getPatientName());
							webScDocOperative.setPatientNum(webScDoc.getPatientNum());
							webScDocOperative.setPatientSex(webScDoc.getPatientSex());
							webScDocOperative.setPatienttypeId(webScDoc.getPatienttypeId());
							webScDocOperative.setQaMemo(webScDoc.getQaMemo());
							webScDocOperative.setQaTeamId(webScDoc.getQaTeamId());
							webScDocOperative.setQaUserId(webScDoc.getQaUserId());
							webScDocOperative.setQxRadio(webScDoc.getQxRadio());
							webScDocOperative.setTransferUserId(webScDoc.getTransferUserId());
							
							webScDocOperativeMapper.insert(webScDocOperative);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
