package com.k3k.unplat.dao;


import com.k3k.unplat.entity.LoginUserEntity;

public interface LoginUserMapper {

	LoginUserEntity selectByUserName(String userName);
}
