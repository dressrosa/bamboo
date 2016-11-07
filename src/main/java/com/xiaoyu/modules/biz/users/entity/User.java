package com.xiaoyu.modules.biz.users.entity;

import com.xiaoyu.common.base.BaseEntity;

/**
 * 2016年10月
 * 
 * @author xiaoyu
 * @description
 * @version 1.0
 */
public class User extends BaseEntity {

	private static final long serialVersionUID = 2636629209237547170L;
	private String loginName; // 姓名
	private String password; // 登录密码
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
