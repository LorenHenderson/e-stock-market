package com.market.stock.companydomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoRepositories
@EnableFeignClients
public class CompanyDomainApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyDomainApplication.class, args);
	}
}
