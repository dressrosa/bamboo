package com.xiaoyu.modules.biz.users.service;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoyu.common.base.BaseService;
import com.xiaoyu.common.base.ResponseMapper;
import com.xiaoyu.common.utils.IdGenerator;
import com.xiaoyu.common.utils.JedisUtils;
import com.xiaoyu.modules.biz.users.dao.UserDao;
import com.xiaoyu.modules.biz.users.entity.User;
import com.google.common.collect.Maps;

/**
 * 2016年10月
 * 
 * @author xiaoyu
 * @description
 * @version 1.0
 */
@Service
@Transactional
public class UserService extends BaseService<UserDao, User> {

	private Map<String, Object> user2Map(User u) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("userId", u.getId());
		map.put("name", u.getLoginName());
		map.put("password", u.getPassword());
		return map;
	}

	public String userInfo(HttpServletResponse response, String userId) {
		ResponseMapper mapper = ResponseMapper.createMapper();
		User u = new User();
		u.setId(userId);
		User user = this.get(u);

		mapper.setData(this.user2Map(user));
		return mapper.getResultJson();
	}

	public String login(HttpServletResponse response, String loginName, String pwd) {
		ResponseMapper mapper = ResponseMapper.createMapper();
		User u = new User();
		u.setLoginName(loginName);
		u.setPassword(pwd);
		User user = this.get(u);
		if (user != null) {
			String token = IdGenerator.uuid();
			JedisUtils.set(token, user.getId(), 6 * 60 * 60);
			response.addHeader("token", token);
			mapper.setData(this.user2Map(user));
		}
		return mapper.getResultJson();
	}

}
