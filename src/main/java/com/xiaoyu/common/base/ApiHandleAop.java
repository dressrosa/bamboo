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

	 @Pointcut("execution(* com.xiaoyu.modules.sys.controller..*.*(..))")
	public void pointcut() {

	}

	 @Around("pointcut()")
	public Object around(ProceedingJoinPoint point) {
		Object[] args = point.getArgs();
		HttpServletRequest request = null;
		String userId = null;
		String token = null;
		String ip = null;
		String uri = null;
		for (Object o : args) {
			if (o instanceof HttpServletRequest)
				request = (HttpServletRequest) o;
		}
		String methodName = point.getSignature().getName();
		if ("login".equals(methodName)) {
			return this.getResult(point);
		}

		 userId = request.getHeader("userId");
		 token = request.getHeader("token");
		 ip = request.getRemoteHost();
		 uri = request.getRequestURI();
		// System.out.println("ip:::" + ip);
		// System.out.println("uri:::" + request.getRequestURI());

		String redis_userId = JedisUtils.get(token);
		if (null == redis_userId) {
			ResponseMapper mapper = new ResponseMapper();
			mapper.setCode(ResultConstant.ARGS_ERROR).setMessage("token异常,请重新登录");
			return mapper.getResultJson();
		}
		if (redis_userId.equals(userId)) {
			JedisUtils.set(token, userId, 6 * 60 * 60);
		} else {
			ResponseMapper mapper = new ResponseMapper();
			mapper.setCode(ResultConstant.ARGS_ERROR).setMessage("请先登录");
			return mapper.getResultJson();
		}
		// 一个用户同一个ip下访问同一个api的次数(1 min之内)
		String ipLimit = JedisUtils.hget(userId + ":" + ip, uri);
		if (null == ipLimit) {
			JedisUtils.hset(userId + ":" + ip, uri, 1 + "", 60);
			ipLimit = "1";
		}
		int limit = Integer.valueOf(ipLimit);
		if (limit > 100) {
			ResponseMapper mapper = new ResponseMapper();
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
	/*解决web端jsonp跨域的问题*/
	private Object getCrossResult(ProceedingJoinPoint point,HttpServletRequest request) {
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
		return request.getParameter("callback")+"("+result+")";
	}
}
