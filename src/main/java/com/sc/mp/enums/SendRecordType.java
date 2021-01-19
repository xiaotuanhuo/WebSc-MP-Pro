package com.sc.mp.enums;

public enum SendRecordType {
	/** 医院操作 */
	//发布订单页面
	hospital_commit_order(1001,"发布新订单","hospital_commit_order"),//发布订单页面：医院发布订单
	//已提交--0
	hospital_cancel_commited_order(1101,"取消订单[已提交->已取消]","hospital_cancel_commited_order"),
	hospital_update_commited_order(1102,"修改订单[已提交]","hospital_update_commited_order"),
	//已审核--1
	hospital_cancel_audited_order(1201,"取消订单[已审核->待取消]","hospital_cancel_audited_order"),//1--->9(gly消息)
	hospital_update_audited_order(1202,"修改订单[已审核]","hospital_update_audited_order"),//
	//已匹配--2
	hospital_cancel_distributioned_order(1301,"取消订单[已匹配->待取消]","hospital_cancel_distributioned_order"),//2--->9(gly消息)
	//已接单--3
	hospital_cancel_confirmed_order(1401,"取消订单[已接单->待取消]","hospital_cancel_confirmed_order"),//3--->9(gly消息)
	//待完成--4
	hospital_agreed_complete_order(1501,"同意订单完成[待完成->已完成]","hospital_agreed_complete_order"),//4-->5
	hospital_refused_complete_order(1502,"拒绝订单完成[待完成->已接单]","hospital_refused_complete_order"),//4-->3(ys消息)

	/** 医生操作 */
	//已分配--2
	doctor_agreed_distribution_order(2001,"接受订单[已分配->已接单]","doctor_agreed_distribution_order"),//2-->3医生
	doctor_refused_distribution_order(2002,"拒绝订单[已分配]","doctor_refused_distribution_order"),//电话联系管理员（gly消息）
	//已接单--3
	doctor_undo_confirmed_order(2101,"撤销订单[已接单->已分配]","doctor_undo_confirmed_order"),//3-->2医生---（gly消息）
	doctor_commit_confirmed_order(2102,"提交订单[已接单->待完成]","doctor_commit_confirmed_order"),//3-->4医生（yy消息）
	doctor_exchange_comfirmed_order(2103,"转单[已接单]","doctor_exchange_comfirmed_order"),//--->(ys消息)
	
	/** 管理员操作 */
	//已提交--0
	gly_cancel_commited_order(3001,"取消订单[已提交->待取消]","gly_cancel_commited_order"),//0-->9
	gly_comfirm_commited_order(3002,"确认订单[已提交->已审核]","gly_comfirm_commited_order"),//0-->1
	//已审核--1
	gly_cancel_audited_order(3101,"取消订单[已审核->待取消]","gly_cancel_audited_order"),//1-->9
	gly_match_audited_order(3102,"匹配订单[已审核->已匹配]","gly_match_audited_order"),//1-->2----（ys消息）
	//已匹配--2
	gly_cancel_distributioned_order(3201,"取消订单[已匹配->待取消]","gly_cancel_distributioned_order"),//2-->9
	gly_undo_distributioned_order(3202,"取消匹配[已匹配->已审核]","gly_undo_distributioned_order"),//2-->1---（ys消息）
	//已接单--3
	gly_cancel_confirmed_order(3301,"取消订单[已接单->待取消]","gly_cancel_confirmed_order"),//3-->9-------？
	//待完成--4
	gly_cancel_complete_order(3401,"取消订单[待完成->待取消]","gly_cancel_complete_order"),//4-->9
	gly_agreed_complete_order(3402,"同意完成订单[待完成->已完成]","gly_agreed_complete_order"),//4-->5
	gly_Refused_complete_order(3403,"拒绝完成订单[待完成->已接单]","gly_Refused_complete_order"),//4-->3
	//已完成--5
	gly_supplement_memo_order(3501,"补充单据备注[已完成]","gly_supplement_memo_order"),
	//待取消--9
	gly_agreed_canceled_order(3601,"同意取消订单[待取消->已取消]","gly_agreed_canceled_order"),//9-->6 ----管理员（ys  and yy消息）
	gly_Refused_canceled_order(3602,"拒绝取消订单","gly_Refused_canceled_order");//9-->原来状态 ----管理员
	
	private int value;
	private String text;
	private String keyValue;
	
	private SendRecordType(int value, String text,String keyValue){
		this.value = value;
		this.text = text;
		this.keyValue=keyValue;
	}
	
	public static SendRecordType valueOf(int value){
		for(SendRecordType rot:SendRecordType.values()){
			if(rot.value == value)
				return rot;
		}
		return null;
	}
	
	public int getValue(){
		return value;
	}
	
	public String toString(){
		return text;
	}
	
	public String getKeyValue() {
		return keyValue;
	}
}
