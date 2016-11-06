package com.xiaoyu;
/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
public class Application {
	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}
}
