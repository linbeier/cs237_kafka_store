package com.cs237.kafkajava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@SpringBootApplication
@MapperScan("com.cs237.kafkajava.mapper")
public class KafkaJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaJavaApplication.class, args);
	}

}
