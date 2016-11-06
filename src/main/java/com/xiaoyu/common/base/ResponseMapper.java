package com.xiaoyu.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.common.collect.Maps;
import java.util.Map;

/**返回結果的封裝
 * @author xiaoyu
 *
 */
public class ResponseMapper {
	private String code = ResultConstant.SUCCESS;
	private String message = null;
	private Long count;
	private Object datas;
	/* 解決fastjson null轉為"" */
	private final ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object arg0, String arg1, Object value) {
			if (null == value)
				return "";
			return value;
		}
	};

	public String getResultJson() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("code", this.code);
		switch (this.code) {
		case ResultConstant.ARGS_ERROR:
			map.put("message", ResultConstant.ARGS_ERROR_MESSAGE);
			break;
		case ResultConstant.EXCEPTION:
			map.put("message", ResultConstant.EXCEPTION_MESSAGE);
			break;
		case ResultConstant.EXISTS:
			map.put("message", ResultConstant.EXISTS_MESSAGE);
			break;
		case ResultConstant.NOT_DATA:
			map.put("message", ResultConstant.NOT_DATA_MESSAGE);
			break;
		default:
			map.put("message", ResultConstant.SUCCESS_MESSAGE);
		}
		if (message != null)
			map.put("message", this.message);
		if (this.count != null) {
			map.put("count", this.count);
		}
		if (this.datas != null) {
			map.put("datas", this.datas);
		}

		return JSON.toJSONString(map, filter, SerializerFeature.EMPTY);
	}

	public ResponseMapper setCode(String code) {
		this.code = code;
		return this;
	}

	public ResponseMapper setMessage(String message) {
		this.message = message;
		return this;
	}

	public ResponseMapper setCount(Long count) {
		this.count = count;
		return this;
	}

	public ResponseMapper setDatas(Object datas) {
		this.datas = datas;
		return this;
	}

}
