package com.sc.mp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sc.mp.model.WebScUser;

@Mapper
public interface UserMapper {
    WebScUser selectByPrimaryKey(Integer userId);
    
    WebScUser selectUserInfo(WebScUser user);
}