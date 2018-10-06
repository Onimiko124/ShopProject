package com.qf.shop_order;

import com.qf.util.LoginAOP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopOrderApplication.class, args);
	}

	/**
	 * 相当于在spring容器中配置了一个<bean></bean>
	 * @return
	 */
	@Bean
	public LoginAOP getLoginAOP() {
		return new LoginAOP();
	}
}
