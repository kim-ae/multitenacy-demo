package br.com.kimae.multitenacydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MultitenacyDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(MultitenacyDemoApplication.class, args);
	}
}
