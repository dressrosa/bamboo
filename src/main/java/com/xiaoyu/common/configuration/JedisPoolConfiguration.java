/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 配置redis连接池
 * 
 * @author xiaoyu 2016年3月19日
 */
@Configuration
public class JedisPoolConfiguration {

	@Value("${redis.pool.maxIdle}")
	private Integer maxIdle;// 最大空闲数

	@Value("${redis.pool.minIdle}")
	private Integer minIdle;// 最小空闲数

	@Value("${redis.pool.maxTotal}")
	private Integer maxTotal;// 最大连接数

	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的

	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;// 在return给pool时，是否提前进行validate操作

	@Value("${redis.pool.host}")
	private String host;// 地址

	@Value("${redis.pool.port}")
	private Integer port;// 端口

	@Value("${redis.pool.password}")
	private String password;// 密码

	@Value("${redis.pool.database}")
	private Integer db;// 数据库

	@Value("${redis.pool.timeout}")
	private Integer timeout;// 数据库

	@Bean(name = "jedisPool")
	public JedisPool jedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		JedisPool pool = null;
		config.setMaxIdle(maxIdle);
		config.setTestOnBorrow(testOnBorrow);
		config.setTestOnReturn(testOnReturn);
		config.setMaxTotal(maxTotal);
		config.setMaxWaitMillis(5000);
		config.setMinIdle(minIdle);
		pool = new JedisPool(config, host, port, timeout, password, db);
		return pool;
	}

}
