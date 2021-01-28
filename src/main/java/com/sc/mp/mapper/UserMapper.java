package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sc.mp.bean.OperationCount;
import com.sc.mp.model.WebScUser;

@Mapper
public interface UserMapper {
	@Select("SELECT user_id FROM WSC_USER WHERE role_id='5' AND user_name=#{doctorName} LIMIT 1")
	String getDoctorId(@Param("doctorName") String doctorName);
	
	/**
	 * 根据微信openid获取用户信息
	 * @param openid
	 * @return
	 */
	@Select("SELECT * FROM WSC_USER WHERE wx_openid=#{openid}")
	WebScUser selectUserByOpenid(@Param("openid") String openid);
	
	/**
	 * 修改微信openid
	 * @param openid
	 * @param userId
	 * @return
	 */
	@Update("UPDATE WSC_USER SET wx_openid=#{openid} WHERE user_id=#{userId}")
	int updOpenid(@Param("openid") String openid, @Param("userId") String userId);
	
	/**
	 * 解绑用户wxUserid wxopenid
	 * @param userId
	 * @return
	 */
	int unbindOpenid(@Param("userId") String userId);
	
	/**
	 * 系统用户绑定wxUserid openid
	 * @param user
	 * @return
	 */
	int bundOpenid(WebScUser user);
	
	/**
	 * 判断指定姓名的医生是否存在
	 * @param doctorName 医生姓名
	 * @return 大于0表示存在
	 */
	@Select("SELECT COUNT(*) FROM WSC_USER WHERE role_id='5' AND user_name=#{doctorName}")
	int isExistByDoctorName(@Param("doctorName") String doctorName);
	
	/**
	 * 获取指定区域的医生
	 * @param cityPre 区域编码，包括province和city
	 * @return
	 */
	@Select("SELECT * FROM WSC_USER WHERE role_id='5' AND city LIKE CONCAT(#{cityPre},'%')")
	List<WebScUser> selectDoctorsByQy(@Param("cityPre") String cityPre);
	
	WebScUser selectByPrimaryKey(String userId);
	
	WebScUser selectUserInfo(WebScUser user);
	
	/**
	 * 登录校验
	 * @param name
	 * @param pwd
	 * @return
	 */
	WebScUser selectByLoginName(@Param("loginName") String name);
	
	/**
	 * 统计医生全职兼职情况
	 * @return
	 */
	List<OperationCount> statsDoctorWork(@Param("roleId") int name);
	
	/**
	 * 查询所有的医生和护士
	 * @param roles 角色id
	 * @return
	 */
	List<WebScUser> selectDoctorAndNurse(List<String> roles);
	List<WebScUser> selectDoctorAndNurse();
	
	//发送信息用
	/**
	 * 查询订单对应区域管理员
	 * @return
	 */
	List<WebScUser> getDocumentLocalAdminUser(String documentId);
	
	/**
	 * 查询订单对应医生
	 * @return
	 */
	List<WebScUser> getDocumentQaUser(String documentId);
}