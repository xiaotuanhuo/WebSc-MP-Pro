package com.sc.mp.model.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 行政区划集合
 * @author aisino
 *
 */
public class District {

	Map<String, CDO> districtMap = new HashMap<String, CDO>();
	
	public Map<String, CDO> getDistrictMap() {
		return districtMap;
	}
	
	public void setDistrictMap(Map<String, CDO> districtMap) {
		this.districtMap = districtMap;
	}
}
