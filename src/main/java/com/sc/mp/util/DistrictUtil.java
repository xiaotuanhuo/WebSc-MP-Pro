package com.sc.mp.util;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sc.mp.bean.District;

/**
 * 全国行政区划读取工具类
 * @author aisino
 *
 */
@Component
public class DistrictUtil implements CommandLineRunner {
	private static Map<String, District> districtMap = new HashMap<String, District>();
	
	public static District getDistrictByCode(String code) {
		return districtMap.get(code);
	}
	
	@Override
	public void run(String... args) throws Exception {
		try {
    		// 读取数据权限json文件
    		ClassPathResource classPathResource = new ClassPathResource("/static/data/address.json");
    		String districtJson = IOUtils.toString(new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));
    		JSONArray jsonArray = JSONObject.parseArray(districtJson);
    		getDistrict(jsonArray, "86");		// 省级区划父节点为86
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private static void getDistrict(JSONArray jsonArray, String parentCode) {
    	for (int i = 0; i < jsonArray.size(); i++) {
    		District district = new District();
    		String code = jsonArray.getJSONObject(i).getString("code");
    		district.setCode(code);
    		district.setName(jsonArray.getJSONObject(i).getString("name"));
    		district.setParentCode(parentCode);
        	JSONArray recursiveArray = jsonArray.getJSONObject(i).getJSONArray("childs");
        	if (recursiveArray != null) {
				getDistrict(recursiveArray, code);
			}
        	districtMap.put(code, district);
    	}
    }
	
}
