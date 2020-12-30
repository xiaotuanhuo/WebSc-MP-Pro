package com.sc.mp.util;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sc.mp.model.vo.CDO;
import com.sc.mp.model.vo.District;

public class DistrictUtil {
	
	@Bean(value = "district")
    public District district() {
    	District district = new District();
    	try {
    		// 读取数据权限json文件
    		ClassPathResource classPathResource = new ClassPathResource("/static/json/address.json");
    		String districtJson = IOUtils.toString(new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));
    		JSONArray jsonArray = JSONObject.parseArray(districtJson);
    		Map<String, CDO> map = getDistrict(jsonArray, "0");		// 省级区划父节点为0
    		district.setDistrictMap(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return district;
    }
	
	/**
     * 递归获取所有行政区划
     * @param jsonArray
     * @param parentId
     * @return
     */
    private Map<String, CDO> getDistrict(JSONArray jsonArray, String parentId) {
    	Map<String, CDO> cdoMap = new HashMap<String, CDO>();
    	
    	for (int i = 0; i < jsonArray.size(); i++) {
    		CDO cdo = new CDO();
    		String id = jsonArray.getJSONObject(i).getString("code");
    		cdo.setId(id);
    		cdo.setName(jsonArray.getJSONObject(i).getString("name"));
    		cdo.setParentId(parentId);
        	JSONArray recursiveArray = jsonArray.getJSONObject(i).getJSONArray("childs");
        	if (recursiveArray != null) {
				getDistrict(recursiveArray, id);
			}
        	cdoMap.put(id, cdo);
    	}
    	return cdoMap;
    }
}
