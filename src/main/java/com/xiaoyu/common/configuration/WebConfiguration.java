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
 * @description 暫時沒用到 改用aop
 * @version 1.0
 */

@Deprecated
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
				return true;
			}

			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {

			}

			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
					Exception ex) throws Exception {
			}

		}).excludePathPatterns("/public/edencity/mall/users/login/**");
		super.addInterceptors(registry);
	}

}
