package com.xiaoyu.common.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 2016年10月
 * 
 * @author xiaoyu
 * @description 解决跨域和请求头的问题
 * @version 1.0
 */


@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

	Logger logger = Logger.getLogger(WebConfiguration.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				logger.info("进入拦截器===================");
				
				//允许所有地址访问
				response.addHeader("Access-Control-Allow-Origin", "*");
				//允许那些请求方式可以访问
				response.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
				//允许前端可以设置哪些请求头
				response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,token,userId");
				//允许前端可以接收那些响应头
				response.addHeader("Access-Control-Expose-Headers", "token,userId");
				return true;
			}

			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {

			}

			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
					Exception ex) throws Exception {
			}

		});//.excludePathPatterns("/public/xiaoyu/users/login/**");
		super.addInterceptors(registry);
	}

}
