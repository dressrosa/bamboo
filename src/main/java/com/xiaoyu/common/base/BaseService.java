/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.common.base;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.xiaoyu.common.utils.IdGenerator;
import com.xiaoyu.common.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 基本CRUD操作
 * 
 * @author xiaoyu 2016年3月22日
 */
public abstract class BaseService<D extends BaseDao<T>, T extends BaseEntity> {

	@Autowired
	private D tDao;

	/**
	 * 获取t,
	 * 
	 * @author xiaoyu
	 * @param t
	 * @return
	 * @time 2016年3月22日下午3:13:24
	 */
	public T get(T t) {
		T temp = null;
		temp = this.tDao.get(t);
		if (null == temp) {
			return t;
		}
		return temp;
	}

	/**
	 * 删除
	 * 
	 * @author xiaoyu
	 * @param t
	 * @return
	 * @time 2016年3月22日下午3:14:41
	 */
	public int delete(T t) {
		t.setDeleteDate(new Date());
		return this.tDao.delete(t);
	}

	/**
	 * 查找集合
	 * 
	 * @author xiaoyu
	 * @param t
	 * @return
	 * @time 2016年3月22日下午3:21:49
	 */
	public List<T> findByList(T t) {
		List<T> list = null;
		list = this.tDao.findList(t);
		return list;
	}

	/**
	 * 分页查找
	 * 
	 * @author xiaoyu
	 * @param t
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @time 2016年3月22日下午3:29:14
	 */
	public Page<T> findByPage(T t, int pageNum, int pageSize) {
		Page<T> page = new Page<T>();
		if (null == t) {
			return page;
		}
		PageHelper.startPage(pageNum, pageSize);
		page = (Page<T>) this.tDao.findList(t);
		return page;
	}

	/**
	 * 更新
	 * 
	 * @author xiaoyu
	 * @param t
	 * @return
	 * @time 2016年3月22日下午3:38:49
	 */
	public int update(T t) {
		int temp = 0;
		if (null == t) {
			return temp;
		}
		Date date = new Date();
		t.setUpdateDate(date);
		temp = this.tDao.update(t);
		return temp;
	}

	/**
	 * 保存
	 * 
	 * @author xiaoyu
	 * @param t
	 * @return
	 * @time 2016年3月22日下午3:39:25
	 */
	public int save(T t) {
		int temp = 0;
		if (null == t) {
			return temp;
		}
		Date date = new Date();
		if (StringUtils.isBlank(t.getId()))
			t.setId(IdGenerator.uuid());
		t.setCreateDate(date);
		t.setUpdateDate(date);
		temp = this.tDao.insert(t);
		return temp;
	}

	/**
	 * 判断是否存在
	 * 
	 * @author xiaoyu
	 * @param t
	 * @return
	 * @time 2016年3月22日下午3:13:11
	 */
	@SuppressWarnings("unused")
	private boolean isExist(T t) {
		if (null == t) {
			return false;
		}
		int count = this.tDao.isExist(t);
		if (count > 0) {
			return true;
		}
		return false;
	}
}
