package com.subscriptiontracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.subscriptiontracker")
@EnableJpaRepositories(basePackages = "com.subscriptiontracker")
@EntityScan(basePackages =  "com.subscriptiontracker")
public class SubscriptionTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionTrackerApplication.class, args);
	}

}
