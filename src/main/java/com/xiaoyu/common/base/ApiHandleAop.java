/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.common.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.xiaoyu.common.utils.JedisUtils;

/**
 * 接口权限处理:登陸的判斷和接口訪問次數的限制
 * 
 * @author xiaoyu 2016年4月3日
 */
@Configuration
@Aspect
public class ApiHandleAop {

	private static Logger logger = Logger.getLogger(ApiHandleAop.class);

	private static final int URI_LIMIT = 1000;// 限制次数

	@Pointcut("execution(* com.edencity.edencity.modules.sys.controller..*.*(..))")
	public void pointcut() {

	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) {
		Object[] args = point.getArgs();
		HttpServletRequest request = null;
		String ip = null;// 客户端ip
		String uri = null;// 接口地址
		String userId = null;// 用户id
		String token = null;// 登录 token
		String ipLimit = null;//
		String redis_userId = null;
		String methodName = null;// 调用方法名
		int limit = 0;// ip限制访问次数
		for (Object o : args) {
			if (o instanceof HttpServletRequest)
				request = (HttpServletRequest) o;
		}
		ip = request.getRemoteHost();
		uri = request.getRequestURI();
		logger.info("ip:" + ip + " uri:" + uri);
		methodName = point.getSignature().getName();

		// 无需登录情况下
		if ("xxxx".equals(methodName)) {
			ipLimit = JedisUtils.hget(methodName + ":" + ip, uri);
			if (null == ipLimit) {
				JedisUtils.hset(methodName + ":" + ip, uri, 1 + "", 60);
				ipLimit = "1";
			}
			limit = Integer.valueOf(ipLimit);
			if (limit > 10) {
				ResponseMapper mapper = ResponseMapper.createMapper();
				mapper.setCode(ResultConstant.EXCEPTION).setMessage("访问次数异常,一分钟之内无法访问");
				JedisUtils.hset(methodName + ":" + ip, uri, limit + "", 60);
				JedisUtils.hincrby(methodName + ":" + ip, uri, 1);
				return mapper.getResultJson();
			} else {
				JedisUtils.hincrby(methodName + ":" + ip, uri, 1);
			}
			return this.getResult(point);
		} else if (uri.startsWith("/public")) {
			ipLimit = JedisUtils.hget(methodName + ":" + ip, uri);
			if (null == ipLimit) {
				JedisUtils.hset(methodName + ":" + ip, uri, 1 + "", 60);
				ipLimit = "1";
			}
			limit = Integer.valueOf(ipLimit);
			if (limit > URI_LIMIT) {
				ResponseMapper mapper = ResponseMapper.createMapper();
				mapper.setCode(ResultConstant.EXCEPTION).setMessage("访问次数异常,一分钟之内无法访问");
				JedisUtils.hset(methodName + ":" + ip, uri, limit + "", 60);
				JedisUtils.hincrby(methodName + ":" + ip, uri, 1);
				return mapper.getResultJson();
			} else {
				JedisUtils.hincrby(methodName + ":" + ip, uri, 1);
			}
			return this.getResult(point);
		}
		// 需登录情况下
		userId = request.getHeader("userId");
		token = request.getHeader("token");
		redis_userId = JedisUtils.get(token);
		if (null == redis_userId) {
			ResponseMapper mapper = ResponseMapper.createMapper();
			mapper.setCode(ResultConstant.ARGS_ERROR).setMessage("token异常,请重新登录");
			return mapper.getResultJson();
		}
		if (redis_userId.equals(userId)) {
			JedisUtils.set(token, userId, 6 * 60 * 60);
		} else {
			ResponseMapper mapper = ResponseMapper.createMapper();
			mapper.setCode(ResultConstant.ARGS_ERROR).setMessage("请先登录");
			return mapper.getResultJson();
		}
		// 一个用户同一个ip下访问同一个api的次数(1 min之内)
		ipLimit = JedisUtils.hget(userId + ":" + ip, uri);
		if (null == ipLimit) {
			JedisUtils.hset(userId + ":" + ip, uri, 1 + "", 60);
			ipLimit = "1";
		}
		limit = Integer.valueOf(ipLimit);
		if (limit > URI_LIMIT) {
			ResponseMapper mapper = ResponseMapper.createMapper();
			mapper.setCode(ResultConstant.EXCEPTION).setMessage("访问次数异常,一分钟之内无法访问");
			JedisUtils.hset(userId + ":" + ip, uri, limit + "", 60);
			JedisUtils.hincrby(userId + ":" + ip, uri, 1);
			return mapper.getResultJson();
		} else {
			JedisUtils.hincrby(userId + ":" + ip, uri, 1);
		}
		return getResult(point);
	}

	private Object getResult(ProceedingJoinPoint point) {
		Object result = null;
		long start = 0;
		long end = 0;
		String methodName = point.getSignature().getName();
		try {
			start = System.currentTimeMillis();
			result = point.proceed();
			end = System.currentTimeMillis();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		logger.info("方法[" + methodName + "]执行时间为:[" + (end - start) + " milliseconds] ");
		return result;
	}

	/*
	 * 解决web端jsonp跨域的问题 但是有很大的局限性,前端不能設置headers 所以采用拦截器,由后端解决跨域问题
	 * 
	 */
	@Deprecated
	private Object getCrossResult(ProceedingJoinPoint point, HttpServletRequest request) {
		Object result = null;
		long start = 0;
		long end = 0;
		String methodName = point.getSignature().getName();
		try {
			start = System.currentTimeMillis();
			result = point.proceed();
			end = System.currentTimeMillis();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		logger.info("方法[" + methodName + "]执行时间为:[" + (end - start) + " milliseconds] ");
		return request.getParameter("callback") + "(" + result + ")";
	}
}
