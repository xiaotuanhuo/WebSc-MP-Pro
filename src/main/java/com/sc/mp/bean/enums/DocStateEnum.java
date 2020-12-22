package com.sc.mp.bean.enums;

public enum DocStateEnum {
	
	submited("0", "已提交"),
	released("1", "已发布"),
	matched("2", "已匹配"),
	confirmed("3", "已确认"),
	completing("4", "待完成"),
	completed("5", "已完成"),
	canceled("6", "已取消"),
	canceling("9", "待取消");
	
	private String value;
	private String txt;
	
	private DocStateEnum(String value, String txt) {
		this.value = value;
		this.txt = txt;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getTxt() {
		return txt;
	}
	
	public String toString() {
		return "{[value:"+value+"],[txt:"+txt+"]}";
	}
	
	public static DocStateEnum getvalueOf(String value){
		if(value!=null){
			for(DocStateEnum docStateEnum:DocStateEnum.values()){
				if(value.equals(docStateEnum.getValue())){
					return docStateEnum;
				}
			}
		}
		return null;
	}
	
	public static DocStateEnum txtOf(String txt){
		if(txt!=null){
			for(DocStateEnum docStateEnum:DocStateEnum.values()){
				if(txt.equalsIgnoreCase(docStateEnum.getTxt())){
					return docStateEnum;
				}
			}
		}
		return null;
	}
	public static DocStateEnum nameOf(String name){
		for(DocStateEnum docStateEnum: DocStateEnum.values()){
			if(docStateEnum.name().equals(name)){
				return docStateEnum;
			}
		}
		return null;
	}
}
