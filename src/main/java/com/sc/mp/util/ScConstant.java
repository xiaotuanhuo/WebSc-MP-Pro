package com.sc.mp.util;

/**
 * 常量
 * @author aisino
 *
 */
public class ScConstant {
	// 用户信息在session存放的属性key
	public final static String USER_SESSION_KEY = "WEBSCMP_JSESSIONID";
	// 按机构或者手术大类统计的时段常量
	public final static String DAY = "day";
	public final static String WEEK = "week";
    public final static String MONTH = "month";
    public final static String YEAR = "year";
    // 统计报表时间段选择常量
    public final static String SLOTWEEK = "sortWeek";
    public final static String SLOTMONTH = "sortMonth";
    public final static String SLOTHALFYEAR = "sortHalfYear";
    public final static String SLOTYEAR = "sortYear";
    // 医生备休时段常量
	public final static String AM = "am";
	public final static String PM = "pm";
	public final static String ALL = "all";
	public final static String AM_TEXT = "上午";
	public final static String PM_TEXT = "下午";
	public final static String ALL_TEXT = "全天";
	public final static String CAL_PREFIX = "备休时段：";
	// 统计手术量TOP时直辖市代码及对应城市名
	public final static String BJS_CODE = "1101";
	public final static String BJS_NANE = "北京市";
	public final static String TJS_CODE = "1201";
	public final static String TJS_NANE = "天津市";
	public final static String SHS_CODE = "3101";
	public final static String SHS_NANE = "上海市";
	public final static String CQS_CODE = "5001";
	public final static String CQS_NANE = "重庆市";
	public final static String OTHER_PROVINCE = "86";
	public final static String OTHER_PROVINCE_TEXT = "其他";
	
	// 虚拟的医疗机构id
	public final static String FICTITIOUS_ORG = "-0000";
	
	// 日志及提示信息
	public final static String USER_ERROR = "用户名或者密码错误";
	public final static String USERNAME_ERROR = "用户名不存在:";
	public final static String PASSWORD_ERROR = "密码不正确:";
	public final static String NO_AUTH = "用户无权限";
	public final static String ADD_SUC = "添加成功";
	public final static String ADD_FAIL = "添加失败";
	public final static String DEL_SUC = "删除成功";
	public final static String DEL_FAIL = "删除失败";
	public final static int MANY_LOGIN_CODE = 9;	// 多用户登录
	public final static String MANY_LOGIN_MSG = "当前用户已在其他地方登录 请重新登录";	// 多用户登录
	public final static String USER_INACTIVE = "用户已锁定";
	
	// 用户状态
	public static final String INACTIVE = "0";
	public static final String ACTIVE = "1";
}
