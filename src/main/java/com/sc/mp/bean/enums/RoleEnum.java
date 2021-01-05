package com.sc.mp.bean.enums;

/**
 * 角色的枚举
 * @author aisino
 *
 */
public enum RoleEnum {

	XTGLY(1, "xtgly"),			// 系统管理员
	YLJGGLY(2, "yljggly"),		// 医疗机构管理员
	WJJGLY(3, "wjjgly"),		// 卫监局管理员
	QYGLY(4, "qygly"),			// 区域管理员
	YS(5, "ys"),				// 医生
	HS(6, "hs"),				// 护士
	CJGLY(7, "cjgly"),			// 超级管理员
	QYDDLRY(8, "qyddlry"),		// 区域订单录入员
	JGDDLRY(9, "jgddlry");		// 医疗机构订单录入员
	
	private int code;
	private String type;
	private RoleEnum(int code, String type) {
		this.code = code;
		this.type = type;
	}
	
	public static RoleEnum valueOf(int code) {
		for(RoleEnum re : RoleEnum.values()){
			if(re.code == code)
				return re;
		}
		return null;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
