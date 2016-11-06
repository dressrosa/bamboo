/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * @author xiaoyu 2016年3月19日
 */
public class SerializeUtil {

	private static Logger logger = Logger.getLogger(SerializeUtil.class);

	/**
	 * 序列化对象
	 * 
	 * @author xiaoyu
	 * @param object
	 * @return
	 * @time 2016年3月19日下午8:41:24
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			if (object != null) {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			logger.debug("序列化失败", e);
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @author xiaoyu
	 * @param bytes
	 * @return
	 * @time 2016年3月19日下午8:43:26
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			if (bytes != null && bytes.length > 0) {
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		} catch (Exception e) {
			logger.debug("反序列化失败", e);
		}
		return null;
	}
}
