package com.xiaoyu.modules.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xiaoyu.common.base.ResponseMapper;
import com.xiaoyu.common.base.ResultConstant;
import com.xiaoyu.common.utils.StringUtils;
import com.xiaoyu.modules.biz.users.service.UserService;

/**
 * 2016年10月
 * 
 * @author xiaoyu
 * @description 用户相关接口
 * @version 1.0
 */
@Configuration
@RestController
public class UserController {

	protected Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/********************************************************************
	 * ******************************************************************
	 * *******************↓↓↓↓↓↓↓↓↓↓↓用户基本操作接口↓↓↓↓↓↓↓↓↓↓↓***********
	 * ******************************************************************
	 */
	
	@RequestMapping("public/xiaoyu/users/login/v1")
	public String login(HttpServletRequest request, HttpServletResponse response, String loginName,String pwd) {
		ResponseMapper mapper = new ResponseMapper();
		try {
			return this.userService.login(response,loginName,pwd);
		} catch (Exception e) {
			mapper.setCode(ResultConstant.EXCEPTION);
			return mapper.getResultJson();
		}
	}

	@RequestMapping("public/xiaoyu/users/userInfo/v1")
	public String userInfo(HttpServletRequest request, HttpServletResponse response, String userId) {
		ResponseMapper mapper = new ResponseMapper();
		mapper.setCode(ResultConstant.ARGS_ERROR);
		if (StringUtils.isBlank(userId)) {
			return mapper.getResultJson();
		}
		try {
			return this.userService.userInfo(response, userId);
		} catch (Exception e) {
			mapper.setCode(ResultConstant.EXCEPTION);
			return mapper.getResultJson();
		}
	}

}
