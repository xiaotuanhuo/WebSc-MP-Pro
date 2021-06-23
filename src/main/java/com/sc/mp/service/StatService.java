package com.sc.mp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sc.mp.bean.OperationCount;
import com.sc.mp.mapper.DocMapper;
import com.sc.mp.util.ScConstant;

/**
 * 统计服务类
 * @author aisino
 *
 */
@Service
public class StatService {
	private static final Logger log = LoggerFactory.getLogger(StatService.class);
	
	@Resource
	private DocMapper docMapper;
	
	/**
	 * 日、月、年手术量二级页面统计
	 * @param type
	 * 				day 日
	 * 				month 月
	 * 				year 年
	 */
	public List<OperationCount> subInfo(String type) {
		List<OperationCount> infos = new ArrayList<OperationCount>();
		switch (type) {
			case ScConstant.DAY:
				infos = docMapper.daySubInfo();
				break;
			case ScConstant.MONTH:
				infos = docMapper.monthSubInfo();
				break;
			case ScConstant.YEAR:
				infos = docMapper.yearSubInfo();
				break;
			default:
				break;
		}
		return infos;
	}
	
	/**
	 * 医疗机构详情统计-基础统计
	 * @param organArray
	 * @return
	 */
	public List<OperationCount> statBasicData(JSONArray organArray) {
		List<String> organs = new ArrayList<String>();
		organArray.forEach(organ -> {
			JSONObject jOrgan = (JSONObject) organ;
			organs.add(jOrgan.getString("id"));
		});
		return docMapper.selectBasicData(organs);
	}
	
	/**
	 * 医疗机构详情统计-按日期统计
	 * @param organArray
	 * @return
	 */
	public List<OperationCount> statSubBasicData(JSONArray organArray, String slottime) {
		List<OperationCount> infos = new ArrayList<OperationCount>();
		List<String> organs = new ArrayList<String>();
		organArray.forEach(organ -> {
			JSONObject jOrgan = (JSONObject) organ;
			organs.add(jOrgan.getString("id"));
		});
		switch (slottime) {
			case ScConstant.DAY:
				infos = docMapper.selectOrganDay(organs);
				break;
			case ScConstant.MONTH:
				infos = docMapper.selectOrganMonth(organs);
				break;
			case ScConstant.YEAR:
				infos = docMapper.selectOrganYear(organs);
				break;
			default:
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					String startTime = format.format(format.parse(slottime.split("~")[0]));
					String endTime = format.format(format.parse(slottime.split("~")[1]));
					infos = docMapper.selectOrganDate(organs, startTime, endTime);
				} catch (Exception e) {
					log.error("日期转换错误..");
				}
				break;
		}
		return infos;
	}
	
	public List<OperationCount> statDocOperative() {
		return docMapper.statOperativeForDoc();
	}
	
	/**
	 * 医生详情统计-基础统计
	 * @param organArray
	 * @return
	 */
	public List<OperationCount> statDoctorBasic(String id) {
		return docMapper.statDrBasicData(id);
	}
	
	/**
	 * 医生手术量详情统计-按日期统计
	 * @param organArray
	 * @return
	 */
	public List<OperationCount> statDoctorSubInfo(String id, String slottime) {
		List<OperationCount> infos = new ArrayList<OperationCount>();
		switch (slottime) {
			case ScConstant.DAY:
				infos = docMapper.statDrInfoDay(id);
				break;
			case ScConstant.MONTH:
				infos = docMapper.statDrInfoMonth(id);
				break;
			case ScConstant.YEAR:
				infos = docMapper.statDrInfoYear(id);
				break;
			default:
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					String startTime = format.format(format.parse(slottime.split("~")[0]));
					String endTime = format.format(format.parse(slottime.split("~")[1]));
					infos = docMapper.statDrInfoDate(id, startTime, endTime);
				} catch (Exception e) {
					log.error("日期转换错误..");
				}
				break;
		}
		return infos;
	}
	
	public List<OperationCount> statDocAnesthetic() {
		return docMapper.statAnestheticForDoc();
	}
}
