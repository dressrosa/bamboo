package com.xiaoyu.common.base;

import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.common.collect.Maps;

/**
 * 2017年3月16日下午5:22:13
 * 
 * @author xiaoyu
 * @description 返回数据的封装
 * @version 2.0
 */
public class ResponseMapper {
	/**
	 * 默认为成功
	 */
	private String code = ResultConstant.SUCCESS;

	private final ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object object, String name, Object value) {
			if (null == value || "null".equals(value))
				return "";
			return value;
		}
	};

	/**
	 * 封装响应的数据
	 */
	private Map<String, Object> dataMap = Maps.newHashMap();

	private ResponseMapper() {
		initMap();
	};

	/**
	 * 单例
	 */
	private static class MapperInstance {
		public static ResponseMapper mapper = new ResponseMapper();
	}

	public static ResponseMapper createMapper() {
		return MapperInstance.mapper;
	}

	private void initMap() {
		dataMap.put("code", code);
		dataMap.put("message", ResultConstant.SUCCESS_MESSAGE);
		dataMap.put("count", null);
		dataMap.put("datas", null);
	}

	public String getResultJson() {
		String json = JSON.toJSONString(dataMap, filter, SerializerFeature.EMPTY);
		this.initMap();// 重置map
		return json;
	}

	public ResponseMapper setCode(String code) {
		dataMap.put("code", code);
		switch (code) {
		case ResultConstant.ARGS_ERROR:
			dataMap.put("message", ResultConstant.ARGS_ERROR_MESSAGE);
			break;
		case ResultConstant.EXCEPTION:
			dataMap.put("message", ResultConstant.EXCEPTION_MESSAGE);
			break;
		case ResultConstant.EXISTS:
			dataMap.put("message", ResultConstant.EXISTS_MESSAGE);
			break;
		case ResultConstant.NOT_DATA:
			dataMap.put("message", ResultConstant.NOT_DATA_MESSAGE);
			break;
		}
		return this;
	}

	public ResponseMapper setMessage(String message) {
		if (message != null)
			dataMap.put("message", message);
		return this;
	}

	public ResponseMapper setCount(Long count) {
		if (count != null)
			dataMap.put("count", count);
		return this;
	}

	public ResponseMapper setDatas(Object datas) {
		if (datas != null)
			dataMap.put("datas", datas);
		return this;
	}
}
