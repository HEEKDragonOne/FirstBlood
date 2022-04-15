package com.tothefor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@MapperScan("com.tothefor.mapper")
@SpringBootApplication
public class SchoolJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolJavaApplication.class, args);
	}

}
